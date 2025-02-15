package com.kbstar.itsm.exload;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.kbstar.itsm.exload.ExcelLoadToolMain.VIEWS;
import com.kbstar.itsm.exload.controller.ExcelLoadToolMainModel;
import com.kbstar.itsm.exload.controller.IExcelLoadToolMainContoller;
import com.kbstar.itsm.exload.jdbc.DataSource;
import com.kbstar.itsm.exload.model.DBInfo;
import com.kbstar.itsm.exload.ui.IMenuBar;
import com.kbstar.itsm.exload.ui.IToolBar;

public class TestExceLoadToolMainModel{
	
	
	private IMenuBar menuBar = mock(IMenuBar.class);
	private IToolBar toolBar = mock(IToolBar.class);
	private IExcelLoadToolMain exceloadToolMain = mock(IExcelLoadToolMain.class);
		
	private IExcelLoadToolMainContoller model;
	
	@BeforeEach
	public void setUp() {
		model = new ExcelLoadToolMainModel();
		model.setMenuBar(menuBar);
		model.setToolBar(toolBar);
		model.setMainView(exceloadToolMain);
	}
	

	@Test
	public void test_메뉴바_초기화완료() {
		//then
		verify(menuBar).initialize(any());
	}
	

	
	@Test
	public void test_DBMS로그온팝업_닫은후실행() {
		
		//when
		model.connectComplete();
		
		//then
		verify(toolBar).setConnectInfo(any());
		verify(menuBar).showConnectionMenu();
	}
	
	
	
	@Test
	public void test_메뉴에서_연결종료() {
		// given
		
		DataSource.INSTANCE.initConfig(egeneDBInfo());
		
		// when
		model.clickCloseConnectionMenu();
		
		
		//then
		assertTrue(DataSource.INSTANCE.isClose());		

		verify(toolBar).setConnectInfo(any());
		
		ArgumentCaptor<VIEWS> view = ArgumentCaptor.forClass(VIEWS.class);
		verify(exceloadToolMain).changeContentView(view.capture());
		assertEquals(view.getValue().compareTo(VIEWS.CLEAR_VIEW), 0 );
	}
	
	
	@Test
	public void test_메뉴에서_코드설정() {
		
		//when
		model.changeViewPulbicCodeEnrollment();

		//then
		ArgumentCaptor<VIEWS> view = ArgumentCaptor.forClass(VIEWS.class);
		verify(exceloadToolMain).changeContentView(view.capture());
		assertEquals(view.getValue().compareTo(VIEWS.PUBLIC_CODE_VEIW), 0 );
		
	}

	
	@Test
	public void test_메뉴에서_익스포트엑셀양식() {
		
		//when
		model.changeViewRegisterExcelForm();

		//then
		ArgumentCaptor<VIEWS> view = ArgumentCaptor.forClass(VIEWS.class);
		verify(exceloadToolMain).changeContentView(view.capture());
		assertEquals(view.getValue().compareTo(VIEWS.REGISTER_EXCEL_EXPORT), 0 );
		
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
