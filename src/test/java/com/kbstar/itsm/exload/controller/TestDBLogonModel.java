package com.kbstar.itsm.exload.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kbstar.itsm.exload.IExcelLoadToolMain;
import com.kbstar.itsm.exload.dao.GenericVectorDAO;
import com.kbstar.itsm.exload.jdbc.DataSource;
import com.kbstar.itsm.exload.model.DBInfo;
import com.kbstar.itsm.exload.ui.IDBLogonPopup;
import com.kbstar.itsm.exload.ui.IMenuBar;
import com.kbstar.itsm.exload.ui.IToolBar;

public class TestDBLogonModel implements IDBLogonPopup, IExcelLoadToolMainContoller,GenericVectorDAO<DBInfo> {
	
	private DBLogonPopModel model;
	private Vector<DBInfo> dbInfoList;
	private Vector<DBInfo> savedbInfoList;
	private DBInfo dbInfoUI;
	private int clearDBInfoUICount;
	private Optional<DBInfo> o_selectedDefaultConnect;
	private int updateUICount;
	

	
	private int m_close;
	private int m_alertError;
	private int m_connectComplete;
	
	 
	
	@BeforeEach
	public void init() {
		
		
	}
	
	
	@Test
	public void test저장된연결명_선택변경시_화면값변경() {

		dbInfoList_3개생성();
		model = new DBLogonPopModel(this);
		model.setView(this);
		model.setMainCtrl(this);
		
		model.selectionChange(dbInfoList.get(0));
		assertEquals(dbInfoList.get(0), dbInfoUI);

	}

	

	@Test
	public void test저장된연결명_미선택시_삭제() {
		
		dbInfoList_3개생성();
		model = new DBLogonPopModel(this);
		model.setView(this);
		model.setMainCtrl(this);
		model.remove();
		
		assertEquals(3, dbInfoList.size());
		assertEquals(1, clearDBInfoUICount);	
		assertEquals(1, updateUICount);
	}

	
	@Test
	public void test저장된연결명_선택시_삭제() {
		
		dbInfoList_3개생성();
		model = new DBLogonPopModel(this);
		model.setView(this);
		model.setMainCtrl(this);		
		model.selectionChange(dbInfoList.get(0));
		model.remove();
		
		assertEquals(2, dbInfoList.size());
		assertEquals(1, clearDBInfoUICount);
		assertEquals(1, updateUICount);
	}
	

	@Test
	public void test선택된저장된연결명_수정된저장된명이다른경우() {
		
		dbInfoList_3개생성();
		model = new DBLogonPopModel(this);
		model.setView(this);
		model.setMainCtrl(this);
		model.selectionChange(dbInfoList.get(0));
		
		
		model.save(DBInfo.builder()
					.id(UUID.randomUUID().toString())
					.connectName("새로운값저장")
					.jdbcUrl("새로운jdbcurl")
					.id("아이디")
					.password("패스워드")
					.build()
				);
		
		
		assertEquals(4, savedbInfoList.size());
		assertEquals("새로운값저장",savedbInfoList.get(3).getConnectName());
		assertEquals(1, clearDBInfoUICount);
		assertEquals(1, updateUICount);
		
	}
	
	
	@Test
	public void test저장된연결명_미선택시_저장() {
		
		dbInfoList_3개생성();
		model = new DBLogonPopModel(this);
		model.setMainCtrl(this);
		model.setView(this);
		
		model.save(DBInfo.builder()
					.id(UUID.randomUUID().toString())
					.connectName("새로운값저장")
					.jdbcUrl("새로운jdbcurl")
					.id("아이디")
					.password("패스워드")
					.build()
				);
		
		assertEquals(4, savedbInfoList.size());
		assertEquals("새로운값저장",savedbInfoList.get(3).getConnectName());
		assertEquals(1, updateUICount);
		
	}

	
	@Test
	public void test저장된연결명_선택시_저장() {
		
		dbInfoList_3개생성();
		model = new DBLogonPopModel(this);
		model.setMainCtrl(this);
		model.setView(this);
		
		model.selectionChange(dbInfoList.get(0));
		
		model.save(DBInfo.builder()
				.id(UUID.randomUUID().toString())
				.connectName("테스트1")
				.jdbcUrl("새로운jdbcurl")
				.id("아이디")
				.password("패스워드")
				.build()
			);
		
		assertEquals(3, savedbInfoList.size());
		assertEquals("테스트1", savedbInfoList.get(0).getConnectName());
	}


	
	@Test
	public void test저장된연결명_선택시_기본연결설정_하나만_선택() {
		
		dbInfoList_3개생성_및_두번째값기본연결로설정();
		model = new DBLogonPopModel(this);
		model.setMainCtrl(this);
		model.setView(this);
		
		model.selectionChange(dbInfoList.get(0));
		dbInfoList.get(1).setDefaultConnectSetting(true);
		
		model.save(DBInfo.builder()
				.id(UUID.randomUUID().toString())
				.connectName("테스트1")
				.jdbcUrl("새로운jdbcurl")
				.id("아이디")
				.password("패스워드")
				.defaultConnectSetting(true)
				.build()
			);
		
		
		assertTrue(savedbInfoList.get(0).isDefaultConnectSetting());
		assertFalse(savedbInfoList.get(1).isDefaultConnectSetting());		
		assertFalse(savedbInfoList.get(2).isDefaultConnectSetting());
		
	}
	
