import com.microsoft.sqlserver.jdbc.SQLServerDataSource


import java.sql.Connection


/**
 * D3Banking
 * User: lpresswood
 * Date: 10/23/14
 * Time: 2:07 PM
 *
 *
 */

class Constants {
  public static final INCOME_KEY = "INCOME"
  public static final EXP_KEY = "EXPENSE"

  public static final String sqlServerHost = "sqlServerHost"
  public static final String sqlServerDb = "sqlServerDb"
  public static final String sqlServerPwd = "sqlServerPwd"
  public static final String sqlServerUid = "sqlServerUid"
  public static final String sqlServerPort = "sqlServerPort"
  public static final String numberOfMonthsToLoad = "numberOfMonthsToLoad"
  public static final String numberOfTxPerDay = "numberOfTxPerDay"
  public static final String maxConcurrency = "maxConcurrency"
  public static final String logConfig = "logConfig"
  public static final String acctSelSql = "acctSelSql"
  public static final String totalInternalTransfersPerDay = "totalInternalTransfersPerDay"
  public static final String internalXferUserSQL = "internalXferUserSQL"

  public static final String txInsSql =  "insert into d3_transaction (created_ts,deleted,amount,offline," +
      "pending,post_date,source_tx_id,type,account_id,category_id,memo) " +
      "values (?,?,?,?,?,?,?,?,?,?,?)"

  public static String server
  public static String dbName
  public static String user
  public static String password
  public static int port


  public static Connection connectToDb() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName(server)
        ds.setDatabaseName(dbName)
        ds.setUser(user)
        ds.setPassword(password)
        ds.setPortNumber(port)


        Connection conn = ds.getConnection()
        if (conn != null) {
            println "Obtained Connection to MSSQL"
        } else {
            println "Unable to get Connection to MSSQL"
        }


        return conn

    }

    static Properties loadProps(String propFileName){
        Properties properties = new Properties()
        File propertiesFile = new File(propFileName)
        propertiesFile.withInputStream {
            properties.load(it)
        }

        server = properties.getProperty(sqlServerHost)
        dbName = properties.getProperty(sqlServerDb)
        user = properties.getProperty(sqlServerUid)
        password = properties.getProperty(sqlServerPwd)
        port = Integer.parseInt(properties.getProperty(sqlServerPort))

        return properties
    }

}
