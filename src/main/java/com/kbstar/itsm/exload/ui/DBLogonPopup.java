package com.kbstar.itsm.exload.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.util.Optional;
import java.util.UUID;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import com.kbstar.itsm.exload.controller.DBLogonPopModel;
import com.kbstar.itsm.exload.controller.IDBLogonPopController;
import com.kbstar.itsm.exload.controller.IExcelLoadToolMainContoller;
import com.kbstar.itsm.exload.dao.DBInfoFileDAO;
import com.kbstar.itsm.exload.dao.GenericVectorDAO;
import com.kbstar.itsm.exload.model.DBInfo;
import com.kbstar.itsm.ui.util.Alert;
import com.kbstar.itsm.ui.util.ProcessingModal;
import com.kbstar.itsm.ui.util.TD;
import com.kbstar.itsm.ui.util.Table;

public class DBLogonPopup implements IDBLogonPopup {
	
	
	private JComboBox<DBInfo> savedConnectName;
	
	private JDialog dialog;
	private Panel connectPanel;
	
	private JTextField connectName;
	private JTextField jdbcUrl;
	private JTextField userName;
	private JPasswordField password;
	private JCheckBox defaultConnChk;
	
	private JButton saveBtn;
	private JButton deleteBtn;
	private JButton connBtn;
	private JButton conntestBtn;
	private JButton closeBtn;
	
	private ProcessingModal modal = ProcessingModal.getInstance;
	
	
	public static enum NAMES {
		SAVED_CONNECT_NAME
		,CONNECT_NAME
		,JDBC_URL
		,USER_NAME
		,PASSWORD
		,DEFAULT_CONN_CHK
		,SAVE_BTN
		,DELETE_BTN
		,CONN_BTN
		,CONN_TEST_BTN
		,CLOSE_BTN;
	}
	
	
	private IDBLogonPopController model;
	private IExcelLoadToolMainContoller mainView;
	
	public DBLogonPopup(IDBLogonPopController model) {
		
		dialog = new JDialog();
		
		connectPanel = new Panel();
		
		this.model = model;
		
		initLayout();
		
		initBind();
		
		initEvent();
		
		model.setView(this);
		
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		Alert.setParent(dialog);
		
		
	}
	
	
	
	private void initLayout() {
		dialog.setSize(500,200);
		
		new Table
		.Builder(dialog)
		.tr()
		.td( new TD.Builder(new JLabel("저장된 연결명 : ")).build() )
		.td( new TD.Builder(NAMES.SAVED_CONNECT_NAME , new JComboBox<DBInfo>() ).maxWidth().colspan(3).build() )
		
		.tr()
		.td( new TD.Builder(new JLabel("연결명 : ")).build() )
		.td( new TD.Builder( NAMES.CONNECT_NAME, new JTextField("") ).maxWidth().build() )
		.td( new TD.Builder( NAMES.SAVE_BTN, new JButton("저장") ).build() )
		.td( new TD.Builder( NAMES.DELETE_BTN, new JButton("삭제") ).build() )
		
		.tr()
		.td( new TD.Builder(new JLabel("JDBC URL : ")).build() )
		.td( new TD.Builder(NAMES.JDBC_URL, new JTextField("")).colspan(3).minWidth().build() )

		.tr()
		.td( new TD.Builder(new JLabel("아이디 : ")).build() )
		.td( new TD.Builder(NAMES.USER_NAME, new JTextField("")).colspan(3).minWidth().build() )

		.tr()
		.td( new TD.Builder(new JLabel("패스워드 : ")).build() )
		.td( new TD.Builder(NAMES.PASSWORD, new JPasswordField("")).colspan(3).minWidth().build() )
		
		.tr()
		.td( new TD.Builder(NAMES.DEFAULT_CONN_CHK, new JCheckBox("기본연결설정")).build()        )
		.td( new TD.Builder( 
					new Table
					.Builder(connectPanel)
					.tr()
					.td( new TD.Builder(NAMES.CONN_BTN, new JButton("연결")).build() )
					.td( new TD.Builder(NAMES.CONN_TEST_BTN, new JButton("연결테스트")).build() )
					.td( new TD.Builder(NAMES.CLOSE_BTN, new JButton("닫기")).build() )
					.build()    
				).colspan(3).right().build()
			)
		.build();
		
	}
	
