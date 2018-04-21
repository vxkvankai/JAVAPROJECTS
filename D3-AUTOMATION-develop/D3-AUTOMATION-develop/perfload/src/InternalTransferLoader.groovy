import groovy.sql.Sql
import groovy.util.logging.Log4j

import java.util.concurrent.atomic.AtomicLong

/**
 * Copyright D3Banking
 * User: lpresswood
 * Date: 10/29/14
 * Time: 3:11 PM
 *
 */
@Log4j
class InternalTransferLoader {
    static final TO = 'TOACCT'
    static final FROM = 'FROMACCT'

    static final Savings = 'DEPOSIT_SAVINGS'
    static final Checking = 'DEPOSIT_CHECKING'

    final Sql gsql
    int totalToLoad
    def daysForTransfers
    Properties properties

    InternalTransferLoader(Sql gsql, int totalToLoad,
                           def daysForTransfers,
                           Properties properties) {
        this.gsql = gsql
        this.totalToLoad = totalToLoad
        this.daysForTransfers = daysForTransfers
        this.properties = properties
    }

    InternalTransferLoader(Sql sql) {
        this.gsql = sql
    }

    static def create32ByteGuid() {
        UUID uuid = UUID.randomUUID()
        return uuid.toString().replace("-", "")
    }

    def loadTransfers(){
        def userSubset = this.getSubsetOfUsers()

        int transferCounter=0
        for (int userId in userSubset) {
            def toFromAcct = this.getFromToAccount(userId)
            if (toFromAcct == null) {
                log.error "Unable to find to/from accounts for userid $userId"
            }

            def from = toFromAcct.get(FROM)
            def to = toFromAcct.get(TO)
            if (from == null || to == null) {
                log.error "Either to or from account was not found or null $from - $to"
            }

            assert from != null: "From Account was null "
            assert to != null: "To Account was null"


            daysForTransfers.each { Date transferDate ->
              createTransfer(userId,toFromAcct,new BigDecimal(1.0),new java.sql.Date(transferDate.getTime()))
            }


            transferCounter++
            if (transferCounter >= totalToLoad) {
                break
            }

        }

    }

    /*
     *  First we need to get our subset of users since on average we will have 12k IT per day.
     *  Hence a percentage of our 300k users only d3_users with login_id begin with perf-
     *
     *  Got to figure out from_account/to_account
     *  For internal transfers d3_account.source = 'INTERNAL'
     *
     *  select d3a.account_status,ua.* from d3_account d3a, user_account ua
     *  where ua.account_id = d3a.id and d3a.source = 'INTERNAL'
     *
     *
     * For each user (some subset of users – can not recall how big that set was)
     * Read userAccount (from_account) // d3_user -> user_account <- d3_account
     * Read userAccount (to_account)
     * Read recipient (for user and to_account)
     * If no recipient create one
     * This would be mm_recipient - What category ?
     * For internal transfer create if not exist mm_internal_account_recipient
     * mm_transfer 1-(0.1) mm_internal_transfer
     * Create internalTransfer (for user, recipient, to_account, from_account) mm_internal_transfer
     *   References mm_internal_account_recipient - needs to be created if does not exist
     *   References mm_transfer - needs to be created if does not exist?
     * (some amount, some note, schedule date in the past, status PROCESSED, some post sequence, some conf number)
     * End for
     *
     */

    def createTransfer(int userId, def toFromAcctId, BigDecimal amount, java.sql.Date schedDate) {
        int from = toFromAcctId.get(FROM)
        int to = toFromAcctId.get(TO)

        // Ok create or lookup the recipient
        def mmRecipId = lookupOrCreateRecipient(userId, to)

        // now we need to crete mm_transfer

        def mmTransferSql = "insert into mm_transfer (created_ts,deleted,amount,scheduled_date," +
                "from_account_id,user_id) values(GetDate(),?,?,?,?,?)"

        def xferkey = gsql.executeInsert(mmTransferSql, [0, amount, schedDate, from, userId])
        if (xferkey != null) {
            def transferId = xferkey[0][0]

            def confirmNbr = create32ByteGuid()
            def note = "PERF-" + create32ByteGuid()
            def status = "PROCESSED"
            def postSeq = 1

            def mmInternalTransferSql = "insert into mm_internal_transfer (id,confirmation_number,note," +
            "posting_seq,status,recipient_id) values(?,?,?,?,?,?)"

            gsql.executeInsert(mmInternalTransferSql, [transferId, confirmNbr, note, postSeq, status, mmRecipId])

            println "Inserted into mm_internal_transfer"
        }
    }

    def lookupOrCreateRecipient(def userId, def toAccount) {
        def lookupMMRecipSql = "select id from mm_recipient where user_id = :user"
        def mmRecipId

        gsql.eachRow(lookupMMRecipSql, [user: userId]) {
            mmRecipId = it.id
        }

        if (mmRecipId == null) {
            java.sql.Date now = new java.sql.Date(System.currentTimeMillis())
            def mmRecSql = "insert into mm_recipient (created_ts,deleted,user_id) values(" +
                    "?,?,?)"

            def keys = gsql.executeInsert(mmRecSql, [now, 0, userId])
            if (keys != null) {
                mmRecipId = keys[0][0]

                // now we need to create mm_internal_account_recipient
                def mmIntRecSql = "insert into mm_internal_account_recipient (id,user_account_id) " +
                        "values(?,?)"

                gsql.executeInsert(mmIntRecSql, [mmRecipId, toAccount])

            }
        }

        // I think thats all we need to return as the id in mm_internal_account_recipient is same
        return mmRecipId
    }

    /**
     * This will get our random set of users which to add internal transfers on
     * @return list of users id aka primary key
     */
    def getSubsetOfUsers() {
        def randUserSql = properties.getProperty(Constants.internalXferUserSQL)

        def users = []
        gsql.eachRow(randUserSql) {
            users.add(it.id)
        }
        return users
    }

    /*
     * We are going to do a transfer from checking to savings
     */

    def getProductId(def name) {
        def sql = 'select id from account_product where source_product_id = :pname'
        return gsql.firstRow(sql, [pname: name]).id
    }

    def getFromToAccount(def userId) {
        assert userId != null: "Null UserId passed in"
        def fromToAcct = [:]
        def checking = getProductId(Checking)
        def savings = getProductId(Savings)


        if (checking == null || savings == null) {
            log.error "Could not find either checking or savings product id"
            System.exit(-1)
        }

        def fromAcct = getUserAccount(userId, checking)
        assert fromAcct.size() != 0: "No Data returned for From Account for user $userId"
        def toAcct = getUserAccount(userId, savings)
        assert toAcct.size() != 0: "No Data returned for To Account for user $userId"

        fromToAcct.put(FROM, fromAcct.get(0))
        fromToAcct.put(TO, toAcct.get(0))

        return fromToAcct
    }


    def getUserAccount(def userId, def productId) {
        def sql = " select ua.id from " +
                "user_account ua, d3_account d3a " +
                " where ua.user_id = :user " +
                " and ua.account_id = d3a.id " +
                " and d3a.source = 'INTERNAL' " +
                " and d3a.account_status = 'OPEN' " +
                " and d3a.account_product_id = :prodId" +
                " and d3a.balance >= 500 "

        def accounts = []

        gsql.eachRow(sql, [user: userId, prodId: productId]) {
            accounts.add(it.id)
        }

        return accounts

    }


}
