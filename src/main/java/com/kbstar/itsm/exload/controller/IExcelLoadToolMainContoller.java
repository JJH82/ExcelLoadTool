package com.kbstar.itsm.exload.controller;

import com.kbstar.itsm.exload.IExcelLoadToolMain;
import com.kbstar.itsm.exload.ui.IMenuBar;
import com.kbstar.itsm.exload.ui.IToolBar;

public interface IExcelLoadToolMainContoller {
	
	
	public void showDBLogonPopup();
	
	public void connectComplete();
	
	public void clickCloseConnectionMenu();

	void setMainView(IExcelLoadToolMain main);

	void setToolBar(IToolBar toolBar);

	void setMenuBar(IMenuBar menuBar);

	public void changeViewPulbicCodeEnrollment();
	
	public void changeViewRegisterExcelForm();
	
	public void changeViewLoaderExcelForm();
	
	
	

}
