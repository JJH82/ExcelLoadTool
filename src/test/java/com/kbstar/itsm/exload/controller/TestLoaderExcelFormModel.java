package com.kbstar.itsm.exload.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kbstar.itsm.excel.loader.SqlMode;
import com.kbstar.itsm.exload.ui.ILoaderExcelForm;

public class TestLoaderExcelFormModel {
	
	private ILoaderExcelForm view = mock(ILoaderExcelForm.class);
	private LoaderExcelFormModel model;
	
	@BeforeEach
	public void setup() {
		model = new LoaderExcelFormModel();
		model.setView(view);
	}
	
	@Test
	public void test초기화버튼클릭시() {		
		//given
		model.loadFile = Optional.of(new File(""));
		model.saveFile = Optional.of(new File(""));
		
		// when
		model.initLoadFileForm();
		
		//then
		assertFalse(model.loadFile.isPresent());
		assertFalse(model.saveFile.isPresent());
		verify(view).clearView();		
	}
	
	
	@Test
	public void test찾기클릭시() {
		
		// when
		model.selectedFile(Optional.of(new File("")));
		
		// then
		verify(view).viewFileName(any());		
	}
	
	@Test
	public void test출력버튼클릭시() {
	
		// given
		String filePath = System.getProperties().get("user.dir") + File.separator+"src\\test\\java\\com\\kbstar\\itsm\\excelFile\\TestFile.xlsx";		
		model.loadFile = Optional.of( new File(filePath));
				
		// when
		model.printSql("INSERT");
		
		// then
		verify(view).outputSqlArea(any());
		
	}
	
	
	
	
}
