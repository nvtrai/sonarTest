package com.s3corp.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

import com.s3corp.utils.Constances;

public class S3CorpDataSource {

	private static final Logger LOGGER = Logger.getLogger(S3CorpDataSource.class);

	private BasicDataSource dataSource;
	private S3CorpDataSource() {
	}

	private static S3CorpDataSource instance;

	public static synchronized S3CorpDataSource getInstance() {
		if (instance == null) {
			synchronized (S3CorpDataSource.class) {
				instance = new S3CorpDataSource();
			}
		}
		return instance;
	}

	public BasicDataSource getDataSource() {
		if (dataSource == null) {
			try {
				Class.forName(Constances.MYSQL_DRIVER);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			BasicDataSource ds = new BasicDataSource();
			ds.setUrl(Constances.MYSQL_URL);
			ds.setUsername(Constances.userNameDB);
			ds.setPassword(Constances.passDB);

			ds.setMinIdle(5);
			ds.setMaxIdle(10);
			ds.setMaxOpenPreparedStatements(100);
			dataSource = ds;
		}
		return dataSource;
	}

}