	@SuppressWarnings("unchecked")
	private void initBind() {
		connectName = (JTextField)Table.getElementByName(NAMES.CONNECT_NAME);
		jdbcUrl = (JTextField)Table.getElementByName(NAMES.JDBC_URL);
		userName = (JTextField)Table.getElementByName(NAMES.USER_NAME);
		password = (JPasswordField)Table.getElementByName(NAMES.PASSWORD);
		defaultConnChk = (JCheckBox)Table.getElementByName(NAMES.DEFAULT_CONN_CHK);
		
		savedConnectName = (JComboBox<DBInfo>)Table.getElementByName(NAMES.SAVED_CONNECT_NAME);
		
		saveBtn = (JButton)Table.getElementByName(NAMES.SAVE_BTN);
		deleteBtn = (JButton)Table.getElementByName(NAMES.DELETE_BTN);
		connBtn = (JButton)Table.getElementByName(NAMES.CONN_BTN);
		conntestBtn = (JButton)Table.getElementByName(NAMES.CONN_TEST_BTN);
		closeBtn = (JButton)Table.getElementByName(NAMES.CLOSE_BTN);
	}
	
	private void initEvent() {
		
		closeBtn.addActionListener((ActionEvent e) -> closePopup() );
		saveBtn.addActionListener( (ActionEvent e) -> model.save(getDBInfoUI()) );
		deleteBtn.addActionListener( (ActionEvent e) -> model.remove());
		connBtn.addActionListener( (ActionEvent e) -> {														
														modal.setMsg("연결중...");
														modal.open(dialog);
														new SwingWorker<Void, Void>() {

															@Override
															protected Void doInBackground() throws Exception {
																model.save(getDBInfoUI()); 
																model.connection();
																return null;
															}

															@Override
															protected void done() {
																modal.close();
															}
														}.execute();
													  }
								 );
		conntestBtn.addActionListener( (ActionEvent e) -> model.testConnection(getDBInfoUI()));
		savedConnectName.addActionListener((ActionEvent e) -> model.selectionChange(selectedDbInfo()) );
	}



	public Container getContentPane() {
		return dialog.getContentPane();
	}
	
	public Container getConnectPane() {
		return connectPanel;
	}
	
	
	
	@Override
	public void selectedDefaultConnect(Optional<DBInfo> dbInfo) {
		dbInfo.ifPresent((DBInfo o) -> savedConnectName.setSelectedItem(o));
	}

	
	
	@Override
	public void setDBInfoUI(DBInfo dbInfo) {
		connectName.setText(dbInfo.getConnectName());
		jdbcUrl.setText(dbInfo.getJdbcUrl());
		userName.setText(dbInfo.getUserName());
		password.setText(dbInfo.getPassword());
		defaultConnChk.setSelected(dbInfo.isDefaultConnectSetting());
	}
	
	private DBInfo getDBInfoUI() {
		
		return DBInfo.builder()
					 .id( selectedDbInfo() == null ? UUID.randomUUID().toString(): selectedDbInfo().getId())
					 .connectName(connectName.getText())
					 .jdbcUrl(jdbcUrl.getText())
					 .userName(userName.getText())
					 .password(password.getText())
					 .defaultConnectSetting(defaultConnChk.isSelected())
			  .build();
	}

	private DBInfo selectedDbInfo() {		
		return savedConnectName.getItemAt(savedConnectName.getSelectedIndex());
	}
	
	@Override
	public void setDBInfoList(Vector<DBInfo> list) {
		savedConnectName.setModel(new DefaultComboBoxModel<DBInfo>(list));
	}
	
	
	@Override
	public void clearDBInfoUI() {
		connectName.setText("");
		jdbcUrl.setText("");
		userName.setText("");
		password.setText("");
		defaultConnChk.setSelected(false);
	}
	
	public JDialog getDialog() {
		return dialog;
	}
	
	
	public static void main(String[] args) {
		
		
		GenericVectorDAO<DBInfo> dao = new DBInfoFileDAO();
		IDBLogonPopController controller = new DBLogonPopModel(dao);
		DBLogonPopup view = new DBLogonPopup(controller);
		
		view.getDialog().setVisible(true);
		
	}

	@Override
	public void updateUI() {
		savedConnectName.updateUI();
		savedConnectName.setSelectedIndex(-1);
	}

	@Override
	public void show(Component c) {
		
		dialog.setLocationRelativeTo(c);
		dialog.setVisible(true);
		
	}
	
	@Override
	public void closePopup() {
		dialog.dispose();			
	}

}
