package com.kbstar.itsm.exload.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import com.kbstar.itsm.exload.controller.IPublicCodeEnrollmentController;
import com.kbstar.itsm.exload.controller.PublicCodeEnrollmentModel;
import com.kbstar.itsm.exload.dao.CodeConfigDAO;
import com.kbstar.itsm.exload.dao.CodeValueDAO;
import com.kbstar.itsm.exload.jdbc.DataSource;
import com.kbstar.itsm.exload.model.CodeConfig;
import com.kbstar.itsm.exload.model.DBInfo;
import com.kbstar.itsm.ui.model.MapModel;
import com.kbstar.itsm.ui.util.Alert;
import com.kbstar.itsm.ui.util.ProcessingModal;
import com.kbstar.itsm.ui.util.TD;
import com.kbstar.itsm.ui.util.Table;

public class PublicCodeEnrollment implements IPublicCodeEnrollment {

	private JPanel panel;
	
	private JComboBox<CodeConfig> savedCodeNameCombo;
	private JTextField codeNameTxt;
	private JTextArea sqlTxtA;
	private JTextField paramsTxt;
	
	private JButton executeBtn;
	private JButton saveBtn;
	private JButton deleteBtn;
	
	private JTable codeListGrid;
	private ProcessingModal modal = ProcessingModal.getInstance;
	
	
	
	public static enum NAMES {
		PCE_VIEW,
		CODE_NAME,
		SQL,
		PARMS,
		EXECUTE,
		SAVE,
		DELETE,
		CODE_LIST_GRID,
		SAVED_CODE_NAME;
	};
	
	private IPublicCodeEnrollmentController model;
	
	public PublicCodeEnrollment(IPublicCodeEnrollmentController model) {
		panel = new JPanel();
		Alert.setParent(panel);
		
		enrollmentLayout();		
		initBind();
		initEvent();
		
		this.model = model;
		this.model.setDefaultView(this);
		
	}
	
	private void enrollmentLayout() {

		
		new Table
			.Builder(NAMES.PCE_VIEW, panel)
			.tr()
			.td(new TD.Builder(new JLabel("저장된 코드명")).build())
			.td(new TD.Builder(NAMES.SAVED_CODE_NAME,new JComboBox<String>()).maxWidth().build())
			.tr()
			.td(new TD.Builder(new JLabel("코드명")).build())
			.td(new TD.Builder(NAMES.CODE_NAME,new JTextField()).maxWidth().build())
			.tr()
			.td(new TD.Builder(new JLabel("SQL")).build())
			.td(new TD.Builder(NAMES.SQL,new JScrollPane(new JTextArea())).maxWidth().maxHeight().build() )
			.tr()
			.td(new TD.Builder(new JLabel("파라미터")).build())
			.td(new TD.Builder(NAMES.PARMS,new JTextField()).maxWidth().build())
			.tr()
			.td(new TD.Builder(
					new Table
						.Builder( new JPanel())
						.tr()
						.td(new TD.Builder(NAMES.EXECUTE, new JButton("실행")).build())
						.td(new TD.Builder(NAMES.SAVE, new JButton("저장")).build())
						.td(new TD.Builder(NAMES.DELETE, new JButton("삭제")).build())
						.build())
						.colspan(6).center().build())
			.tr()
			.td(
				new TD.Builder( NAMES.CODE_LIST_GRID
							  , new JScrollPane(new JTable(MapModel.builder().build()))
							  )
				.colspan(3)
				.maxWidth()
				.maxHeight()
				.build()
			).build();
			
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
		
		codeListGrid = Table.getElementByNameInJScrollPanel(NAMES.CODE_LIST_GRID, JTable.class);
	}
	
	
	public void initSavedMappingCombo(Vector<CodeConfig> list) {
		savedCodeNameCombo.setModel(new DefaultComboBoxModel<>(list));
	}
	
	private void initEvent() {
		savedCodeNameCombo.addActionListener((ActionEvent e) -> model.selectionChange(selectedSavedCodeNameComboItem()) );
		deleteBtn.addActionListener((ActionEvent e) -> model.remove());
		saveBtn.addActionListener((ActionEvent e) -> model.save(getCodeConfigUI()));

		

		executeBtn.addActionListener((ActionEvent e) -> {

			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					 modal.open(panel);
					 model.execute(getCodeConfigUI());				    
					return null;
				}			

				@Override
				protected void done() {
					modal.close();
			
				}
			}.execute();			 
			
		});
	}	

	
	
	private Optional<CodeConfig> selectedSavedCodeNameComboItem() {
		return Optional.ofNullable(savedCodeNameCombo.getItemAt(savedCodeNameCombo.getSelectedIndex()));
	}

	@Override
	public CodeConfig getCodeConfigUI() {
				
		return CodeConfig.builder()
				.name(codeNameTxt.getText())
				.sql(sqlTxtA.getText())
				.parameters(paramsTxt.getText())
				.list(((MapModel)codeListGrid.getModel()).getData())
				.build();
	}
	
	@Override
	public void displayCodeConfigUI(CodeConfig codeConfig) {
		
		codeNameTxt.setText(codeConfig.getName());
		sqlTxtA.setText(codeConfig.getSql());
		paramsTxt.setText(codeConfig.getParameters());
		displayCodeListGridData(codeConfig.getList());		
	}

	@Override
	public void clearCodeConfigUI() {
		codeNameTxt.setText("");
		sqlTxtA.setText("");
		paramsTxt.setText("");
		savedCodeNameCombo.setSelectedIndex(-1);
		codeListGrid.setModel(MapModel.builder().build());
	}

	@Override
	public void updateUI() {
		savedCodeNameCombo.updateUI();
	}

	@Override
	public void displaySavedCodeNameComboData(Vector<CodeConfig> list) {
		savedCodeNameCombo.setModel(new DefaultComboBoxModel<CodeConfig>(list));		
	}
	
	@Override
	public void displayCodeListGridData(Vector<Map<String, String>> list) {
		codeListGrid.setModel(MapModel.builder()
							  		  .dataList(list)
							  		  .build()
							  );
	}
	
	public static void main(String[] args) {
		DataSource.INSTANCE.initConfig(egeneDBInfo());
		
		IPublicCodeEnrollmentController pcec = new PublicCodeEnrollmentModel(new CodeConfigDAO(), new CodeValueDAO());
		
		new PublicCodeEnrollment(pcec);
		
	}
	
	public static DBInfo egeneDBInfo() {
		
		return DBInfo.builder()
		.connectName("egene DB")
		.jdbcUrl("jdbc:db2://nitdbd01:26500/DSNITT")
		.userName("egene")
		.password("itdb$C03")
		.build();
	}

	@Override
	public Component getComponent() {
		return panel;
	}


}
