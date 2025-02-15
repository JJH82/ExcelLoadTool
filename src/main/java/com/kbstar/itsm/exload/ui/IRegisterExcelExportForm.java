package com.kbstar.itsm.exload.ui;

import java.util.Vector;

import com.kbstar.itsm.exload.controller.IRegisterExcelExportFormController;
import com.kbstar.itsm.exload.model.MappingInfo;
import com.kbstar.itsm.ui.common.IView;

public interface IRegisterExcelExportForm extends IView{
	
	void setModel(IRegisterExcelExportFormController model);
	
	void initSavedMappingCombo(Vector<MappingInfo> list); 
	
	void displaySelectedMappingInfo(MappingInfo mapInfo);
	
	
	void setCommonCodeComboInGrid(Vector<String> codeConfig);

	void setEnableMappingUI(boolean enable);
	
	void setEnableExecuteBtn(boolean enable);

	void clearMappingInfoUI();
	
	void updateUIMappingGrid();
	
	void clearSelectionMappingGrid();
	
	
	

}
