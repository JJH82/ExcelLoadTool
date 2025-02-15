package com.kbstar.itsm.exload.jdbc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.kbstar.itsm.exload.model.DBInfo;

public class TestDataSource {
	

	
	@Test
	public void testDb연결테스트() throws Exception {
		
		assertTrue(DataSource.INSTANCE.testConnect(renobitDBInfo()));
	}

	
	@Test
	public void testDb연결테스트2() throws Exception {
		
		assertTrue(DataSource.INSTANCE.testConnect(egeneDBInfo()));
	}

	
	
	
	@Test
	public void testDB설정값설정_후_연결() throws SQLException {
		
		DataSource.INSTANCE.initConfig(renobitDBInfo());
		assertTrue(DataSource.INSTANCE.getConnection().isValid(3000));
	}
	
	public DBInfo renobitDBInfo() {
		
		return DBInfo.builder()
		.connectName("renobit DB")
		.jdbcUrl("jdbc:db2://nitdbdm1:26500/DSNIMT")
		.userName("renobit")
		.password("itdb$E01")
		.build();
	}
	
	
	public DBInfo egeneDBInfo() {
		
		return DBInfo.builder()
		.connectName("egene DB")
		.jdbcUrl("jdbc:db2://nitdbd01:26500/DSNITT")
		.userName("egene")
		.password("itdb$E01")
		.build();
	}
	

}
