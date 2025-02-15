package com.kbstar.itsm.exload.ui;

import java.awt.Component;
import java.awt.TextField;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import com.kbstar.itsm.exload.model.DBInfo;
import com.kbstar.itsm.ui.common.IView;
import com.kbstar.itsm.ui.util.TD;
import com.kbstar.itsm.ui.util.Table;

public class ToolBar implements IToolBar, IView {
	
	private JTextField connectInfo;
	private DBInfo dbInfo;
	private JMenuBar menubar;
	
	public static String CONNECT_INFO = "connectInfo";

	public ToolBar() {
		menubar = new JMenuBar();

		connectInfo = new JTextField("");
		connectInfo.setEditable(false);
		connectInfo.setFocusable(false);
		
		new Table.Builder(menubar)
			.tableStyle(false)
			.tr()
			.td( new TD.Builder(new JLabel("연결정보 : ") ).build() )
			.tr()
			.td( new TD.Builder(CONNECT_INFO, connectInfo ).build() )
		.build();
	
	}
	
	@Override
	public Component getComponent() {
		return menubar;
	}
	
	@Override
	public void setConnectInfo(DBInfo dbInfo) {
		
		connectInfo.setText(dbInfo.getConnectName());
		
		
	}

}
