package com.kbstar.itsm.exload.controller;

import com.kbstar.itsm.exload.model.DBInfo;
import com.kbstar.itsm.exload.ui.IDBLogonPopup;

public interface IDBLogonPopController {
	
	public void setView(IDBLogonPopup view);
	
	void setMainCtrl(IExcelLoadToolMainContoller mainCtrl);
	
	public void selectionChange(DBInfo dbInfo);
	
	public void remove();
	
	public void save(DBInfo dbInfo);
	
	public void connection();
	
	public void testConnection(DBInfo dbInfo);


}
