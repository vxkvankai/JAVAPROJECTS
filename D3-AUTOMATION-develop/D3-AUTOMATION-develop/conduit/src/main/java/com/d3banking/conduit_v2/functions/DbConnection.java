package com.d3banking.conduit_v2.functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class DbConnection extends ReadXMLData{

	
	
	
	/**
	 * Connect to DB
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public List<Object> connectToDB() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException, InterruptedException 
	{
		Connection conn = null;
		Statement stmt = null;
		List<Object> _sqlObjects = new ArrayList<Object>();
		String url = "jdbc:jtds:sqlserver://"+ getTestDataFor("urlDB").get(0) + ";DatabaseName="+ getTestDataFor("dbName").get(0) + "";
		String driverName = "net.sourceforge.jtds.jdbc.Driver";
		Class.forName(driverName);
		String userDB = getTestDataFor("userDB").get(0);
		String passwordDB = getTestDataFor("passwordDB").get(0);
		conn = DriverManager.getConnection(url, userDB, passwordDB);
		stmt = conn.createStatement();
		_sqlObjects.add(stmt);
		_sqlObjects.add(conn);
		Thread.sleep(1000);
		return _sqlObjects;
	}

	/**
	 * Close DB connection
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void closeDBConnection(List<Object> _sqlObjects) throws SQLException {
		Connection conn = (Connection) _sqlObjects.get(1);
		conn.close();
	}

	/**
	 * Run query and return first result
	 * 
	 * @param _stmt
	 * @param _sqlQuery
	 * @param _returnNoResult
	 * @param _noConnectionClose
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String runQuery(
			List<Object> _stmt, 
			String _sqlQuery,
			boolean _returnNoResult, 
			boolean _noConnectionClose)throws SQLException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException, InterruptedException 
			{
				ResultSet rs = null;
				String output = null;
				Statement stmt = (Statement) _stmt.get(0);
				if (_returnNoResult) {
					try {
						stmt.execute(_sqlQuery);
					} catch (NullPointerException e) {
					}
				} else {
					rs = stmt.executeQuery(_sqlQuery);
					while (rs.next()) {
						output = (rs.getString(1));
					}
				}
				if (!_noConnectionClose)
				{
					closeDBConnection(_stmt);
				}
				return output;
			}

	/**
	 * Run query and return list of results
	 * 
	 * @param _stmt
	 * @param _sqlQuery
	 * @param _returnNoResult
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public List<String> runQuery2(
			List<Object> _stmt, 
			String _sqlQuery,
			boolean _returnNoResult) throws SQLException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException, InterruptedException 
			{
				ResultSet rs = null;
				List<String> output = new ArrayList<String>();
				Statement stmt = (Statement) _stmt.get(0);
				if (_returnNoResult) {
					stmt.execute(_sqlQuery);
				} else {
					rs = stmt.executeQuery(_sqlQuery);
					while (rs.next()) {
						output.add(rs.getString(1));
					}
				}
				closeDBConnection(_stmt);
				return output;
			}
}
