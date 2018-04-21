package com.d3.database;

import static com.d3.database.DatabaseUtils.DatabaseType.ORACLE;

import com.d3.exceptions.D3DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.CheckForNull;

/**
 * Util class that connects and queries the D3 Database
 */
@Slf4j
class DatabaseUtils implements AutoCloseable {

    private Connection conn;
    private DatabaseType dbType;
    private String host;
    private String nameOrService;
    private String user;
    private String password;
    private String port;
    private String schema;

    private static final String LOG_SQL_MSG = "Executing {} with params {}";

    /**
     * Connects to the configured database from System properties:
     *
     * dbType
     * dbHost
     * dbNameOService
     * dbUser
     * dbPassword
     * dbPort
     * dbSchema
     *
     * @throws D3DatabaseException on connection error
     */
    public DatabaseUtils() throws D3DatabaseException {
        dbType = DatabaseType.valueOf(System.getProperty("dbType").toUpperCase());
        host = System.getProperty("dbHost");
        nameOrService = System.getProperty("dbNameOrService");
        user = System.getProperty("dbUser");
        password = System.getProperty("dbPassword");
        port = System.getProperty("dbPort");
        schema = System.getProperty("dbSchema", "");
        connectToDatabase();
    }

    public Connection getConn() {
        return conn;
    }

    /**
     * The new environments separate the separate envs on the database by the schema. This sets the current session/connection to be under the correct
     * schema. The property to set in the connection property file is 'dbSchema'
     */
    private void setSchemaForOracle() throws SQLException {
        if (schema != null && !schema.isEmpty()) {
            @Language("SQL") String query = "ALTER SESSION SET CURRENT_SCHEMA= ?";
            executeUpdateQuery(query, schema);
        } else {
            log.error("Schema config is not set, {}", schema);
        }
    }

    /**
     * Connect to a database
     */
    private void connectToDatabase() throws D3DatabaseException {

        String url;

        switch (dbType) {
            case ORACLE:
                url = String.format("jdbc:oracle:thin:@//%s:%s/%s", host, port, nameOrService);
                log.info("Connecting to the Oracle database: {}", url);
                break;
            case MY_SQL:
                url = String.format("jdbc:mysql://%s%s", host, nameOrService);
                log.info("Connecting to the MySql database: {}", url);
                break;
            case SQL_SERVER:
                url = String.format("jdbc:jtds:sqlserver://%s:%s;DatabaseName=%s", host, port, nameOrService);
                log.info("Connecting to the SqlServer database, {}", url);
                break;
            default:
                throw new D3DatabaseException(String.format("%s is not supported yet", dbType));
        }
        log.info("URL used: {}", url);

        try {
            Class.forName(dbType.getDriverString());
            conn = DriverManager.getConnection(url, user, password);
            if (dbType == ORACLE) {
                setSchemaForOracle();
            }
        } catch (ClassNotFoundException e) {
            log.error("Error initializing SQL Driver: {}", dbType.getDriverString(), e);
            throw new D3DatabaseException("Fix the DriverName variable");
        } catch (SQLException e) {
            log.error("Error Connecting to SQL Driver: {} with url {}", dbType.getDriverString(), url, e);
            throw new D3DatabaseException("Database Error");
        }
    }

    /**
     * Execute an update query against the database
     *
     * @param updateQuery Query to run against the database
     * @param arguments arguments to give to the updateQuery via MessageFormat.format() If arguments are given, make sure you use two single quotes to
     * equal one single quote: (conn, "UPDATE test SET test_field = ''{0}''", true)
     * @throws SQLException when an issue running the sql query happens
     */
    public void executeUpdateQuery(@Language("SQL") String updateQuery, Object... arguments) throws SQLException {

        try (PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {
            for (int i = 0; i < arguments.length; ++i) {
                // parameter index is 1-indexed
                preparedStatement.setObject(i + 1, arguments[i]);
            }
            log.info(LOG_SQL_MSG, updateQuery, arguments);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Execute a select query and get all the data from one column
     * @param column Which column to get the data from
     * @param query The select query to run
     * @param arguments Any arguments to add to the query
     * @return A list of strings that hold the data from the column
     * @throws SQLException On Error
     */
    public List<String> getAllDataFromSelectQuery(String column, @Language("SQL") String query, Object... arguments) throws SQLException {
        List<String> queryResults = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < arguments.length; ++i) {
                // parameter index is 1-indexed
                stmt.setObject(i + 1, arguments[i]);
            }
            log.info(LOG_SQL_MSG, query, arguments);
            try (ResultSet set = stmt.executeQuery()) {
                while (set.next()) {
                    String result = set.getString(column);
                    queryResults.add(result);
                }
                return queryResults;
            }
        }
    }

    /**
     * Query the database with a select statement
     *
     * @param column Column name to get the data from
     * @param query The SQL statement to execute
     * @param arguments Any arguments to prepare the statement with
     * @return A String representation of the pulled column data
     * @throws SQLException On An error with the sql statement or connection
     */
    @CheckForNull
    public String getDataFromSelectQuery(String column, @Language("SQL") String query, Object... arguments) throws SQLException {

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            for (int i = 0; i < arguments.length; ++i) {
                // parameter index is 1-indexed
                preparedStatement.setObject(i + 1, arguments[i]);
            }

            log.info(LOG_SQL_MSG, query, arguments);
            try (ResultSet set = preparedStatement.executeQuery()) {
                set.next();
                return set.getString(column);
            }
        }
    }

    /**
     * Query the database with a select statement
     *
     * @param column Column name to get the data from
     * @param query The SQL statement to execute
     * @param arguments A List of arguments to prepare the statement with
     * @return A String representation of the pulled column data
     * @throws SQLException On An error with the sql statement or connection
     */
    @CheckForNull
    public String getDataFromSelectQuery(String column, @Language("SQL") String query, List<?> arguments) throws SQLException {

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            for (Object o : arguments) {
                preparedStatement.setObject(arguments.indexOf(o) + 1, o);
            }

            log.info(LOG_SQL_MSG, query, arguments);
            try (ResultSet set = preparedStatement.executeQuery()) {
                set.next();
                return set.getString(column);
            }
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (Exception e) {
            log.error("Error closing the db connection", e);
        }
    }

    /**
     * This holds the driver location for each of the types of databases D3 supports
     */
    public enum DatabaseType {
        ORACLE("oracle.jdbc.driver.OracleDriver"),
        SQL_SERVER("net.sourceforge.jtds.jdbc.Driver"),
        MY_SQL("com.mysql.jdbc.Driver");

        String driverString;

        DatabaseType(String driverString) {
            this.driverString = driverString;
        }

        public String getDriverString() {
            return driverString;
        }
    }
}
