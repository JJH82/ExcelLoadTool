
package com.kbstar.itsm.exload.ui;

import java.awt.Component;
import java.awt.Container;
import java.util.Optional;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.kbstar.itsm.exload.controller.IRegisterExcelExportFormController;
import com.kbstar.itsm.exload.controller.RegisterExcelExportFormModel;
import com.kbstar.itsm.exload.dao.CodeConfigDAO;
import com.kbstar.itsm.exload.dao.MappingInfoDAO;
import com.kbstar.itsm.exload.dao.SqlDAO;
import com.kbstar.itsm.exload.jdbc.DataSource;
import com.kbstar.itsm.exload.model.DBInfo;
import com.kbstar.itsm.exload.model.MappingInfo;
import com.kbstar.itsm.ui.model.JGrid;
import com.kbstar.itsm.ui.util.Alert;
import com.kbstar.itsm.ui.util.FileChoose;
import com.kbstar.itsm.ui.util.ProcessingModal;
import com.kbstar.itsm.ui.util.TD;
import com.kbstar.itsm.ui.util.Table;

public class RegisterExcelExportForm implements IRegisterExcelExportForm {
	
	
	private IRegisterExcelExportFormController model;
	
	private JPanel panel  = new JPanel();
	 
	public enum NAMES {
		SAVED_NAME_COMBO
		,NAME_TXT
		,SQL_TXTA
		,SQL_EXECUTE_BTN
		,GRID_ADD_BTN
		,GRID_DEL_BTN
		,MAPING_GRID 
		,SAVE_BTN
		,DEL_BTN
		,OUTPUT_EXCEL_BTN
		,CODE_COMBO
		,COLUM_NM_COMBO
		,FREEZE_COMBO
		,CLEAR_BTN
	}
	
	private JComboBox<MappingInfo> saveNameCombo;
	private JComboBox<String> codeCombo = new JComboBox<>();
	private JComboBox<String> columNmCombo = new JComboBox<>();
	private final JComboBox<String> freezeCombo = new JComboBox<String>(new String[] {"N","Y"});
	
	private JTextField nameTxt;
	private JTextArea sqlTxtA;
	private JButton sqlExecuteBtn;
	private JButton gridAddBtn;
	private JButton gridDelBtn;
	private JGrid mappingGrid;
	private JButton saveBtn;
	private JButton delBtn;
	private JButton clearBtn;
	private JButton ouputExcelBtn;
	
	private ProcessingModal modal = ProcessingModal.getInstance;
	
	
	public RegisterExcelExportForm(IRegisterExcelExportFormController model) {
	
		Alert.setParent(panel);
		initLayout();
		initBind();
		initEvent();
		
		this.model = model;
		this.model.setDefaultView(this);
	}
	
	private void initBind() {
		
		saveNameCombo = Table.getElementByName(NAMES.SAVED_NAME_COMBO,JComboBox.class);
		nameTxt = Table.getElementByName(NAMES.NAME_TXT,JTextField.class);
		sqlTxtA = Table.getElementByNameInJScrollPanel(NAMES.SQL_TXTA, JTextArea.class);

		sqlExecuteBtn = Table.getElementByName(NAMES.SQL_EXECUTE_BTN,JButton.class);
		gridAddBtn = Table.getElementByName(NAMES.GRID_ADD_BTN,JButton.class);
		gridDelBtn = Table.getElementByName(NAMES.GRID_DEL_BTN,JButton.class);
		
		mappingGrid = Table.getElementByNameInJScrollPanel(NAMES.MAPING_GRID, JGrid.class);

		saveBtn = Table.getElementByName(NAMES.SAVE_BTN,JButton.class);
		delBtn = Table.getElementByName(NAMES.DEL_BTN,JButton.class);
		clearBtn = Table.getElementByName(NAMES.CLEAR_BTN,JButton.class);
		ouputExcelBtn = Table.getElementByName(NAMES.OUTPUT_EXCEL_BTN,JButton.class);
		
		
		codeCombo.setEditable(true);
		columNmCombo.setEditable(true);
		
	}
	
