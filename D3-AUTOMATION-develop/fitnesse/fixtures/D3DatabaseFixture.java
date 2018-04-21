package com.d3.utils;

import fit.ColumnFixture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class D3DatabaseFixture extends ColumnFixture {


    private static String dbHost;
    private static String dbUser;
    private static String dbPassword;
    private static String dbName;
    private static String oracleUpdateQuery;
    private static String sqlServerUpdateQuery;
    private static String oracleSelectQuery;
    private static String sqlServerSelectQuery;

    static String parameter1 = "";
    static String parameter2 = "";
    static String parameter3 = "";

    private static String newOracleSelectQuery;
    private static String newSqlServerSelectQuery;
    private static String newSqlServerUpdateQuery;
    private static String newOracleUpdateQuery;
    private static ResultSet resultSet;

    private static Connection conn;
    private static Statement stmt;

    private static String whichDbToConnectTo;


    public String connectToDatabase() throws ClassNotFoundException, SQLException {

        String driverName;
        String url;
        // TODO JMoravec: probably should put this somewhere else
        Logger logger = LoggerFactory.getLogger(getClass().getName());

        switch (whichDbToConnectTo) {
            case "Oracle":
                url = "jdbc:oracle:thin:@" + dbHost;
                driverName = "oracle.jdbc.driver.OracleDriver";
                Class.forName(driverName);
                conn = DriverManager.getConnection(url, dbUser, dbPassword);
                logger.debug("Connected to the Oracle database");
                break;
            case "SqlServer":
                url = "jdbc:jtds:sqlserver://" + dbHost + ";DatabaseName=" + dbName;
                driverName = "net.sourceforge.jtds.jdbc.Driver";
                Class.forName(driverName);
                conn = DriverManager.getConnection(url, dbUser, dbPassword);
                logger.debug("Connected to the SqlServer database");
                break;
            default:
                url = "jdbc:jtds:sqlserver://" + dbHost + ";DatabaseName=" + dbName;
                driverName = "net.sourceforge.jtds.jdbc.Driver";
                Class.forName(driverName);
                conn = DriverManager.getConnection(url, dbUser, dbPassword);
                logger.debug("Connected to the SqlServer database");
                break;
        }
        return "Connected to " + whichDbToConnectTo + " Database";
    }


    public int executeUpdateQuery() throws SQLException {
        // TODO JMoravec: probably should put this somewhere else
        Logger logger = LoggerFactory.getLogger(getClass().getName());

        stmt = conn.createStatement();
        int numberOfRowsAffected = 0;
        switch (whichDbToConnectTo) {
            case "Oracle":
                newOracleUpdateQuery = oracleUpdateQuery;
                if (!parameter1.equals("")) {
                    newOracleUpdateQuery = oracleUpdateQuery.replace("XXX", parameter1);
                    logger.debug("Query being run: {}", newOracleUpdateQuery);
                }
                if (!parameter2.equals("")) {
                    newOracleUpdateQuery = newOracleUpdateQuery.replace("YYY", parameter2);
                    logger.debug("Query being run: {}", newOracleUpdateQuery);
                }
                if (!parameter3.equals("")) {
                    newOracleUpdateQuery = newOracleUpdateQuery.replace("ZZZ", parameter3);
                }
                logger.debug("Query being run: {}", newOracleUpdateQuery);
                numberOfRowsAffected = stmt.executeUpdate(newOracleUpdateQuery);
                break;
            case "SqlServer":
                newSqlServerUpdateQuery = sqlServerUpdateQuery;
                if (!parameter1.equals("")) {
                    newSqlServerUpdateQuery = sqlServerUpdateQuery.replace("XXX", parameter1);
                }
                if (!parameter2.equals("")) {
                    newSqlServerUpdateQuery = newSqlServerUpdateQuery.replace("YYY", parameter2);
                }
                if (!parameter3.equals("")) {
                    newSqlServerUpdateQuery = newSqlServerUpdateQuery.replace("ZZZ", parameter3);
                }
                numberOfRowsAffected = stmt.executeUpdate(newSqlServerUpdateQuery);
                break;
        }

        logger.debug("Number of rows updated --> {}", numberOfRowsAffected);
        return numberOfRowsAffected;

    }

    public String executeSelectQuery() throws SQLException, ClassNotFoundException {
        // TODO JMoravec: probably should put this somewhere else
        Logger logger = LoggerFactory.getLogger(getClass().getName());
        stmt = conn.createStatement();

        switch (whichDbToConnectTo) {
            case "Oracle":
                newOracleSelectQuery = oracleSelectQuery;
                if (!parameter1.equals("")) {
                    newOracleSelectQuery = oracleSelectQuery.replace("XXX", parameter1);
                }
                if (!parameter2.equals("")) {
                    newOracleSelectQuery = newOracleSelectQuery.replace("YYY", parameter2);
                }
                if (!parameter3.equals("")) {
                    newOracleSelectQuery = newOracleSelectQuery.replace("ZZZ", parameter3);
                }
                logger.debug("Query being run: {}", newOracleSelectQuery);
                resultSet = stmt.executeQuery(newOracleSelectQuery);
                break;
            case "SqlServer":
                newSqlServerSelectQuery = sqlServerSelectQuery;
                if (!parameter1.equals("")) {
                    newSqlServerSelectQuery = sqlServerSelectQuery.replace("XXX", parameter1);
                }
                if (!parameter2.equals("")) {
                    newSqlServerSelectQuery = newSqlServerSelectQuery.replace("YYY", parameter2);
                }
                if (!parameter3.equals("")) {
                    newSqlServerSelectQuery = newSqlServerSelectQuery.replace("ZZZ", parameter3);
                }
                resultSet = stmt.executeQuery(newSqlServerSelectQuery);
                break;
        }

        resultSet.next();
        return resultSet.getString(1);
    }


    public String closeDatabaseConnection() throws SQLException {
        conn.close();
        return "Database connection closed";

    }


}


