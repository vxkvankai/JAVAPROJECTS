import groovy.sql.Sql
import groovy.time.TimeCategory

import java.sql.Connection

/**
 * Copyright D3Banking
 * User: lpresswood
 * Date: 10/30/14
 * Time: 10:17 AM
 * 
 */


Properties properties = Constants.loadProps('/Users/lpresswood/Projects/D3/D3-AUTOMATION/perfload/src/PerfLoader.config')

Sql gsql = createGSql()

InternalTransferLoader transferLoader = new InternalTransferLoader(gsql)

def checking = transferLoader.getProductId(InternalTransferLoader.Checking)
def savings = transferLoader.getProductId(InternalTransferLoader.Savings)

assert checking != null : "Should have found checking Acct"
assert savings != null : "Should have found savings Acct"

assert checking == 31
assert savings == 33

println "finished"

def userId = 1

def toFromAcct = transferLoader.getFromToAccount(userId)

assert toFromAcct != null

assert toFromAcct.get(InternalTransferLoader.FROM) != null
assert toFromAcct.get(InternalTransferLoader.TO) != null


def recipId = transferLoader.lookupOrCreateRecipient(userId,toFromAcct.get(InternalTransferLoader.TO))

assert recipId != null : "Unable to find Recip Id"

def today = new Date()

use(TimeCategory) {
    today = today - 1.day
    today.clearTime()
    println today
}

transferLoader.createTransfer(userId,toFromAcct,new BigDecimal(1),new java.sql.Date(today.getTime()))


def createGSql(){
    // get accounts etc
    Connection conn = Constants.connectToDb()
    return Sql.newInstance(conn)

}