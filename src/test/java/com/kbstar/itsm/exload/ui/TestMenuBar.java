package com.kbstar.itsm.exload.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.swing.JMenuItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kbstar.itsm.exload.controller.IExcelLoadToolMainContoller;
import com.kbstar.itsm.ui.util.Table;

public class TestMenuBar {
	
	private IExcelLoadToolMainContoller model = mock(IExcelLoadToolMainContoller.class);
	
	private MenuBar menuBar;
	
	private JMenuItem connItem;
	private JMenuItem disconnItem;
	private JMenuItem codeitem;
	private JMenuItem exportExcelFormat;
	private JMenuItem excelLoader;
	
	
	@BeforeEach
	public void setUp() {
		menuBar = new MenuBar();
		menuBar.initialize(model);
		initBind();
	}
	
	private void initBind() {
		connItem = Table.getElementByName(MenuBar.NAMES.CONN_ITEM, JMenuItem.class); 
		disconnItem = Table.getElementByName(MenuBar.NAMES.DSICONN_ITEM, JMenuItem.class);
		codeitem = Table.getElementByName(MenuBar.NAMES.CODE_ITEM, JMenuItem.class);
		exportExcelFormat = Table.getElementByName(MenuBar.NAMES.EXPORT_EXCEL_FORMAT, JMenuItem.class);
		excelLoader = Table.getElementByName(MenuBar.NAMES.EXCEL_LOADER, JMenuItem.class);
	}
	
	@Test
	public void 초기메뉴() {
		
		//then
		assertTrue(connItem.isEnabled());
		assertFalse(disconnItem.isEnabled());
		assertFalse(codeitem.isEnabled());
		assertFalse(exportExcelFormat.isEnabled());
		assertFalse(excelLoader.isEnabled());
	}

	
	@Test
	public void Test_연결완료시_메뉴() {
		
		//when
		menuBar.showConnectionMenu();
		
		//then
		assertFalse(connItem.isEnabled());
		assertTrue(disconnItem.isEnabled());
		assertTrue(codeitem.isEnabled());
		assertTrue(exportExcelFormat.isEnabled());
		assertTrue(excelLoader.isEnabled());
	}
	
	
	@Test
	public void test_click연결종료(){
		
		//give
		disconnItem.setEnabled(true);
		
		//when
		disconnItem.doClick();

		//then
		verify(model).clickCloseConnectionMenu();
		
	}
	
	
	@Test
	public void test_click연결하기() {
		
		//when
		connItem.doClick();
		
		//then
		verify(model).showDBLogonPopup();
	}
	
	
	@Test
	public void test_click익스포트엑셀양식() {
		
		//given
		exportExcelFormat.setEnabled(true);
		
		//when
		exportExcelFormat.doClick();
		
		//then
		verify(model).changeViewRegisterExcelForm();;
	}

	


}
