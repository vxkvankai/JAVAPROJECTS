
import groovy.util.logging.Log4j

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Copyright D3Banking
 * User: lpresswood
 * Date: 10/27/14
 * Time: 2:56 PM
 *
 */
@Log4j
public class SqlLoader implements Runnable {

    def listOfTx

    public SqlLoader(def listOfTx){
        this.listOfTx = listOfTx
    }

    @Override
    public void run(){
        // assume this will worker
        DbThread dbThread = (DbThread) Thread.currentThread()

        try {
            insertIntoD3TxBatch(dbThread.txStatement)
        }catch(SQLException se){
            println se.toString()
        }

    }

    // use normal jdbc batching groovy withbatch and preparedstatements dont seem to be as fast
    def insertIntoD3TxBatch(PreparedStatement stmt) {

        for (D3Transaction transaction : listOfTx) {
            java.sql.Date createTs = transaction.createTs
            short deleted = transaction.deleted
            BigDecimal amount = transaction.amount
            short offline = transaction.offline
            short pending = transaction.pending
            String sourceTx = transaction.sourceTxId
            long accountId = transaction.accountId
            long categoryId = transaction.categoryId
            int type = transaction.type

            stmt.setDate(1,createTs)
            stmt.setShort(2,deleted)
            stmt.setBigDecimal(3,amount)
            stmt.setShort(4,offline)
            stmt.setShort(5,pending)
            stmt.setDate(6,createTs) // update ts
            stmt.setString(7,sourceTx)
            stmt.setInt(8,type)
            stmt.setLong(9,accountId)
            stmt.setLong(10,categoryId)
            stmt.setString(11,transaction.memo)

            stmt.addBatch()
        }

        //log.info "Committing Batch of " + listOfTx.size() + " Transactions"
        stmt.executeBatch()

    }

}
