
import groovy.util.logging.Log4j

import java.util.concurrent.ThreadFactory

/**
 * Copyright D3Banking
 * User: lpresswood
 * Date: 10/27/14
 * Time: 3:26 PM
 *
 */
@Log4j
class SqlThreadFactory implements ThreadFactory {


    SqlThreadFactory(){
    }

    @Override
    Thread newThread(Runnable r) {
         return new DbThread(r)
    }


}
