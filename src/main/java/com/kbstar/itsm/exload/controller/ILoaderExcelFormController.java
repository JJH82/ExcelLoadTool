package com.kbstar.itsm.exload.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

import com.kbstar.itsm.exload.ui.ILoaderExcelForm;

public interface ILoaderExcelFormController {
	
	void setView(ILoaderExcelForm view);
	
	void initLoadFileForm();
	
	void selectedFile(Optional<File> optional);
	
	void findLoadFile(Optional<File> loadFile);

	void printSql(String sqlMode);

	void saveFile(Optional<File> saveFile);

}
