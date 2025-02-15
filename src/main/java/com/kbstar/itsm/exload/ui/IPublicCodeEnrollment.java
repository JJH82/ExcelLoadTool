package com.kbstar.itsm.exload.ui;

import java.util.Map;
import java.util.Optional;
import java.util.Vector;

import com.kbstar.itsm.exload.model.CodeConfig;
import com.kbstar.itsm.ui.common.IView;

public interface IPublicCodeEnrollment extends IView {

	CodeConfig getCodeConfigUI();
	
	void displaySavedCodeNameComboData(Vector<CodeConfig> list);

	void displayCodeConfigUI(CodeConfig codeConfig);
	
	void displayCodeListGridData(Vector<Map<String,String>> list);
	
	void clearCodeConfigUI();
	
	void initSavedMappingCombo(Vector<CodeConfig> list);
	
	void updateUI();

}