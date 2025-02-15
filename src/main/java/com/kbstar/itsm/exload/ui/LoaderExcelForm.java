package com.kbstar.itsm.exload.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import com.kbstar.itsm.exload.controller.ILoaderExcelFormController;
import com.kbstar.itsm.exload.controller.LoaderExcelFormModel;
import com.kbstar.itsm.ui.util.FileChoose;
import com.kbstar.itsm.ui.util.ProcessingModal;
import com.kbstar.itsm.ui.util.TD;
import com.kbstar.itsm.ui.util.Table;

public class LoaderExcelForm implements ILoaderExcelForm{
	
	private JPanel panel  = new JPanel();	
	private JTextField fileName;
	private JButton findFileBtn;
	private JButton delFileBtn;
	private JComboBox<String> addSqlCombo;
	private JComboBox<String> sqlMode;
	private JTextArea outputSqlArea;
	private JButton outputBtn;
	private JButton outputFileBtn;
	private ILoaderExcelFormController model;
	private ProcessingModal modal = ProcessingModal.getInstance;
	
	public enum NAMES {
		FILE_NAME
		,FIND_FILE_BTN
		,DEL_FILE_BTN
		,SQL_MODE
		,OUTPUT_SQL_AREA
		,OUTPUT_BTN
		,OUTPUT_FILE_BTN
	}
	

	public LoaderExcelForm(ILoaderExcelFormController model) {		
		this.model = model;
		this.model.setView(this);
		initLayout();
		initBind();
		initEvent();		
	}

	private void initLayout() {
		
		new Table.Builder(panel)
		.tr()
		.td( new TD.Builder(new JLabel("로드파일명 : ")).build()  )
		.td( new TD.Builder(NAMES.FILE_NAME, new JTextField()).maxWidth().build()  )
		.td( new TD.Builder(
								new Table.Builder(new JPanel())
									.tr()
									.td(new TD.Builder(NAMES.FIND_FILE_BTN,new JButton("찾기")).build())
									.td(new TD.Builder(NAMES.DEL_FILE_BTN,new JButton("초기화")).build())
									.build()
							).build() 
		   )
		.tr()
		.td( new TD.Builder(new JLabel("SQL구분 : ")).build()  )
		.td( new TD.Builder(NAMES.SQL_MODE, new JComboBox<String>(new String[] {"UPDATE","INSERT","복구"})).colspan(2).build()  )
		.tr()
		.td( new TD.Builder(NAMES.OUTPUT_SQL_AREA,new JScrollPane(new JTextArea())).maxWidth().heightRate(10).colspan(3).build()  )
		.tr()
		.td( new TD.Builder(
				new Table.Builder(new JPanel())
					.tr()
					.td(new TD.Builder(NAMES.OUTPUT_BTN,new JButton("출력")).build())
					.td(new TD.Builder(NAMES.OUTPUT_FILE_BTN,new JButton("파일로저장")).build())
					.build()
			).colspan(3).center().build() 
			).build();
	}

	private void initBind() {
		fileName = Table.getElementByName(NAMES.FILE_NAME,JTextField.class);
		findFileBtn = Table.getElementByName(NAMES.FIND_FILE_BTN,JButton.class);
		delFileBtn = Table.getElementByName(NAMES.DEL_FILE_BTN,JButton.class);
		sqlMode = Table.getElementByName(NAMES.SQL_MODE,JComboBox.class);
		outputSqlArea = Table.getElementByNameInJScrollPanel(NAMES.OUTPUT_SQL_AREA, JTextArea.class);
		outputBtn = Table.getElementByName(NAMES.OUTPUT_BTN, JButton.class);
		outputFileBtn = Table.getElementByName(NAMES.OUTPUT_FILE_BTN, JButton.class);
		
		fileName.setEditable(false);
		outputSqlArea.setEditable(false);
		outputBtn.setEnabled(false);
		outputFileBtn.setEnabled(false);
	}
	
	private void initEvent() {
		FileChoose.setParent(panel);
		findFileBtn.addActionListener((ActionEvent e) -> model.selectedFile(FileChoose.EXCEL.open()));	
		delFileBtn.addActionListener((ActionEvent e) -> model.initLoadFileForm());
		outputBtn.addActionListener((ActionEvent e) -> {
						
			modal.open(panel);
			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					model.printSql(String.valueOf(sqlMode.getSelectedItem())); 					
					return null;
				}

				@Override
				protected void done() {
					modal.close();
				}
			}.execute();	
			
				
		});
		
		outputFileBtn.addActionListener((ActionEvent e) -> {
		
			modal.open(panel);
			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					model.saveFile(FileChoose.SQL.save()); 					
					return null;
				}

				@Override
				protected void done() {
					modal.close();
				}
			}.execute();
		});
	}
	
	
	
	public Component getComponent() {
		return panel;
	}
	
	
	
	@Override
	public void clearView() {
		fileName.setText("");			
		outputSqlArea.setText("");
		sqlMode.setSelectedIndex(0);
		outputBtn.setEnabled(false);
		outputFileBtn.setEnabled(false);
	}

	@Override
	public void selectFile(File file) {
		fileName.setText(file.getPath());
		outputBtn.setEnabled(true);
	}
	
	@Override
	public void outputSqlArea(String value) {		
		outputSqlArea.setText(value);
		outputFileBtn.setEnabled(true);
	}



	public static void main(String[] args) {
		JFrame frame = new JFrame();
		ILoaderExcelFormController model  = new LoaderExcelFormModel();
		LoaderExcelForm lef = new LoaderExcelForm(model);
		frame.add(lef.getComponent());
		frame.setTitle("Excel tes");
		frame.setSize(700,800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void viewFileName(File file) {
		fileName.setText(file.getPath());
		outputBtn.setEnabled(true);		
	}

}
