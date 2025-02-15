package com.kbstar.itsm.exload.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kbstar.itsm.exload.jdbc.DataSource;
import com.kbstar.itsm.exload.model.CodeConfig;
import com.kbstar.itsm.exload.model.DBInfo;

public class TestCodeValueDAO {
	
	
	private CodeValueDAO dao;
	
	@BeforeEach
	public void setup() {
		DataSource.INSTANCE.initConfig(egeneDBInfo());
		dao = new CodeValueDAO();
		
	}
	
	
	@Test
	public void test_CodeData조회() throws SQLException {
		
		assertTrue(dao.selectMap(조회공통코드()).size()>0);

		assertNotNull(dao.selectMap(조회공통코드()).get(0).get("코드"));
		assertNotNull(dao.selectMap(조회공통코드()).get(0).get("값"));
	}

	
	@Test
	public void test_CodeData조건_조회() throws SQLException {
	
		assertTrue(dao.selectMap(조회공통코드_조건()).size()>0);
		
	}

	
	public CodeConfig 조회공통코드_조건() {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("select COD_ID 코드,COD_NAME 값 \n");
		sb.append("from ecf_code                \n");
		sb.append("where COD_CTY_ID = '$key' and 1 = $value   \n");
		sb.append("order by cod_order           \n");
		sb.append("WITH UR                      \n");
		

		return CodeConfig.builder()
				.name("테스트")
				.sql(sb.toString())
				.parameters("key:KBCMSTA,value:1")
				.build();
		
	}
	
	
	public CodeConfig 조회공통코드() {
			
			StringBuffer sb = new StringBuffer();
			
			sb.append("select COD_ID 코드, COD_NAME 값 \n");
			sb.append("from ecf_code                \n");
			sb.append("order by cod_order           \n");
			sb.append("WITH UR                      \n");
			
			System.out.println(sb.toString());
			return CodeConfig.builder()
					.name("테스트")
					.sql(sb.toString())
					.build();
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
