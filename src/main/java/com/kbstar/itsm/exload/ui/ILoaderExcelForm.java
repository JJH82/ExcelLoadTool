package com.kbstar.itsm.exload.ui;

import java.io.File;

import com.kbstar.itsm.ui.common.IView;

public interface ILoaderExcelForm extends IView {
	
	void clearView();
	void selectFile(File file);
	void outputSqlArea(String value);
	void viewFileName(File file);

}
