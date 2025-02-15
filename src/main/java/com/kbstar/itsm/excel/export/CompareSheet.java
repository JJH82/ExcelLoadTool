package com.kbstar.itsm.excel.export;

import static com.kbstar.itsm.excel.SheetNames.BACKUP_SHEET;
import static com.kbstar.itsm.excel.SheetNames.COMPARE_SHEET;
import static com.kbstar.itsm.excel.SheetNames.DATA_SHEET;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.kbstar.itsm.excel.export.data.ColumnInfo;
import com.kbstar.itsm.excel.export.data.WorkbookInfo;

class CompareSheet {
	
	private SXSSFSheet compareSheet;
	private List<ColumnInfo> columnInfoList;
	private SXSSFWorkbook workbook;
	private WorkbookInfo  workbookInfo;
	
	
	CompareSheet(BackupSheet backupSheet) {
		this.workbook = backupSheet.getWorkbook();
		this.compareSheet = workbook.createSheet(COMPARE_SHEET.toString());
		this.workbook.setSheetOrder(this.compareSheet.getSheetName(), 4);
		this.columnInfoList = backupSheet.getColumnInfoList();
		this.workbookInfo = backupSheet.getWorkbookInfo();
		
		sheetStyle();
		lockSheet();
		writeHeadData();
		writeData();
		
	}
	
	
	void lockSheet() {
		compareSheet.protectSheet("jjh");
		compareSheet.lockFormatColumns(false);
		compareSheet.lockAutoFilter(false);
		compareSheet.lockObjects(false);
		compareSheet.lockSelectLockedCells(false);	
		compareSheet.enableLocking();
		
	}
	
	
	
	protected void sheetStyle() {
		//A1  틀고정
		compareSheet.createFreezePane(0, 2);
		
		//데이터검증컬럼 자동필터
		compareSheet.setAutoFilter(new CellRangeAddress(1, 1, 0, 0));
		
	}
	
	protected void writeDataValid() {
		
		int column = 0;
		 
		Row oneRow =  CellUtil.getRow(1, compareSheet);
		Cell cell = CellUtil.getCell(oneRow, column);
		compareSheet.setColumnWidth(column, 23*256+156);

		cell.setCellValue("데이터검증");
		
		for (int rownum = 0 ; rownum < workbookInfo.getDataRowCount() ; rownum++) {
			
			Row row =  CellUtil.getRow(rownum+2, compareSheet);
			cell = CellUtil.getCell(row, column);
			
			cell.setCellFormula(createIsModifyFormula(cell));
			
		}
			
	}
	
	
	protected void writeHeadData() {
		
		CellUtil.getRow(0, compareSheet).setZeroHeight(true);
		
		int column = 0;
		Row row =  CellUtil.getRow(1, compareSheet);
		Cell cell = CellUtil.getCell(row, column);
		compareSheet.setColumnWidth(column, 23*256+156);
		cell.setCellValue("데이터검증");

		
		for(ColumnInfo columnInfo: columnInfoList) {
			column++;
			compareSheet.setColumnWidth(column, 35*256+156);
			Cell oneRowcell = CellUtil.getCell(row, column);
			oneRowcell.setCellValue( columnInfo.getSqlColumnName() );
		}
	}
	
	
	private String createIsModifyFormula(Cell cell) {
		
		CellRangeAddress cellAddr = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), 1, columnInfoList.size());
		StringBuilder sb = new StringBuilder();
		sb.append("IF(COUNTIF(")
		.append(cellAddr.formatAsString())
		.append(",\"*->*\") > 0,\"수정\",\"\")");

		return sb.toString();
		
	}
	
	
	protected void writeData() {
		
		for (int rownum = 0 ; rownum < workbookInfo.getDataRowCount(); rownum++) {
			
			int cellnum = 0;
			for(ColumnInfo columnInfo: workbookInfo.getColumnInfoList()) {
				
				Cell cell = CellUtil.getCell(CellUtil.getRow(rownum+2, compareSheet), columnInfo.getColIdx()+1 );
				
				if(columnInfo.getColIdx() == 0 ) {
					CellUtil
					.getCell(CellUtil.getRow(rownum+2, compareSheet),0)
					.setCellFormula(createIsModifyFormula(cell));
				}
									
				CellRangeAddress cellAddr = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex()-1, cell.getColumnIndex()-1);	
				cell.setCellFormula(createCompareBackupFormula(cellAddr));				
				
				/*
				Hyperlink link = workbook.getCreationHelper().createHyperlink(HyperlinkType.DOCUMENT);
				link.setAddress(cellAddr.formatAsString(DataSheet.NAME, false));
				cell.setHyperlink(link);
					*/
			}
			
		}
	}
	
	private String createCompareBackupFormula(CellRangeAddress cellAddr) {
		
		return new StringBuilder()
				  .append("IF(")
				  .append(cellAddr.formatAsString(BACKUP_SHEET.toString(), false)).append("=").append(cellAddr.formatAsString(DATA_SHEET.toString(), false))
				  .append(",\"\",")
				  .append(cellAddr.formatAsString(BACKUP_SHEET.toString(), false)).append("&\"->\"&").append(cellAddr.formatAsString(DATA_SHEET.toString(), false)).append(")")
				  .toString();
	}
	
}
