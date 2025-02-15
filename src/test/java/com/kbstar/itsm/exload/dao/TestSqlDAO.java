package com.kbstar.itsm.exload.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kbstar.itsm.exload.jdbc.DataSource;
import com.kbstar.itsm.exload.model.DBInfo;

public class TestSqlDAO {
	
	
	private SqlDAO dao;
	
	@BeforeEach
	public void setup() {
		DataSource.INSTANCE.initConfig(egeneDBInfo());
		dao = new SqlDAO();
		
	}
	
	
	@Test
	public void test_CodeData조회() throws SQLException {
		assertTrue(dao.selectColumnLable(조회_조건()).size()>0);
		assertNotNull(dao.selectColumnLable(조회_조건()).get(0));
	}
	
	
	@Test
	public void test_Data조회() throws SQLException {
		assertTrue(dao.selectMap(조회_조건()).size()==1);

	}

	
	

	
	public String 조회_조건() {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("select CM_ID 구성ID         \n");
		sb.append("      ,CM_NAME 구성항목명   \n");
		sb.append("from egene.eso_cm           \n");
		sb.append("where rownum = 1           \n");
		sb.append("with ur                  \n");

		return sb.toString();
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
