package com.s3corp.dao;

import java.sql.ResultSet;

import org.apache.log4j.Logger;

public class LoginDAO {
	final static Logger LOGGER = Logger.getLogger(LoginDAO.class);
	public static String validate(String user, String pwd) {
		LOGGER.info("=============  validate login with hibernate ===========");
		String retVal = null;
		JdbcUtils jdbcUtils = new JdbcUtils();
		String sqlString = "SELECT user_name as username FROM user_profiles up WHERE up.user_name =? ";

		try {
			Object[] params = {user};
			ResultSet resultSet = jdbcUtils.select(sqlString, params);
			while(resultSet.next()) {
				retVal = resultSet.getString("username");
				LOGGER.info("while ... retVal : " + retVal);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("!!!!!!!!! validate jdbc.readData is error : " + e.getMessage());
			e.printStackTrace();
		}finally {
			jdbcUtils.close();
		}
		
		LOGGER.info("------ validate login with hibernate retVal : " + retVal);

		return retVal;
	}

}
