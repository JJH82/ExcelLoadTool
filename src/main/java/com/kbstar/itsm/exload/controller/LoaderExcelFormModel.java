package com.kbstar.itsm.exload.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.channels.FileChannel;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;

import com.kbstar.itsm.excel.loader.ExcelLoader;
import com.kbstar.itsm.excel.loader.SqlMode;
import com.kbstar.itsm.exload.ui.ILoaderExcelForm;
import com.kbstar.itsm.ui.util.Alert;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class LoaderExcelFormModel implements ILoaderExcelFormController {
	
	private ILoaderExcelForm view;
	Optional<File> loadFile = Optional.empty();
	Optional<File> saveFile = Optional.empty();
	private String sqlData;
	
	@Override
	public void setView(ILoaderExcelForm view) {
		this.view = view;
		
	}	

	@Override
	public void printSql(String sqlMode) {

		String sql ="";
		try {
			sql =  ExcelLoader.loader(loadFile.get(), SqlMode.getSqlMode(sqlMode));
		} catch ( IOException | InvalidFormatException e) {
			Alert.ERROR.show(e.toString());
			return;
		}
		
		sqlData = sql;
		view.outputSqlArea(sql);
	}

	@Override
	public void saveFile(Optional<File> saveFile)  {
		
		try {

			String line;
			int count = 0;
			int fileCount = 1;
			
			String path = saveFile.map(File::getParent).orElse("c:") + File.separator;
			String fileName = saveFile.map(File::getName).orElse("c:\\loader.sql");		
			String extension = "."+StringUtils.substringAfter(fileName, ".");
			fileName = StringUtils.substringBefore(fileName, ".");

			File file = new File(path+fileName + "_" + fileCount + extension);
			PrintWriter out = new PrintWriter(file);			
			
			BufferedReader reader = new BufferedReader(new StringReader(sqlData));
			
			while((line = reader.readLine()) != null) {
				out.println(line);
				count++;
				
				if(count % 200 == 0) {
					out.close();
					count = 0;
					fileCount++;
					file = new File(path + fileName + "_" + fileCount + extension);
					out = new PrintWriter(file);
					
				}
			}
			out.close();
			
			if(file.length() ==0) file.delete();
			
		}catch(Exception e) {
			Alert.ERROR.show(e.toString());
		}
		
	}
	
	
	private void saveFile(File saveFile) {
		
		try (PrintWriter out = new PrintWriter(saveFile))
		{
			out.print(sqlData);
		} catch (FileNotFoundException e) {
			Alert.ERROR.show(e.toString());
		}		
	}

	@Override
	public void findLoadFile(Optional<File> loadFile) {
		this.loadFile = loadFile;
	}

	@Override
	public void initLoadFileForm() {
		loadFile = Optional.empty();
		saveFile = Optional.empty();
		
		view.clearView();
	}

	@Override
	public void selectedFile(Optional<File> selectedFile) {
		this.loadFile = selectedFile;
		this.loadFile.ifPresent(view::viewFileName);
	}


}