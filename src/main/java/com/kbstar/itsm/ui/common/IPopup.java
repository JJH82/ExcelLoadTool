package com.kbstar.itsm.ui.common;

import java.awt.Component;

public interface IPopup {
	
	public void setParent(Component parent);
	
	public void show();
	
	public void postClosed();

}