	@Test
	public void test저장된연결명_미선택시_기본연결설정_하나만_선택() {
		
		dbInfoList_3개생성_및_두번째값기본연결로설정();
		model = new DBLogonPopModel(this);
		model.setMainCtrl(this);
		model.setView(this);
		
		
		model.save(DBInfo.builder()
				.id(UUID.randomUUID().toString())
				.connectName("새로운값저장")
				.jdbcUrl("새로운jdbcurl")
				.id("아이디")
				.password("패스워드")
				.defaultConnectSetting(true)
				.build()
			);
		
		
		assertTrue(savedbInfoList.get(3).isDefaultConnectSetting());
		assertFalse(savedbInfoList.get(1).isDefaultConnectSetting());		
		assertFalse(savedbInfoList.get(2).isDefaultConnectSetting());
	}

	
	
	@Test
	public void test로드시_두번째값_기본연결설정() {
		
		dbInfoList_3개생성_및_두번째값기본연결로설정();
		model = new DBLogonPopModel(this);
		model.setMainCtrl(this);
		model.setView(this);
		
		
		assertEquals(dbInfoList.get(1), this.o_selectedDefaultConnect.get() );
	}
	
	
	@Test
	public void test로드시_기본연결설정_값없음() {
		
		dbInfoList_3개생성();
		model = new DBLogonPopModel(this);
		model.setView(this);
	
		assertFalse(o_selectedDefaultConnect.isPresent());
	}
	
	
	
	@Test
	public void test로드시_기본연결설정_값두번째값() {
		
		dbInfoList_3개생성_및_두번째값기본연결로설정();
		model = new DBLogonPopModel(this);
		model.setView(this);
	
		assertTrue(o_selectedDefaultConnect.isPresent());
		assertEquals(dbInfoList.get(1),o_selectedDefaultConnect.get() );
	}
	

	
	
	
	@Test
	public void test_연결테스트버튼클릭시_연결완료시() throws SQLException {
		
		
		dbInfoList_3개생성_및_두번째값기본연결로설정();
		model = new DBLogonPopModel(this);
		model.setView(this);
		model.setMainCtrl(this);
		
		DBInfo dbinfo = o_selectedDefaultConnect.get();
		model.selectionChange(dbinfo);
		
		model.connection();

		
	//	assertTrue( DataSource.INSTANCE.getConnection().isValid(3000));
		assertEquals(dbinfo.getConnectName(), DataSource.INSTANCE.getDBInfo().getConnectName() );
		
		assertEquals(1, m_connectComplete);
		assertEquals(1, m_close);
		
	}
	
	

	
	@Test
	public void test_로드시기본연결이없고연결버튼클릭시() throws SQLException {
		
		dbInfoList_3개생성();
		model = new DBLogonPopModel(this);
		model.setView(this);
		
		model.connection();
		
		assertFalse(o_selectedDefaultConnect.isPresent());
		assertEquals(0, m_alertError);
		assertEquals(0, m_close);		
	}	
	
	
	
	
	private void dbInfoList_3개생성() {
		dbInfoList = new Vector<DBInfo>();
		dbInfoList.add(
				DBInfo.builder()
				.connectName("테스트1")
				.jdbcUrl("jdbc Url1")
				.userName("egene")
				.password("test")
				.build()
				
				);
		
		dbInfoList.add(
				DBInfo.builder()
				.connectName("테스트2")
				.jdbcUrl("jdbc Url")
				.userName("renobit")
				.password("test")
				.build()
				
				);

		dbInfoList.add(
				DBInfo.builder()
				.connectName("테스트3")
				.jdbcUrl("jdbc Url")
				.userName("sqe000")
				.password("test")
				.build()
				
				);
	}
	
	
	private void dbInfoList_3개생성_및_두번째값기본연결로설정() {
		dbInfoList = new Vector<DBInfo>();
		dbInfoList.add(
				DBInfo.builder()
				.connectName("테스트1")
				.jdbcUrl("jdbc Url1")
				.userName("egene")
				.password("test")
				.build()
				
				);
		
		dbInfoList.add(
				DBInfo.builder()
				.connectName("테스트2")
				.jdbcUrl("jdbc:db2://nitdbdm1:26500/DSNIMT")
				.userName("renobit")
				.password("itdb$E01")
				.defaultConnectSetting(true)
				.build()
				
				);

		dbInfoList.add(
				DBInfo.builder()
				.connectName("테스트3")
				.jdbcUrl("jdbc Url")
				.userName("sqe000")
				.password("test")
				.build()
				
				);
	}
	
	
	
	





	



	@Override
	public boolean saveAll(Vector<DBInfo> info) {
		savedbInfoList = info;
		return true;
	}





	@Override
	public Vector<DBInfo> loadAll() {
		return dbInfoList;
	}





	@Override
	public void selectedDefaultConnect(Optional<DBInfo> dbInfo) {
		this.o_selectedDefaultConnect = dbInfo;
		
	}










	@Override
	public void setDBInfoList(Vector<DBInfo> list) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void clearDBInfoUI() {
		clearDBInfoUICount++;
		
	}





	@Override
	public void setDBInfoUI(DBInfo dbInfo) {
		dbInfoUI = dbInfo;
		
	}



	@Override
	public void updateUI() {
		updateUICount++;
		
	}



	@Override
	public void show(Component c) {
		
		
	}


	@Override
	public void closePopup() {
		m_close++;
		
	}







	@Override
	public void showDBLogonPopup() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void connectComplete() {
		m_connectComplete++;
		
	}



	@Override
	public void clickCloseConnectionMenu() {
		// TODO Auto-generated method stub
		
	}



	



	@Override
	public void setToolBar(IToolBar toolBar) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setMenuBar(IMenuBar menuBar) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void changeViewPulbicCodeEnrollment() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setMainView(IExcelLoadToolMain main) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void changeViewRegisterExcelForm() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void changeViewLoaderExcelForm() {
		// TODO Auto-generated method stub
		
	}



	


}
