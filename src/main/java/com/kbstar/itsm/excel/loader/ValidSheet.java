package com.kbstar.itsm.excel.loader;

import static com.kbstar.itsm.excel.SheetNames.VAILD_SHEET;

import java.util.Locale;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class ValidSheet {
	
	private XSSFSheet sheet;
	private static int START_ROW = 1; //A1 시작
	private static int COLUMN_A = 0;  //A 열
	
	ValidSheet(XSSFWorkbook workbook) {
		sheet = workbook.getSheet(VAILD_SHEET.toString());
	}
	
	void checkSheetPassword() throws InvalidFormatException {
		if(!sheet.validateSheetPassword("jjh")) throw new InvalidFormatException("업로드 엑셀폼양식이 아닙니다.\n업로드 엑셀 양식을 다시 받아주세요.");
	}

	void vaildate() throws  InvalidFormatException {
		
		for(int rownum = START_ROW ; rownum < sheet.getLastRowNum(); rownum++) {

			Row row = sheet.getRow(rownum);			
			Cell cell = row.getCell(COLUMN_A);
			
			if(CellType.ERROR.equals(cell.getCellType()) ) throw new InvalidFormatException("엑셀시트에서 잘라내기 기능사용금지!\n엑셀양식을 다시 다운로드 받으세요!");
			if(!cell.getStringCellValue().isEmpty()) throw new InvalidFormatException( VAILD_SHEET.toString() +"!"+cell.getAddress().formatAsString() +"셀을 확인 부탁드립니다.");

		}
	}
}
