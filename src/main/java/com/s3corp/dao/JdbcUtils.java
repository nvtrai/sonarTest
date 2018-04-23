package com.s3corp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;

import com.s3corp.utils.Constances;

public class JdbcUtils {

	private static final Logger LOGGER = Logger.getLogger(JdbcUtils.class);
	
	private  String jdbcDriverStr;
	private  String jdbcURL;

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private PreparedStatement preparedStatement;

	public JdbcUtils(){}
	
	@Deprecated
	public ResultSet readData(String query) {
		try {
			LOGGER.info("=========> readData");
			
			connection = DriverManager.getConnection(jdbcURL,Constances.userNameDB,Constances.passDB);
			statement = connection.createStatement();
			LOGGER.info("=========> go to executeQuery");
			resultSet = statement.executeQuery(query);
			LOGGER.info("=========> go to return resultSet size :" + resultSet.getFetchSize());
			return resultSet;
		}catch (Exception e) {
		}
		return null;
	}

	public void executeUpdate(String query, Object[] params) throws SQLException {
		prepareStatementQuery(query, params);
		int retExe = preparedStatement.executeUpdate();
		if (retExe > 0) {
			LOGGER.info("=====execute query is ok : " + query);
		} else {
			LOGGER.error("=====execute query failed : " + query);
		}

		preparedStatement.close();
	}

	public ResultSet select(String query, Object[] params) throws SQLException {
		prepareStatementQuery(query, params);
		return preparedStatement.executeQuery();
	}

	private void prepareStatementQuery(String query, Object[] params) throws SQLException {
		
		getConnection();
			
		preparedStatement = connection.prepareStatement(query);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				Object ele = params[i];
				if (ele instanceof String) {
					preparedStatement.setString(i+1, (String) ele);
					continue;
				}
				if (ele instanceof Integer) {
					preparedStatement.setInt(i+1, (Integer) ele);
					continue;
				}
				if (ele instanceof Long) {
					preparedStatement.setLong(i+1, (Long) ele);
					continue;
				}
				if (ele instanceof Double) {
					preparedStatement.setDouble(i+1, (Double) ele);
					continue;
				}
				if (ele instanceof Boolean) {
					preparedStatement.setBoolean(i+1, (Boolean) ele);
					continue;
				}
				if (ele instanceof Date) {
					preparedStatement.setDate(i+1, new java.sql.Date(((Date)ele).getTime()));
					continue;
				}
			}
		}
	}

	private void getConnection() throws SQLException {
		connection = S3CorpDataSource.getInstance().getDataSource().getConnection();
		LOGGER.info("=========== OPENED JDBC Connection ===========");

		if (connection == null || connection.isClosed()) {
			connection = DriverManager.getConnection(Constances.MYSQL_URL,Constances.userNameDB,Constances.passDB);
			LOGGER.info("=========== OPENED JDBC Connection ===========");
		}
	}

	/**
	 * update to using S3CorpDataSource, no longer need to close connection by your hand
	 */
	@Deprecated
	public void close() {
		
		/*LOGGER.info("=========== CLOSING JDBC Connection ===========");
		try {
			if (resultSet != null)
				resultSet.close();
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		} catch (Exception e) {
			LOGGER.error("!!!!!!!! closing JDBC is error !!!!!!");
		}*/
	}

	public Boolean executeQuery(String query,Object[] params) {
		LOGGER.info("deleteInArray : " + query);
		try {
			
			prepareStatementQuery(query, params);
			
			preparedStatement.execute();
			return true;
		} catch (SQLException e) {
			LOGGER.error("deleteInArray were error : " + e.getMessage());
		}
		
		return false;
		
	}

}
