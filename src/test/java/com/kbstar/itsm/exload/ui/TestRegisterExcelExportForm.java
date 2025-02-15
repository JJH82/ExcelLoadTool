package com.kbstar.itsm.exload.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.kbstar.itsm.exload.controller.IRegisterExcelExportFormController;
import com.kbstar.itsm.exload.model.MappingInfo;
import com.kbstar.itsm.exload.ui.RegisterExcelExportForm.NAMES;
import com.kbstar.itsm.test.util.GeneratorUtil;
import com.kbstar.itsm.ui.model.JGrid;
import com.kbstar.itsm.ui.util.Table;

public class TestRegisterExcelExportForm{
	
	private RegisterExcelExportForm view;
	private IRegisterExcelExportFormController model = mock(IRegisterExcelExportFormController.class);
	
	
	private JComboBox<MappingInfo> savedNameCombo;
	private JComboBox<String> codeComboBox =  new JComboBox<String>();
	private JComboBox<String> columnNmCombo =  new JComboBox<String>();
	private JTextField nameTxt;
	private JButton addFileBtn;
	private JTextArea sqlTxtA;
	private JButton sqlExecuteBtn;
	private JButton gridAddBtn;
	private JButton gridDelBtn;
	private JGrid mappingGrid;
	private JButton saveBtn;
	private JButton delBtn;
	private JButton ouputExcelBtn;
	
	@BeforeEach
	public void setup() {
		// given
		view = new RegisterExcelExportForm(model);
		initBind();
	}
	
	
	@Test
	public void 저장된매핑에_값이_있을경우() {
		
		Vector<MappingInfo> threeList =  리스트_컬럼명_3개생성();		
		view.initSavedMappingCombo(threeList);
		// 콤보박스에 값이 설정되었는지확인
		assertEquals(threeList.size(), savedNameCombo.getModel().getSize());
	}
	
	
	@Test
	public void 저장된매핑_값선택시() {
		MappingInfo aRow = 리스트_컬럼명_3개생성().get(0);
		view.displaySelectedMappingInfo(aRow);
		assertEquals(nameTxt.getText(), aRow.getName());
		assertEquals(sqlTxtA.getText(), aRow.getSql());
		assertEquals(mappingGrid.getModel().getRowCount(), aRow.getMappingList().size());
	}
	
	@Test
	public void 저장된_매핑명_값변경시() {
		Vector<MappingInfo> threeList =  리스트_컬럼명_3개생성();
		ArgumentCaptor<Optional<MappingInfo>> arg = ArgumentCaptor.forClass(Optional.class);
		
		view.initSavedMappingCombo(threeList);
		
		// 첫번째 값선택
		savedNameCombo.setSelectedIndex(1);
		
		//메소드 실해여부확인
		verify(model).selectionChange(arg.capture());
		
		// 첫번째 값 맞는지 확인
		assertEquals(threeList.get(1),arg.getValue().get());
	}
	
	
	
	
	
	@Test
	public void 삭제버튼클릭() {
		delBtn.doClick();
		verify(model).remove();
	}
	
	@Test
	public void 그리드추가() {
		gridAddBtn.doClick();
		verify(model).addRow(-1);

	}
	
	@Test
	public void 그리드삭제() {
		gridDelBtn.doClick();
		verify(model).removeRow(anyInt());
	}
	
	@Test
	public void 모델의값매핑() {
		
		MappingInfo mInfo = 리스트_컬럼명_3개생성().get(0);
		view.displaySelectedMappingInfo(mInfo);
		
		assertEquals(mInfo.getName(), nameTxt.getText());
		assertEquals(mInfo.getSql(), sqlTxtA.getText());
		assertEquals(mInfo.getColumnList().size(),columnNmCombo.getModel().getSize() );
		assertEquals(mInfo.getMappingList().size(), mappingGrid.getModel().getRowCount() );

	}
	
	
	@Test
	public void 공통코드_콤보박스에값적용() {
		
		 Vector<String> condeValue = 값_3개생성();
		view.setCommonCodeComboInGrid(condeValue);
		assertEquals(condeValue.size(), codeComboBox.getModel().getSize());
		assertEquals(condeValue.get(0),codeComboBox.getModel().getElementAt(0) );
	}

	
	private Vector<String> 값_3개생성() {

		return GeneratorUtil.toVector(3, i ->  "값 : "+ i );

	}
	
	
	
	
	
	private Vector<MappingInfo> 리스트_3개생성() {

		return GeneratorUtil.toVector(3, i -> {
			return MappingInfo.builder()
			.name("이름" + i )
			.sql("sql" + i)
			.mappingList(
					GeneratorUtil.toVector(3, j -> {
						Map<String,String> m = new HashMap<>();
						m.put("key"+j, "value"+ j);
						return m;
					})
					)
			.build();
		});
	}
	
	
	private Vector<MappingInfo> 리스트_컬럼명_3개생성() {

		return GeneratorUtil.toVector(3, i -> {
			return MappingInfo.builder()
			.name("이름" + i )
			.sql("sql" + i)
			.mappingList(
					GeneratorUtil.toVector(3, j -> {
						Map<String,String> m = new HashMap<>();
						m.put("key"+j, "value"+ j);
						return m;
					})
					)
			.columnList(new Vector<>(GeneratorUtil.toList(3, k -> "구성항목명"+k )) )
			.build();
		});
	}
	
	
	private void initBind() {
		
		savedNameCombo = Table.getElementByName(NAMES.SAVED_NAME_COMBO,JComboBox.class);
		nameTxt = Table.getElementByName(NAMES.NAME_TXT,JTextField.class);

		
		sqlTxtA = Table.getElementByNameInJScrollPanel(NAMES.SQL_TXTA, JTextArea.class);

		

		sqlExecuteBtn = Table.getElementByName(NAMES.SQL_EXECUTE_BTN,JButton.class);
		gridAddBtn = Table.getElementByName(NAMES.GRID_ADD_BTN,JButton.class);
		gridDelBtn = Table.getElementByName(NAMES.GRID_DEL_BTN,JButton.class);
		
		mappingGrid = Table.getElementByNameInJScrollPanel(NAMES.MAPING_GRID, JGrid.class);		
		
		saveBtn = Table.getElementByName(NAMES.SAVE_BTN,JButton.class);
		delBtn = Table.getElementByName(NAMES.DEL_BTN,JButton.class);
		ouputExcelBtn = Table.getElementByName(NAMES.OUTPUT_EXCEL_BTN,JButton.class);
		
		codeComboBox = Table.getElementByName(NAMES.CODE_COMBO, JComboBox.class);
		columnNmCombo = Table.getElementByName(NAMES.COLUM_NM_COMBO, JComboBox.class);
		
		
	}

	

}
