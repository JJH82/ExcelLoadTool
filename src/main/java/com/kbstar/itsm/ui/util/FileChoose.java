package com.kbstar.itsm.ui.util;

import java.awt.Component;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public enum FileChoose {
	
	
	
	EXCEL {
		@Override
		public FileType createFileType() {
			FileType fileType = new FileType();
			fileType.extension = new FileNameExtensionFilter("*.xls,*.xlsx", "xls","xlsx");
			fileType.openTitle = "Excel파일 열기";
			fileType.saveTitle = "Excel파일 저장";
			new File("export").mkdir();
			fileType.filePath=new File(new File("export").getAbsoluteFile()+File.separator + toYYYYMMDD() +"_export.xlsx");
			
			return fileType;
		}
	},
	SQL {

		@Override
		public FileType createFileType() {			
			FileType fileType = new FileType();
			fileType.extension = new FileNameExtensionFilter("*.sql,*.txt", "sql","txt");
			fileType.openTitle = "SQL파일 열기";
			fileType.saveTitle = "SQL파일 저장";
			new File("export").mkdir();
			fileType.filePath=new File(new File("export").getAbsoluteFile()+File.separator + toYYYYMMDD()+"_export.sql");
			
			return fileType;
		}
		
	};
	
	FileType fileType;
	JFileChooser fileChooser = new JFileChooser();
	static Component parent;
	
	private FileChoose() {
		fileType = createFileType();

		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.removeChoosableFileFilter(fileChooser.getChoosableFileFilters()[0]);
		fileChooser.addChoosableFileFilter(fileType.extension);
	}
	
	private static String toYYYYMMDD() {		
		return LocalDate.now().format(DateTimeFormatter.ofPattern("YYYYMMdd"));
	}
	
	public Optional<File> open() {
		fileChooser.setDialogTitle(fileType.openTitle);
		fileChooser.setApproveButtonText("선택");
		fileChooser.setSelectedFile(fileType.filePath);

		return getSelectedFile(); 
	}
	
	
	public Optional<File> save() {
		fileChooser.setDialogTitle(fileType.saveTitle);
		fileChooser.setApproveButtonText("선택");
		fileChooser.setSelectedFile(fileType.filePath);
		
		return getSelectedFile();
	}
	
	public static void setParent(Component parent) {
		FileChoose.parent = parent;
	}

	private Optional<File> getSelectedFile() {
		return Optional.ofNullable(fileChooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION ? null :fileChooser.getSelectedFile() );
	}
	
	
	public abstract FileType createFileType();
	
	/**
	 * 내부클래스는 private, get, set을 사용할 필요가 없다.
	 * 이팩티브 자바책.. 참조
	 */
	private class FileType {
		FileNameExtensionFilter extension; 
		String openTitle;
		String saveTitle;
		File filePath;

	}
	
	
	public static void main(String[] args) {
		 FileChoose.SQL.save();
		
	}

}
