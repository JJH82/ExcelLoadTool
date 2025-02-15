package com.kbstar.itsm.exload.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.kbstar.itsm.exload.controller.IExcelLoadToolMainContoller;
import com.kbstar.itsm.ui.util.TD;
import com.kbstar.itsm.ui.util.Table;

public class MenuBar implements IMenuBar  {
	
	private static final long serialVersionUID = 1L;

	private JMenuItem connItem;
	private JMenuItem disconnItem;
	private JMenuItem codeItem;
	private JMenuItem formatItem;
	private JMenuItem loaderItem;
	
	
	public static enum NAMES {
		CONN_ITEM
		,DSICONN_ITEM
		,CODE_ITEM
		,EXPORT_EXCEL_FORMAT
		,EXCEL_LOADER;
	}
	
	private IExcelLoadToolMainContoller mainCtrl;
	
	private JMenuBar menubar =  new JMenuBar();

	@Override
	public void initialize(IExcelLoadToolMainContoller excelLoadToolMain) {
		
		mainCtrl = excelLoadToolMain;
		
		setLayout();
		initBind();
		
		connItem.addActionListener((ActionEvent e) -> mainCtrl.showDBLogonPopup());
		codeItem.addActionListener((ActionEvent e) -> mainCtrl.changeViewPulbicCodeEnrollment());
		formatItem.addActionListener((ActionEvent e) -> mainCtrl.changeViewRegisterExcelForm());
		disconnItem.addActionListener((ActionEvent e) -> mainCtrl.clickCloseConnectionMenu());
		loaderItem.addActionListener((ActionEvent e) -> mainCtrl.changeViewLoaderExcelForm());

		

		showUnConnectionMenu();
	}
	

	@Override
	public void showUnConnectionMenu() {
		connItem.setEnabled(true);
		disconnItem.setEnabled(false);
		codeItem.setEnabled(false);
		formatItem.setEnabled(false);
		loaderItem.setEnabled(false);
	}
	
	@Override
	public void showConnectionMenu() {
		connItem.setEnabled(false);
		disconnItem.setEnabled(true);
		codeItem.setEnabled(true);
		formatItem.setEnabled(true);
		loaderItem.setEnabled(true);
	}
	
	


	private void setLayout() {
		menubar.setSize(new Dimension(10,20));
		menubar.setMinimumSize(menubar.getSize());		
		menubar.setMaximumSize(menubar.getSize());
		menubar.setPreferredSize(menubar.getSize());
		
		menubar.add(
				new Table.Builder(new JMenu("설정"))
					.tableStyle(false)
					.tr()
					.td( new TD.Builder(NAMES.CONN_ITEM, new JMenuItem("연결") ).build() )
					.tr()
					.td( new TD.Builder(NAMES.DSICONN_ITEM, new JMenuItem("연결종료") ).build() )
					.tr()
					.td( new TD.Builder(NAMES.CODE_ITEM, new JMenuItem("코드설정") ).build() )
					.tr()
					.td( new TD.Builder(NAMES.EXPORT_EXCEL_FORMAT, new JMenuItem("익스포트엑셀양식") ).build() )
					.tr()
					.td( new TD.Builder(NAMES.EXCEL_LOADER, new JMenuItem("로드엑셀") ).build() )
					.build()
				);
		
		
	}
	
	private void initBind() {
		connItem = Table.getElementByName(NAMES.CONN_ITEM, JMenuItem.class); 
		disconnItem = Table.getElementByName(NAMES.DSICONN_ITEM, JMenuItem.class);
		codeItem = Table.getElementByName(NAMES.CODE_ITEM, JMenuItem.class);
		formatItem= Table.getElementByName(NAMES.EXPORT_EXCEL_FORMAT, JMenuItem.class);
		loaderItem= Table.getElementByName(NAMES.EXCEL_LOADER, JMenuItem.class);
	}


	@Override
	public Component getComponent() {
		return menubar;
	}
	

}
