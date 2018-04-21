/**
 * D3Banking
 * User: lpresswood
 * Date: 10/23/14
 * Time: 10:29 AM
 *
 * Notes/Assumptions
 *
 * 1 income every 14 days
 * Total of between 5-10 transactions per day
 *
 *
 */


import groovy.time.TimeCategory
import groovy.sql.Sql



import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.log4j.PropertyConfigurator

import java.sql.*


import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

String propFileName = null

CliBuilder cli = new CliBuilder(usage: 'PerfLoader -h -p propertyFile ')
cli.h(longOpt: 'help', 'usage information')
cli.p(argName: 'propertyFile', longOpt: 'propFile', args: 1, required: true, type: GString, 'propertyFile')
cli.i(argName: 'internalTransfer', longOpt: 'internalXfer', args: 1, required: false, type: GString, 'internalTransfer')

boolean loadInternalXfers=false

def opt = cli.parse(args)
if (!opt) {
    println "Unable to parse arguments"
    System.exit(-1)
}

if (opt.h) {
    cli.usage()
    System.exit(0)
}

if (opt.p) {
    propFileName = opt.p
}

if ( opt.i ){
  loadInternalXfers = true
}

Properties properties = Constants.loadProps(propFileName)

String logConfigFile = properties.getProperty(Constants.logConfig)
def config = new ConfigSlurper().parse(new File(logConfigFile).toURL())
PropertyConfigurator.configure(config.toProperties())
Logger log = Logger.getInstance(getClass())

log.level = Level.INFO

log.info "Starting Transaction Load"



Sql sql = createGSql()
def daysForTx = calcDateRange(Integer.parseInt(properties.getProperty(Constants.numberOfMonthsToLoad)))



if ( loadInternalXfers ){
    // most likely will need to be done in parallel
    int totalTransfersToLoad = Integer.parseInt(properties.getProperty(Constants.totalInternalTransfersPerDay))
    // load internal Transfers does not need to be extreme as only 12k tx per day
    InternalTransferLoader transferLoader = new InternalTransferLoader(sql,totalTransfersToLoad,daysForTx,properties)
    transferLoader.loadTransfers()

    sql.close()
} else {

    // creates our thread factory for our ThreadPoolExecutor so we create threads that have connections pinned to them
    SqlThreadFactory threadFactory = new SqlThreadFactory()
    // get our select sql to figure out which accounts to pull
    def accounts = getAccounts(sql, properties.getProperty(Constants.acctSelSql))
    log.info "There were " + accounts.size() + " Accounts Selected to have Transactions loaded "
    def txCategories = getUncategorizedCategories(sql)
    int concurrency = Integer.parseInt(properties.getProperty(Constants.maxConcurrency))
    ThreadPoolExecutor loaderExecutor = new ThreadPoolExecutor(concurrency, concurrency + 1, 1000,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory)
    int nbrOfTxPerDay = Integer.parseInt(properties.getProperty(Constants.numberOfTxPerDay))

    addTransaction(accounts, daysForTx, txCategories,
            nbrOfTxPerDay, loaderExecutor)

    // Signal Pool to shutdown
    loaderExecutor.shutdown()
    // Wait until all submitted jobs are finished
    try {
        while (!loaderExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
            log.info "Awaiting Completion of Data Load"
        }
    } catch (Exception e) {
        e.printStackTrace()
    }

    log.info "Finished Transaction Load"

    // Close out our GSQL
    sql.close()

}



def createGSql(){
    // get accounts etc
    Connection conn = Constants.connectToDb()
    return Sql.newInstance(conn)

}

// we most likely will need to do this in parallel
def addTransaction(def accounts, def daysForTx,
                   def txCategories, def txPerDay,
                   ThreadPoolExecutor loaderExecutor) {
    for (int accountId : accounts) {   // 300k
        def dateCtr = 0
        def txForAccount = []
        for (java.util.Date dt : daysForTx) {
            dateCtr++

            D3Transaction transaction
            if (dateCtr % 14 == 0) {
                // going to add income tx
                transaction = new D3Transaction(new BigDecimal(1000),
                        accountId, txCategories.get(Constants.INCOME_KEY))
                txForAccount.add(transaction)
            }

            for (int txCtr = 0; txCtr < txPerDay; txCtr++) {
                transaction = new D3Transaction(new BigDecimal(50),
                        accountId, txCategories.get(Constants.EXP_KEY))
                txForAccount.add transaction
            }
        }

        // At this point we are going to want to submit our job
        loaderExecutor.submit(new SqlLoader(txForAccount))
    }


}

/**
 * This will get an array of dates which we need to use to load 10 tx for each
 * day for each account
 * @return list of dates
 */
def calcDateRange(def numberOfMonthsToLoad) {
    def daysForTx = []
    def today = new java.util.Date()

    use(TimeCategory) {
        //today = today - 1.day
        startDate = today - numberOfMonthsToLoad.month
        duration = today - startDate
        for (int ctr = 1; ctr <= duration.days; ctr++) {
            def day = today - ctr.day
            day.clearTime()
            daysForTx.add(day)
        }
    }

    return daysForTx

}

def getAccounts(def gsql, String sql) {
    accounts = []
    gsql.eachRow(sql) {
        accounts.add(it.id)
    }
    return accounts
}

def getUncategorizedCategories(def gsql) {
    def incSql = "select id from category where category_group = 'Uncategorized Income'"
    def expSql = "select id from category where category_group = 'Uncategorized Expense'"

    def incomeCategoryIds = [:]

    gsql.eachRow(incSql) {
        incomeCategoryIds.put(Constants.INCOME_KEY, it.id)
    }

    gsql.eachRow(expSql) {
        incomeCategoryIds.put(Constants.EXP_KEY, it.id)
    }

    return incomeCategoryIds
}









