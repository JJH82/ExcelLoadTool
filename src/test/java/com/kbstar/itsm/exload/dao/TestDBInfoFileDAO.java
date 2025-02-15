package com.kbstar.itsm.exload.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Vector;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kbstar.itsm.exload.dao.DBInfoFileDAO;
import com.kbstar.itsm.exload.model.DBInfo;

public class TestDBInfoFileDAO {
	
	private DBInfoFileDAO dao = new DBInfoFileDAO();
	
	private Vector<DBInfo> dbInfoList;
	
	@BeforeEach
	public void setUp() {
		
		dbInfoList_3개생성();
	}
	
	@AfterEach
	public void afterAll() {
		
		new File(System.getProperties().get("user.dir").toString()+File.separator + "conf"+File.separator+dao.configFileName() ).delete();
	}
	

	@Test
	public void DBInfo저장하기() {
		assertTrue(dao.saveAll(dbInfoList));
	}
	
	@Test
	public void DBInfo불러오기() {
		assertTrue(dao.saveAll(dbInfoList));
		assertEquals(dbInfoList.size(), dao.loadAll().size());
	}

	
	
	private void dbInfoList_3개생성() {
		dbInfoList = new Vector<DBInfo>();
		dbInfoList.add(
				DBInfo.builder()
				.connectName("테스트1")
				.jdbcUrl("jdbc Url1")
				.id("egene")
				.password("test")
				.build()
				
				);
		
		dbInfoList.add(
				DBInfo.builder()
				.connectName("테스트2")
				.jdbcUrl("jdbc Url")
				.id("renobit")
				.password("test")
				.build()
				
				);

		dbInfoList.add(
				DBInfo.builder()
				.connectName("테스트3")
				.jdbcUrl("jdbc Url")
				.id("sqe000")
				.password("test")
				.build()
				
				);
	}


}
