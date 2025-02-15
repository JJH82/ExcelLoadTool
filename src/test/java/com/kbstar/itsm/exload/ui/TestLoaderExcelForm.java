package com.kbstar.itsm.exload.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kbstar.itsm.exload.controller.ILoaderExcelFormController;
import com.kbstar.itsm.exload.ui.LoaderExcelForm.NAMES;
import com.kbstar.itsm.ui.util.Table;

public class TestLoaderExcelForm {
	
	private ILoaderExcelFormController model = mock(ILoaderExcelFormController.class);
	private ILoaderExcelForm view;
	
	private JTextField fileName;
	private JButton findFileBtn;
	private JButton delFileBtn;
	private JComboBox<String> addSqlCombo;
	private JComboBox<String> sqlMode;
	private JTextArea outputSqlArea;
	private JButton outputBtn;
	private JButton outputFileBtn;
	
	
	
	@BeforeEach
	public void setup() {
		view = new LoaderExcelForm(model);
		model.setView(view);		
		putComponetsIntoMemberVariables();
	}
	
	
	
	private void putComponetsIntoMemberVariables() {

		fileName = Table.getElementByName(NAMES.FILE_NAME,JTextField.class);
		findFileBtn = Table.getElementByName(NAMES.FIND_FILE_BTN,JButton.class);
		delFileBtn = Table.getElementByName(NAMES.DEL_FILE_BTN,JButton.class);
		sqlMode = Table.getElementByName(NAMES.SQL_MODE,JComboBox.class);
		outputSqlArea = Table.getElementByNameInJScrollPanel(NAMES.OUTPUT_SQL_AREA, JTextArea.class);
		outputBtn = Table.getElementByName(NAMES.OUTPUT_BTN, JButton.class);
		outputFileBtn = Table.getElementByName(NAMES.OUTPUT_FILE_BTN, JButton.class);
		
	}
	
	@Test
	public void test초기화버튼클릭시() {
		//given
		fileName.setText("테스트");
		outputSqlArea.setText("테스트");
		
		
		//when
		delFileBtn.doClick();
		
		
		//then		
		verify(model).initLoadFileForm();
	}
	
	@Test
	public void test찾기버튼파일선택시() {

		// given
		File file = new File("C:\\테스트.xlsx");
		
		// when
		view.selectFile(file);
		
		//then
		assertEquals(fileName.getText(), "C:\\테스트.xlsx");
		assertEquals(outputBtn.isEnabled(), true);
		
	}
	
	

	
	
}
