/**
 * Copyright D3banking
 * User: lpresswood
 * Date: 10/28/14
 * Time: 8:53 AM
 *
 */

log4j {
    appender.stdout = "org.apache.log4j.ConsoleAppender"
    appender."stdout.layout" ="org.apache.log4j.PatternLayout"
    appender.scrlog = "org.apache.log4j.FileAppender"
    appender."scrlog.layout"="org.apache.log4j.PatternLayout"
    appender."scrlog.layout.ConversionPattern"="%d %5p %c{1} - %m%n"
    appender."scrlog.file"="PerfLoader.log"
    rootLogger="debug,scrlog,stdout"
}