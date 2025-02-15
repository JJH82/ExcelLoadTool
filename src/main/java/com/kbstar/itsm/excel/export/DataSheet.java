package com.kbstar.itsm.excel.export;

import static com.kbstar.itsm.excel.SheetNames.DATA_SHEET;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbstar.itsm.excel.export.data.ColumnInfo;
import com.kbstar.itsm.excel.export.data.WorkbookInfo;

class DataSheet {
	
	private ObjectMapper mapper = new ObjectMapper();;
	
	private SXSSFWorkbook workbook;
	private WorkbookInfo workbookInfo;
	private SXSSFSheet sheet;
	private CellStyles styles;
	
	SXSSFWorkbook getWorkbook() {
		return workbook;
	}

	WorkbookInfo getWorkbookInfo() {
		return workbookInfo;
	}

	 SXSSFSheet getSheet() {
		return sheet;
	}

	DataSheet(SXSSFWorkbook workbook, WorkbookInfo workbookInfo ) throws IOException {
		this.workbook = workbook;
		this.workbookInfo = workbookInfo;
		
		sheet = this.workbook.createSheet(DATA_SHEET.toString());
		this.workbook.setSheetOrder(sheet.getSheetName(), 0);


		styles = new CellStyles(this.workbook);
		
		writeHeadData();
		writeData();
		
		lockSheet();	
		sheetStyle();
	}
	
	
	
	void sheetStyle() {

		workbookInfo.getColumnInfoList()
					.stream()
					.filter( o -> o.getFreezeColumnIdx() > 0)
					.findFirst()
					.ifPresent(o -> sheet.createFreezePane(o.getFreezeColumnIdx(), 2));
		
		sheet.setAutoFilter(new CellRangeAddress(1,1,0, workbookInfo.getColumnInfoList().size()-1 ));
	}
	
	
	void lockSheet() {
		sheet.protectSheet("jjh");
		sheet.lockFormatColumns(false);
		sheet.lockAutoFilter(false);
		sheet.lockObjects(false);
		sheet.lockSelectLockedCells(false);	
		sheet.enableLocking();
	}

	private CellStyle getDataStyle(boolean isLock) {
		
		return isLock ? styles.getLockText() : styles.getUnlockText();
	}
	
	void writeHeadData() throws JsonProcessingException {
		
		Row oneRow =  CellUtil.getRow(0, sheet);
		Row towRow =  CellUtil.getRow(1, sheet);
		
		oneRow.setZeroHeight(true);
		
		int column = 0;
		for(ColumnInfo sheetInfo: workbookInfo.getColumnInfoList()) {
			
			sheet.setColumnWidth(column, sheetInfo.getSize()*256+156);
			
			Cell oneRowcell = CellUtil.getCell(oneRow, column);
			Cell towRowcell = CellUtil.getCell(towRow, column);
			
			oneRowcell.setCellValue( mapper.writeValueAsString(sheetInfo.getMataData()) );
			towRowcell.setCellValue( sheetInfo.getSqlColumnName()    );
			
			column++;
		}
	}
	
	
	void writeData() {
						
		for(int rownum = 0;  rownum < workbookInfo.getDataRowCount(); rownum++) {
			for(ColumnInfo sheetInfo: workbookInfo.getColumnInfoList()) {
				
				Cell cell = CellUtil.getCell(CellUtil.getRow(rownum+2, sheet), sheetInfo.getColIdx());
				cell.setCellStyle(getDataStyle(sheetInfo.isWhere()));
				cell.setCellValue(sheetInfo.getDataList().get(rownum));
								
			}
			
		}
		
	}
	
}
