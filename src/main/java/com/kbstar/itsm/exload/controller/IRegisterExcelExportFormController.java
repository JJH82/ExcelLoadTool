package com.kbstar.itsm.exload.controller;

import java.io.File;
import java.util.Optional;

import com.kbstar.itsm.exload.model.MappingInfo;
import com.kbstar.itsm.exload.ui.IRegisterExcelExportForm;

public interface IRegisterExcelExportFormController {
	
	public void setDefaultView(IRegisterExcelExportForm reef);
	
	public void selectionChange(Optional<MappingInfo> mappingInfo);

	public void save(MappingInfo mappingInfo);
	
	public void executeSql(String sql);
	
	public void remove();
	
	public void addRow(int index);
	
	public void removeRow(int index);
	
	
	public void clearFreezePane();
	
	public void exportExcel(File exportFile,MappingInfo mappingInfo);
	
	public void clearData();

}
