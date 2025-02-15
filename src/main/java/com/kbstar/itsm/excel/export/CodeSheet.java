package com.kbstar.itsm.excel.export;

import static com.kbstar.itsm.excel.SheetNames.*;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.kbstar.itsm.excel.export.data.ColumnInfo;
import com.kbstar.itsm.excel.export.data.CommonCodeInfo;
import com.kbstar.itsm.excel.export.data.WorkbookInfo;

class CodeSheet {
	
	private SXSSFWorkbook workbook;
	private WorkbookInfo workbookInfo;
	private SXSSFSheet codeSheet;

		
	SXSSFWorkbook getWorkbook() {
		return workbook;
	}

	WorkbookInfo getWorkbookInfo() {
		return workbookInfo;
	}

	 CodeSheet(DataSheet dataSheet) {
		this.workbook = dataSheet.getWorkbook();
		this.codeSheet = this.workbook.createSheet(CODE_SHEET.toString());
		this.workbook.setSheetOrder(this.codeSheet.getSheetName(), 1);
		this.workbookInfo =	dataSheet.getWorkbookInfo();
		
		//컬럼  데이터 비례 너비정보 추적
		codeSheet.trackAllColumnsForAutoSizing();
		
		lockSheet();
		writeHeadData();
		writeData();
		applyDataValidationConstraint();		
	}
	
	void lockSheet() {
		codeSheet.protectSheet("exjjh");
		codeSheet.lockFormatColumns(false);
		codeSheet.lockAutoFilter(false);
		codeSheet.lockObjects(false);
		codeSheet.lockSelectLockedCells(false);	
		codeSheet.enableLocking();
		
	}
	
	 void writeHeadData() {
		
		Row oneRow =  CellUtil.getRow(0, codeSheet);
		
		int column = 0;
		for(String codeName: workbookInfo.getCommonCodeInfoMap().keySet()) {
			
			Cell oneRowcell = CellUtil.getCell(oneRow, column);
			
			oneRowcell.setCellValue( codeName );
			
			column++;
		}
	}
	
	
	void writeData() {
		
		 int maxRowCount = workbookInfo
						 .getCommonCodeInfoMap()
						 .values()
						 .stream()
						 .map(CommonCodeInfo::getList)
						 .map(List::size)
						 .max(Integer::compareTo)
						 .orElse(0)		 
		 ;
		 
		
		for(int rownum = 0;  rownum < maxRowCount; rownum++) {
			
			int column = 0;
			for(String codeName: workbookInfo.getCommonCodeInfoMap().keySet()) {
				
				List<String> codeValueList = workbookInfo.getCommonCodeInfoMap().get(codeName).getList();
				
				if(rownum >= codeValueList.size() ) {
					column++;
					continue; 
				}

				Cell cell = CellUtil.getCell(CellUtil.getRow(rownum + 1, codeSheet), column++);
				cell.setCellValue(codeValueList.get(rownum));
			}
		}
		
		for (int i =0 ; i<  workbookInfo.getCommonCodeInfoMap().keySet().size(); i++ ) codeSheet.autoSizeColumn(i);
	}
	
	void applyDataValidationConstraint() {
		
		SXSSFSheet dataSheet = workbook.getSheet(DATA_SHEET.toString());
		 
		DataValidationHelper dvHelper = dataSheet.getDataValidationHelper();
		
		
		for(ColumnInfo info:  workbookInfo.getColumnInfoList()) {
			
			if (info.getCodeName().isEmpty()) continue;
			
			CommonCodeInfo codeInfo = workbookInfo.getCommonCodeInfoMap().get(info.getCodeName());
			
			CellRangeAddressList cellAddrList = new CellRangeAddressList();
			cellAddrList.addCellRangeAddress(info.getDataAddr());
			
			DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(codeInfo.getCellAddr().formatAsString("코드", true));
			DataValidation validation = dvHelper.createValidation(dvConstraint, cellAddrList);
				
				
			validation.setShowErrorBox(true);
			validation.setEmptyCellAllowed(true);
				
			dataSheet.addValidationData(validation);				
		}
			
	}
	
	
}