	private void initEvent() {
		
		FileChoose.setParent(panel);
		
		saveNameCombo.addActionListener( e -> model.selectionChange(getSelectedItem()) );
		clearBtn.addActionListener(e -> {
				model.clearData();
				saveNameCombo.setSelectedIndex(-1);
			});
		
		
		sqlExecuteBtn.addActionListener(e -> {			
			modal.open(panel);
			new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					model.executeSql(sqlTxtA.getText()); 					
					return null;
				}

				@Override
				protected void done() {
					modal.close();
				}
			}.execute();	
		});
		gridAddBtn.addActionListener(e -> model.addRow(mappingGrid.getSelectedRow()));
		gridDelBtn.addActionListener(e -> model.removeRow(mappingGrid.getSelectedRow()));
		
		
		freezeCombo.addActionListener( e -> {
			if("Y".equals(freezeCombo.getSelectedItem())) 
				model.clearFreezePane();	
		});
		
		saveBtn.addActionListener(e -> {
			modal.open(panel);
			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {													
					model.save(getMappingInfoInUI());
					return null;
				}			

				@Override
				protected void done() {
					modal.close();											
				}
			}.execute();
		});
		delBtn.addActionListener(e -> model.remove());
		ouputExcelBtn.addActionListener(e -> FileChoose.EXCEL.save()
										.ifPresent(file -> {

											modal.open(panel);
											new SwingWorker<Void, Void>() {
												@Override
												protected Void doInBackground() throws Exception {													
													model.exportExcel(file, getMappingInfoInUI());
													return null;
												}			

												@Override
												protected void done() {
													modal.close();											
												}
											}.execute();			 
										})	
									);
		
	}
	
	private Optional<MappingInfo> getSelectedItem() {
		return Optional.ofNullable(saveNameCombo.getSelectedIndex() == -1 ? null:saveNameCombo.getItemAt(saveNameCombo.getSelectedIndex()));
	}
	
	private MappingInfo getMappingInfoInUI() {

		return MappingInfo
				.builder()
				.name(nameTxt.getText())

				.sql(sqlTxtA.getText())
				.build();
	}

	
	private void initLayout() {
		
		Container top = 
			new Table.Builder(new JPanel())
			.tr()
			.td(new TD.Builder(new JLabel("저장된 매핑명 : ")).build() )
			.td( 
					new TD.Builder(
							new Table.Builder(new JPanel())
							.tr()
							.td(new TD.Builder(NAMES.SAVED_NAME_COMBO, new JComboBox()).maxWidth().build())
							.td(new TD.Builder(NAMES.SAVE_BTN, new JButton("저장")).build() )
							.td(new TD.Builder(NAMES.DEL_BTN, new JButton("삭제")).build() )
							.td(new TD.Builder(NAMES.CLEAR_BTN, new JButton("초기화")).build() )
							.build()	
						 )
						 .maxWidth().build()
					)
			.tr()
			.td(new TD.Builder(new JLabel("매핑명 : ")).build() )
			.td(new TD.Builder(NAMES.NAME_TXT,new JTextField()).maxWidth().build() )
			.tr()
			.td(new TD.Builder(new JSeparator()).colspan(2).maxWidth().build())
			.tr()
			.td( new TD.Builder (
					new Table.Builder(new JPanel())
					.tr()
					.td( new TD.Builder(NAMES.SQL_TXTA,new JScrollPane(new JTextArea())).maxWidth().maxHeight().build()      )
					
					.build()	
				)
				.colspan(2).maxHeight().build()
			).build();
		
		
		Container bottom = 
				new Table.Builder(new JPanel())
				.tr()
				.td( 
					new TD.Builder(
						new Table.Builder(new JPanel())
						.tr()
						.td( new TD.Builder(NAMES.SQL_EXECUTE_BTN,new JButton("SQL실행")).top().build()      )
						.td( new TD.Builder(NAMES.GRID_ADD_BTN,new JButton("추가")).build()      )
						.td( new TD.Builder(NAMES.GRID_DEL_BTN,new JButton("삭제")).build()      )
						.build()	
					 )
					 .right().build()
				   )				
				.tr()
				.td(
					new TD.Builder(NAMES.MAPING_GRID,
							new JScrollPane(
									JGrid.builder()
										.colName("SQL컬럼명")
											.width(120)									
											.editorCell(new  DefaultCellEditor(columNmCombo))
										.nextCol()
										.colName("DB테이블").width(100).nextCol()
										.colName("컬럼명").width(150).nextCol()
										.colName("공통코드")
											.width(100)
											.editorCell(new  DefaultCellEditor(codeCombo))
										.nextCol()
										.colName("크기").width(1).nextCol()
										.colName("조건절")
											.width(1)
											.editorCell(new DefaultCellEditor(new JComboBox<String>(new String[] {"N","Y"})))
										.nextCol()
										.colName("필수")
											.width(1)
											.editorCell(new DefaultCellEditor(new JComboBox<String>(new String[] {"N","Y"})))
										.nextCol()
										.colName("틀고정")
										.width(5)
										.editorCell(new DefaultCellEditor(freezeCombo))
										.nextCol()
										.colName("수기입력")
										.width(1)
										.editorCell(new DefaultCellEditor(new JComboBox<String>(new String[] {"N","Y"})))
										.nextCol()
										.build()
							)
						)
						.heightRate(20)
						.build()
					)
				.tr()
				.td(new TD.Builder(new JSeparator()).maxWidth().build())
				.tr()
				.td( new TD.Builder(NAMES.OUTPUT_EXCEL_BTN , new JButton("엑셀출력")).right().build())
				.build();				
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,top,bottom);
		splitPane.setOneTouchExpandable(true);
		splitPane.resetToPreferredSizes();
		splitPane.setResizeWeight(0.5);
		new Table.Builder(panel)
		.addElement(NAMES.CODE_COMBO, codeCombo)
		.addElement(NAMES.COLUM_NM_COMBO, columNmCombo)
		.addElement(NAMES.FREEZE_COMBO, freezeCombo)
		.tr()
		.td(new TD.Builder( splitPane ).maxHeight().build() )
		.build();
		
	}
	
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		DataSource.INSTANCE.initConfig(
				DBInfo.builder()
				.jdbcUrl("jdbc:db2://nitdbd01:26500/DSNITT")
				.userName("egene")
				.password("itdb$D07")
				.build()
				);
		
		RegisterExcelExportFormModel model = new RegisterExcelExportFormModel(new MappingInfoDAO(),new CodeConfigDAO(),new SqlDAO());
		RegisterExcelExportForm reef = new RegisterExcelExportForm(model);

		
		frame.add(reef.getComponent());
		
		frame.setTitle("Excel Load Tool");
		frame.setSize(700,800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}

	@Override
	public Component getComponent() {
		return panel;
	}

	@Override
	public void initSavedMappingCombo(Vector<MappingInfo> list) {
		saveNameCombo.setModel(new DefaultComboBoxModel<>(list));
	}
	
	@Override
	public void displaySelectedMappingInfo(MappingInfo mapInfo) {
		nameTxt.setText(mapInfo.getName());
		sqlTxtA.setText(mapInfo.getSql());
		mappingGrid.setListData(mapInfo.getMappingList());
		columNmCombo.setModel(new DefaultComboBoxModel<>(mapInfo.getColumnList()) );
	}

	@Override
	public void setEnableMappingUI(boolean enable) {
		gridAddBtn.setEnabled(enable);
		gridDelBtn.setEnabled(enable);
		mappingGrid.setEnabled(enable);
		ouputExcelBtn.setEnabled(enable);
	}

	@Override
	public void clearMappingInfoUI() {
		
		saveNameCombo.setSelectedIndex(-1);
		saveNameCombo.updateUI();
		nameTxt.setText("");
		sqlTxtA.setText("");
		mappingGrid.setListData(new Vector<>());
	
	}

	@Override
	public void setModel(IRegisterExcelExportFormController model) {
		this.model = model;
		
	}

	@Override
	public void setCommonCodeComboInGrid(Vector<String> codeConfig) {
		codeCombo.setModel(new DefaultComboBoxModel<>(codeConfig));
	}

	@Override
	public void setEnableExecuteBtn(boolean enable) {
		sqlExecuteBtn.setEnabled(enable);
	}

	@Override
	public void updateUIMappingGrid() {
		mappingGrid.updateUI();
	}


	@Override
	public void clearSelectionMappingGrid() {
		mappingGrid.clearSelection();
	}
	
}
