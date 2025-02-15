package com.kbstar.itsm.exload.controller;

import java.sql.SQLException;

import javax.swing.SwingUtilities;

import com.kbstar.itsm.exload.ExcelLoadToolMain;
import com.kbstar.itsm.exload.IExcelLoadToolMain;
import com.kbstar.itsm.exload.dao.DBInfoFileDAO;
import com.kbstar.itsm.exload.dao.GenericVectorDAO;
import com.kbstar.itsm.exload.jdbc.DataSource;
import com.kbstar.itsm.exload.model.DBInfo;
import com.kbstar.itsm.exload.ui.DBLogonPopup;
import com.kbstar.itsm.exload.ui.IDBLogonPopup;
import com.kbstar.itsm.exload.ui.IMenuBar;
import com.kbstar.itsm.exload.ui.IToolBar;


public class ExcelLoadToolMainModel implements IExcelLoadToolMainContoller {
	
	private IMenuBar menuBar;
	private IToolBar toolBar;

	private IDBLogonPopup dbLogonPopup;
	private IExcelLoadToolMain mainView;
	
	private final GenericVectorDAO<DBInfo> dao = new DBInfoFileDAO();
	
	@Override
	public void setMainView(IExcelLoadToolMain main) {
		this.mainView = main;
	}
	
	@Override
	public void setMenuBar(IMenuBar menuBar) {
		this.menuBar = menuBar;
		this.menuBar.initialize(this);
	}

	@Override
	public void setToolBar(IToolBar toolBar) {
		this.toolBar = toolBar;
	}
	
	@Override
	public void showDBLogonPopup() {
		SwingUtilities.invokeLater(()-> {
			IDBLogonPopController controller = new DBLogonPopModel(dao);
			controller.setMainCtrl(this);
			dbLogonPopup = new DBLogonPopup(controller);
			dbLogonPopup.show(mainView.getComponent());			
		}) ; 
		
	}

	@Override
	public void connectComplete() {
		toolBar.setConnectInfo(DataSource.INSTANCE.getDBInfo());
		menuBar.showConnectionMenu();
	}

	@Override
	public void clickCloseConnectionMenu() {
		try {
			DataSource.INSTANCE.close();
		} catch (SQLException e) {}
		
		toolBar.setConnectInfo(DataSource.INSTANCE.getDBInfo());
		menuBar.showUnConnectionMenu();
		mainView.changeContentView(ExcelLoadToolMain.VIEWS.CLEAR_VIEW);
		
	}

	@Override
	public void changeViewPulbicCodeEnrollment() {
		mainView.changeContentView(ExcelLoadToolMain.VIEWS.PUBLIC_CODE_VEIW);
	}

	@Override
	public void changeViewRegisterExcelForm() {
		mainView.changeContentView(ExcelLoadToolMain.VIEWS.REGISTER_EXCEL_EXPORT);		
	}

	@Override
	public void changeViewLoaderExcelForm() {
		mainView.changeContentView(ExcelLoadToolMain.VIEWS.LOADER_EXCEL_VIEW);
	}
	
	
	

	
}
