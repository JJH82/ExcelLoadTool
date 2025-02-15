package com.kbstar.itsm.exload.ui;

import java.awt.Component;
import java.util.Optional;
import java.util.Vector;

import com.kbstar.itsm.exload.controller.IExcelLoadToolMainContoller;
import com.kbstar.itsm.exload.model.DBInfo;

public interface IDBLogonPopup {
	
	public void selectedDefaultConnect(Optional<DBInfo> dbInfo);	
	
	public void setDBInfoList(Vector<DBInfo> list);
	
	public void clearDBInfoUI();
	
	public void setDBInfoUI(DBInfo dbInfo);
	
	public void updateUI();

	public void show(Component c);

	public void closePopup();

}
