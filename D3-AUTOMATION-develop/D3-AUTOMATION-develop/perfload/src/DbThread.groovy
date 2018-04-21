import groovy.sql.Sql
import groovy.util.logging.Log4j

import java.sql.Connection
import java.sql.PreparedStatement

/**
 * Copyright D3Banking
 * User: lpresswood
 * Date: 10/27/14
 * Time: 3:34 PM
 *
 */
@Log4j
class DbThread extends Thread {
    Connection connection
    Sql gsql
    PreparedStatement txStatement

    public DbThread(Runnable r){
        super(r)
        connection = Constants.connectToDb()
        gsql = Sql.newInstance(connection)
        txStatement = connection.prepareStatement(Constants.txInsSql)
    }

    @Override
    public void run(){
        super.run()
    }

}
