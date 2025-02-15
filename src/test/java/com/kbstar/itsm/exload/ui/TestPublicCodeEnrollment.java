package com.kbstar.itsm.exload.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kbstar.itsm.exload.controller.IPublicCodeEnrollmentController;
import com.kbstar.itsm.exload.model.CodeConfig;
import com.kbstar.itsm.exload.ui.PublicCodeEnrollment.NAMES;
import com.kbstar.itsm.ui.util.Table;

public class TestPublicCodeEnrollment implements IPublicCodeEnrollmentController {


	private PublicCodeEnrollment view;
	
	private JComboBox<CodeConfig> savedCodeNameCombo;
	private JTextField codeNameTxt;
	private JTextArea sqlTxtA;
	private JTextField paramsTxt;
	
	private JButton executeBtn;
	private JButton saveBtn;
	private JButton deleteBtn;
	
	private JTable codeListGrid;
	
	private IPublicCodeEnrollment p_setDefaultView;
	
	private int m_remove;
	
	private CodeConfig p_execute;
	
	private Optional<CodeConfig> selectedItem;
	
	private CodeConfig p_save;
	
	private Vector<CodeConfig> codeConfigList = new Vector<CodeConfig>();
	
	
	@BeforeEach
	public void setup() {
		view = new PublicCodeEnrollment(this);
		view.displaySavedCodeNameComboData(codeConfigList);
		
		initBind();
	}
	
	
	@SuppressWarnings("unchecked")
	private void initBind() {
		
		savedCodeNameCombo = Table.getElementByName(NAMES.SAVED_CODE_NAME,JComboBox.class);
		codeNameTxt = Table.getElementByName(NAMES.CODE_NAME,JTextField.class);
		sqlTxtA = (JTextArea)Table.getElementByName(NAMES.SQL,JScrollPane.class).getViewport().getComponent(0);
		paramsTxt = Table.getElementByName(NAMES.PARMS,JTextField.class);
		
		executeBtn = Table.getElementByName(NAMES.EXECUTE,JButton.class);
		saveBtn = Table.getElementByName(NAMES.SAVE,JButton.class);
		deleteBtn = Table.getElementByName(NAMES.DELETE,JButton.class);
		
		codeListGrid = (JTable)Table.getElementByName(NAMES.CODE_LIST_GRID, JScrollPane.class).getViewport().getComponent(0);
	}
	
	
	@Test
	public void test컨트롤러에_디폴트뷰설정() {
		assertEquals(view, p_setDefaultView);
	}
	
	
	
	@Test
	public void test두번째행_선택() {
		
		createCodeCofigList();
		
		assertEquals(3, savedCodeNameCombo.getModel().getSize());

		savedCodeNameCombo.setSelectedIndex(1);
		
		selectedItem.ifPresent(view::displayCodeConfigUI);
		
		assertNotNull(selectedItem);
		
		assertEquals(selectedItem.get().getName(), codeNameTxt.getText());
		assertEquals(selectedItem.get().getSql(), sqlTxtA.getText());
		assertEquals(selectedItem.get().getParameters(), paramsTxt.getText());
	}


	@Test
	public void test두번째행_삭제() {
		
		createCodeCofigList();
		assertEquals(3, savedCodeNameCombo.getModel().getSize());
		
		savedCodeNameCombo.setSelectedIndex(1);
		assertEquals(codeConfigList.get(1), selectedItem.get());

		deleteBtn.doClick();
		assertEquals(1, m_remove);
		
		view.clearCodeConfigUI();
		assertEquals(-1, savedCodeNameCombo.getSelectedIndex());
		assertEquals("", codeNameTxt.getText());
		assertEquals("", paramsTxt.getText());
		assertEquals("", paramsTxt.getText());
		
	}
	
	
	
	@Test
	public void testUI값을_CodeInfo매핑() {
		
		codeNameTxt.setText("새로운sql");
		sqlTxtA.setText("sql");
		paramsTxt.setText("param1");
		saveBtn.doClick();
		
		CodeConfig output= view.getCodeConfigUI();
		
		assertNotNull(output);
		assertEquals(codeNameTxt.getText(), output.getName());
		assertEquals(sqlTxtA.getText(), output.getSql());
		assertEquals(paramsTxt.getText(), output.getParameters());
	}
	
	@Test
	public void test새로운데이터저장() {
		
		codeNameTxt.setText("새로운sql");
		sqlTxtA.setText("sql");
		paramsTxt.setText("param1");
		saveBtn.doClick();
		
		CodeConfig input= view.getCodeConfigUI();
		 
		assertNotNull(input);
		assertEquals(input.getName(), p_save.getName());
		assertEquals(input.getSql(), p_save.getSql());
		assertEquals(input.getParameters(), p_save.getParameters());
	}
	

	
	
	@Test
	public void test실행버튼_클릭후_실행결과표출() {
		
		Vector<Map<String,String>> codeList = new Vector<Map<String,String>>();
		
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("key5", "value1");
		map.put("key4", "value2");
		map.put("1", "value3");
		codeList.add(map);
				
		view.displayCodeListGridData(codeList);
		
		
		assertTrue(codeListGrid.getModel().getRowCount()>0);
		assertEquals(codeListGrid.getColumnName(0), "key5");
		assertEquals(codeListGrid.getColumnName(1), "key4");
		
		
	}
	
	
	private void createCodeCofigList() {
		
		Vector<Map<String,String>> list1 = new Vector<Map<String,String>>();
		Map<String,String> map1 = new HashMap<String,String>();
		map1.put("테스트키1","테스트값2");
		list1.add(map1);
		
		codeConfigList.add(
							CodeConfig.builder()
								.name("name테스트1")
								.sql("sql테스트1")
								
								.parameters("파라미터1")
								.list(createCodeList("key1","value1"))
								.build()
						);

		codeConfigList.add(
				CodeConfig.builder()
					.name("name테스트2")
					.sql("sql테스트2")
					.list(createCodeList("key2","value2"))
					.parameters("파라미터2")
					.build()
			);

		codeConfigList.add(
				CodeConfig.builder()
					.name("name테스트3")
					.sql("sql테스트3")
					.list(createCodeList("key3","value3"))
					.parameters("파라미터3")
					.build()
			);

		
	}
	
	public Vector<Map<String,String>> createCodeList(String key,String value) {
		
		Vector<Map<String,String>> list = new Vector<Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
		map.put(key,value);		
		list.add(map);
		
		return list;
	}



	@Override
	public void selectionChange(Optional<CodeConfig> codeConfig) {
		selectedItem = codeConfig;
	}


	@Override
	public void save(CodeConfig codeConfig) {
		p_save = codeConfig;
		
	}


	@Override
	public void remove() {
		m_remove++;
		
	}


	@Override
	public void execute(CodeConfig codeConfig) {
		p_execute = codeConfig;
		
	}


	@Override
	public void setDefaultView(IPublicCodeEnrollment pce) {
		p_setDefaultView = pce;
		
	}

}
