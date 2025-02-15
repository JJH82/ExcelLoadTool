package com.kbstar.itsm.exload.ui;

import static com.kbstar.itsm.ui.util.Table.getElementByName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kbstar.itsm.exload.controller.IDBLogonPopController;
import com.kbstar.itsm.exload.controller.IExcelLoadToolMainContoller;
import com.kbstar.itsm.exload.model.DBInfo;

public class TestDBLogonPopup implements IDBLogonPopController {
	

	private int deleteCount;
	private JTextField connectName;
	private JTextField jdbcUrl;
	private JTextField userName;
	private JTextField password;
	private JCheckBox defaultConnChk; 
	
	private JButton saveBtn;
	private JButton deleteBtn;
	private JButton connBtn;
	private JButton conntestBtn;
	private Vector<DBInfo> dbInfoList = new Vector<DBInfo>();
	
	private JComboBox<DBInfo> savedConnectName;
	
	private DBInfo selectedDBInfo;
	
	private DBInfo saveDbInfo;
	
	private DBLogonPopup view;
	
	private IDBLogonPopup setView;
	
	private int m_connection ;
	
	private int m_testConnectionCount ;
	
	
	@BeforeEach
	public void setUp() {
		
		view = new DBLogonPopup(this);
		
		view.setDBInfoList(dbInfoList);
		
		putComponetsIntoMemberVariables();
		
	}
	

	private void putComponetsIntoMemberVariables() {

		savedConnectName = (JComboBox<DBInfo>)getElementByName(DBLogonPopup.NAMES.SAVED_CONNECT_NAME);
		connectName = (JTextField)getElementByName(DBLogonPopup.NAMES.CONNECT_NAME);
		jdbcUrl = (JTextField)getElementByName(DBLogonPopup.NAMES.JDBC_URL);
		userName = (JTextField)getElementByName(DBLogonPopup.NAMES.USER_NAME);
		password = (JTextField)getElementByName(DBLogonPopup.NAMES.PASSWORD);
		defaultConnChk = (JCheckBox)getElementByName(DBLogonPopup.NAMES.DEFAULT_CONN_CHK);
		
		saveBtn = (JButton)getElementByName(DBLogonPopup.NAMES.SAVE_BTN);
		deleteBtn = (JButton)getElementByName(DBLogonPopup.NAMES.DELETE_BTN);
		connBtn = (JButton)getElementByName(DBLogonPopup.NAMES.CONN_BTN);
		conntestBtn = (JButton)getElementByName(DBLogonPopup.NAMES.CONN_TEST_BTN);
		
		
	}


	@Test
	public void test뷰를_컨트롤로에_등록() {
		assertEquals(view, setView);
	}
	
	
	@Test
	public void test두번째행_선택() {
		
		addDBInfo();
		savedConnectName.setSelectedIndex(1);

		view.setDBInfoUI(selectedDBInfo);
		
		assertNotNull(selectedDBInfo);
		assertEquals(selectedDBInfo.getConnectName(), connectName.getText());
		assertEquals(selectedDBInfo.getJdbcUrl(), jdbcUrl.getText());
		assertEquals(selectedDBInfo.getUserName(), userName.getText());
		assertEquals(selectedDBInfo.getPassword(), password.getText());
		assertEquals(selectedDBInfo.isDefaultConnectSetting(), defaultConnChk.isSelected());
	}
	
	
	@Test
	public void test두번째행_삭제() {
		
		addDBInfo();
		savedConnectName.setSelectedIndex(1);
		deleteBtn.doClick();
		view.clearDBInfoUI();
		
		
		assertEquals(1, deleteCount);
		assertEquals(dbInfoList.get(1), selectedDBInfo);
		
		assertEquals("", connectName.getText());
		assertEquals("", jdbcUrl.getText());
		assertEquals("", userName.getText());
		assertEquals("", password.getText());
		assertFalse(defaultConnChk.isSelected());		
	}
	
	
	@Test
	public void test새로운데이터저장() {
		
		connectName.setText("새로운저장");
		jdbcUrl.setText("새로운jdbc Url");
		userName.setText("test1");
		password.setText("test!");
		defaultConnChk.setSelected(true);
		saveBtn.doClick();
		
		assertEquals(connectName.getText(), saveDbInfo.getConnectName());
		assertEquals(jdbcUrl.getText(), saveDbInfo.getJdbcUrl() );
		assertEquals(userName.getText(),  saveDbInfo.getUserName());
		assertEquals(password.getText(), saveDbInfo.getPassword() );
		assertTrue(saveDbInfo.isDefaultConnectSetting());		
	}
	

	


	
	@Test
	public void test연결_테스트_버튼클릭() {
		
		conntestBtn.doClick();	
		
		assertEquals(1,m_testConnectionCount);		
	}
	
	
	

	
	
	private void addDBInfo() {
		

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
	public void selectionChange(DBInfo dbInfo) {
		selectedDBInfo = dbInfo;
		
	}


	@Override
	public void remove() {
		deleteCount++;
	}




	@Override
	public void save(DBInfo dbInfo) {
		saveDbInfo = dbInfo;
		
	}

	@Override
	public void setView(IDBLogonPopup view) {
		this.setView = view;
		
	}


	@Override
	public void connection() {
		m_connection++;
		
	}


	@Override
	public void testConnection(DBInfo dbinfo) {
		m_testConnectionCount++;
		
	}


	@Override
	public void setMainCtrl(IExcelLoadToolMainContoller mainCtrl) {
		
		
	}



	


	

}
