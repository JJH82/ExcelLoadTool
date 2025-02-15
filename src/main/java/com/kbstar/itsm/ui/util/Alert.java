package com.kbstar.itsm.ui.util;

import java.awt.Component;

import javax.swing.JOptionPane;

public enum Alert {
	
	INFO("알림창",JOptionPane.INFORMATION_MESSAGE),
	ERROR("에러",JOptionPane.ERROR_MESSAGE);
	
	private String title;
	private int icon;
	private static Component parent;
	
	private Alert(String title, int icon) {
		this.title = title;
		this.icon = icon;
	}
	
	public static void setParent(Component parent) {
		Alert.parent = parent;
	}
	
	public void show(String msg) {
		JOptionPane.showMessageDialog(parent, msg, title, icon);
	}

}
