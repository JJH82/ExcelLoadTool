package com.kbstar.itsm.exload.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import com.kbstar.itsm.exload.model.DBInfo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariConfigMXBean;
import com.zaxxer.hikari.HikariDataSource;

public enum DataSource  {
	
	INSTANCE;
	

	private HikariDataSource datasource = new HikariDataSource();
	private DBInfo dbInfo = DBInfo.builder().build();
	
	DataSource() {}

	public void initConfig(DBInfo dbInfo) {

		this.dbInfo = dbInfo;
		datasource = new HikariDataSource();

		datasource.setJdbcUrl(dbInfo.getJdbcUrl());
		datasource.setUsername(dbInfo.getUserName());
		datasource.setPassword(dbInfo.getPassword());
		
				
	}
	
	public DBInfo getDBInfo() {
		return dbInfo;
	}

	
	public boolean testConnect(DBInfo dbInfo) throws Exception{

		boolean result;
		HikariDataSource testSource = new HikariDataSource();
		
		testSource.setJdbcUrl(dbInfo.getJdbcUrl());
		testSource.setUsername(dbInfo.getUserName());
		testSource.setPassword(dbInfo.getPassword());
		
		result = testSource.getConnection().isValid(3000);
		testSource.close(); 
		
		return result;
	}

	
	public Connection getConnection() throws SQLException {
		return datasource.getConnection();
	}
	
	public void close() throws SQLException {
		dbInfo = DBInfo.builder().build();
		datasource.close();
	}
	
	public boolean isClose() {
		return datasource.isClosed();
	}
	
	


}
