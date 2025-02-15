package com.kbstar.itsm.excel.loader;

import static com.kbstar.itsm.excel.SheetNames.COMPARE_SHEET;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class CompareSheet {
	
	private XSSFSheet sheet;
	private static int START_ROW = 2; //A1 시작
	private static int COLUMN_A = 0;  //A 열
	
	CompareSheet(XSSFWorkbook workbook) {
		sheet = workbook.getSheet(COMPARE_SHEET.toString());
	}
	
	
	 List<Integer> getModifiedRowList() {
				
		List<Integer> list = new ArrayList<>();

		for(int rownum = START_ROW ; rownum <= sheet.getLastRowNum(); rownum++) {	
			Row row = sheet.getRow(rownum);
			Cell cell = row.getCell(COLUMN_A);
			
			if(!cell.getStringCellValue().isEmpty()) list.add(rownum);
			
		}
		return list;
	}
	

}
