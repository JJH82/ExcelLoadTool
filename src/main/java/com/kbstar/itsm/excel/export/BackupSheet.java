package com.kbstar.itsm.excel.export;

import static com.kbstar.itsm.excel.SheetNames.*;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbstar.itsm.excel.export.data.ColumnInfo;
import com.kbstar.itsm.excel.export.data.WorkbookInfo;


import lombok.Getter;

@Getter
class BackupSheet {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private SXSSFSheet backupSheet;
	private List<ColumnInfo> columnInfoList;
	private WorkbookInfo  workbookInfo;
	private SXSSFWorkbook workbook;
	private CellStyles styles;
	
	BackupSheet(ValidSheet vaildSheet) throws JsonProcessingException {
		
		this.workbook = vaildSheet.getWorkbook();

		this.backupSheet = this.workbook.createSheet(BACKUP_SHEET.toString());

		this.workbook.setSheetOrder(this.backupSheet.getSheetName(), 3);
		
		this.columnInfoList = vaildSheet.getColumnInfoList();
		this.workbookInfo = vaildSheet.getWorkbookInfo();
		
		styles = new CellStyles(this.workbook);
				
		writeHeadData();
		writeData();
		
	
		this.workbook.setSheetHidden(this.workbook.getSheetIndex(backupSheet), true);

		
	}
	
	void writeHeadData() throws JsonProcessingException {
		
		Row oneRow =  CellUtil.getRow(0, backupSheet);
		Row towRow =  CellUtil.getRow(1, backupSheet);
		
		
		
		int column = 0;
		for(ColumnInfo sheetInfo: workbookInfo.getColumnInfoList()) {
			
			backupSheet.setColumnWidth(column, sheetInfo.getSize()*256+156);
			
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
				Cell cell = CellUtil.getCell(CellUtil.getRow(rownum+2, backupSheet), sheetInfo.getColIdx());
				cell.setCellStyle(styles.getLockText());
				cell.setCellValue(sheetInfo.getDataList().get(rownum));
			}
		}
		
	}

}
