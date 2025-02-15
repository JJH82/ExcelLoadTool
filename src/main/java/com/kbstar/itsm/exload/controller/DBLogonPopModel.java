package com.kbstar.itsm.exload.controller;

import java.util.Optional;
import java.util.Vector;

import javax.swing.SwingUtilities;

import com.kbstar.itsm.exload.dao.GenericVectorDAO;
import com.kbstar.itsm.exload.jdbc.DataSource;
import com.kbstar.itsm.exload.model.DBInfo;
import com.kbstar.itsm.exload.ui.IDBLogonPopup;
import com.kbstar.itsm.ui.util.Alert; 

public class DBLogonPopModel implements IDBLogonPopController {
	
	
	private IDBLogonPopup view;
	private GenericVectorDAO<DBInfo> dao;
	private DataSource ds = DataSource.INSTANCE;
	
	private Vector<DBInfo> dbInfoList;
	private Optional<DBInfo> selectedDBInfo = Optional.empty();
	private Optional<DBInfo> defaultConnectDBInfo =  Optional.empty();
	
	private IExcelLoadToolMainContoller mainCtrl;
	
	
	public DBLogonPopModel(GenericVectorDAO<DBInfo> dao) {
		this.dao = dao;
		this.dbInfoList = this.dao.loadAll();
	}
	
	
	@Override
	public void setMainCtrl(IExcelLoadToolMainContoller mainCtrl) {
		this.mainCtrl = mainCtrl;
	}
	

	@Override
	public void remove() {

		selectedDBInfo.ifPresent((DBInfo o) -> dbInfoList.remove(o));
		
		view.clearDBInfoUI();
		view.updateUI();

	}
	
	private void setOnlyOneDefaultConnectSetting(DBInfo dbInfo) {
		
		if(!dbInfo.isDefaultConnectSetting()) return;
		for(DBInfo o : dbInfoList) o.setDefaultConnectSetting(false);
		
	}
	
	@Override
	public void save(DBInfo dbInfo) {
	
		if(dbInfo.getConnectName().isEmpty()) return;
		
		setOnlyOneDefaultConnectSetting(dbInfo);

		if(isUpdate(dbInfo)) updateDBInfo(dbInfo);
		else {
			dbInfoList.add(dbInfo);
			view.updateUI();
			view.clearDBInfoUI();
		}
		
		
		dao.saveAll(dbInfoList);
	}


	private boolean isUpdate(DBInfo dbInfo) {
		
		return this.dbInfoList
				.stream()
				.anyMatch(
						(DBInfo o) -> dbInfo.getConnectName().equals(o.getConnectName())
						);
		
	}

	private void updateDBInfo(DBInfo dbInfo) {
		
		selectedDBInfo.ifPresent((DBInfo o) -> {
			o.setId(dbInfo.getId());
			o.setConnectName(dbInfo.getConnectName());
			o.setJdbcUrl(dbInfo.getJdbcUrl());
			o.setUserName(dbInfo.getUserName());
			o.setPassword(dbInfo.getPassword());
			o.setDefaultConnectSetting(dbInfo.isDefaultConnectSetting());
		});
		
	}

	@Override
	public void selectionChange(DBInfo dbInfo) {
		selectedDBInfo = Optional.ofNullable(dbInfo);
		selectedDBInfo.ifPresent( (DBInfo o) -> view.setDBInfoUI(o));
	}



	@Override
	public void setView(IDBLogonPopup view) {
		
		this.view = view;
	
		view.setDBInfoList(dbInfoList);
		
		this.defaultConnectDBInfo = dbInfoList
				.stream()
				.filter((DBInfo dbInfo) ->  dbInfo.isDefaultConnectSetting())
				.findFirst(); 
		
		view.selectedDefaultConnect(defaultConnectDBInfo);

		
	}


	@Override
	public void connection() {
		selectedDBInfo.ifPresent( (DBInfo o) -> {
			ds.initConfig(o);
		try {
				ds.getConnection().isValid(300);					

				view.closePopup();
				mainCtrl.connectComplete();
			} catch (Exception e) {
				Alert.ERROR.show(e.toString());
				
			}	
		}); 
		
	}


	@Override
	public void testConnection(DBInfo dbInfo) {
		try {
			ds.testConnect(dbInfo);	
		} catch (Exception e) {
			Alert.ERROR.show( e.toString());
			return;
		}
		
		Alert.INFO.show("연결성공");

	}

	


}
