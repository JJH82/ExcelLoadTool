package com.kbstar.itsm.exload.ui;

import com.kbstar.itsm.exload.controller.IExcelLoadToolMainContoller;
import com.kbstar.itsm.ui.common.IView;

public interface IMenuBar extends IView{

	void showUnConnectionMenu();

	void showConnectionMenu();

	void initialize(IExcelLoadToolMainContoller excelLoadToolMain);
	

}