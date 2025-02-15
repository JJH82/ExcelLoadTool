package com.kbstar.itsm.excel.export;


import static com.kbstar.itsm.excel.SheetNames.CODE_SHEET;
import static com.kbstar.itsm.excel.SheetNames.DATA_SHEET;
import static com.kbstar.itsm.excel.SheetNames.VAILD_SHEET;
import static com.kbstar.itsm.excel.SheetNames.BACKUP_SHEET;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.kbstar.itsm.excel.export.data.ColumnInfo;
import com.kbstar.itsm.excel.export.data.WorkbookInfo;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
class ValidSheet {
	
	private SXSSFWorkbook workbook;
	private WorkbookInfo workbookInfo;
	private SXSSFSheet validSheet;
	private List<ColumnInfo> fiteredColumnInfoList;
	private List<ColumnInfo> columnInfoList;
	private CellStyles styles;
 
	
	

	ValidSheet(CodeSheet codeSheet) {
		this.workbook = codeSheet.getWorkbook();
		this.workbookInfo = codeSheet.getWorkbookInfo();
		this.validSheet  = this.workbook.createSheet(VAILD_SHEET.toString());
		this.workbook.setSheetOrder(this.validSheet.getSheetName(), 2);
		this.styles = new CellStyles(this.workbook);
		
		this.fiteredColumnInfoList = this.workbookInfo
									.getColumnInfoList()
									.stream()
									.filter(o -> o.isRequired() || !o.getCodeName().isEmpty() )
									.collect(toList());
		
		this.columnInfoList = this.workbookInfo.getColumnInfoList();
		
		sheetStyle();
		lockSheet();
		writeHeadData();
		writeData();
		
	}
	


	void lockSheet() {
		validSheet.protectSheet("jjh");
		validSheet.lockFormatColumns(false);
		validSheet.lockAutoFilter(false);
		validSheet.lockObjects(false);
		validSheet.lockSelectLockedCells(false);	
		validSheet.enableLocking();
		
	}
	
	
	void sheetStyle() {
		//A1  틀고정
		validSheet.createFreezePane(0, 1);
		
		//데이터검증컬럼 자동필터
		validSheet.setAutoFilter(new CellRangeAddress(0, 0, 0, 0));
		
	}
	
	
	private String createDataValidFormula(Cell cell) {
		
		CellRangeAddress cellAddr = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), 1, fiteredColumnInfoList.size());
		StringBuilder sb = new StringBuilder();
		sb.append("IF(COUNTIF(")
		.append(cellAddr.formatAsString())
		.append(",\"*!\"),")
		.append("COUNTIF(")
		.append(cellAddr.formatAsString())
		.append(",\"*!\")&\"건 이상 검증 확인 필요\",\"\")");
		return sb.toString();
		
	}
	
	
	
	
	void writeHeadData() {
		
		int column = 0;
		Row oneRow =  CellUtil.getRow(0, validSheet);

		Cell oneRowcell = CellUtil.getCell(oneRow, column);
		validSheet.setColumnWidth(column, 23*256+156);
		
		oneRowcell.setCellValue("데이터검증");
		
		
		for(ColumnInfo columnInfo: fiteredColumnInfoList) {
			
			column++;
			
			validSheet.setColumnWidth(column, 35*256+156);
			
			oneRowcell = CellUtil.getCell(oneRow, column);
			
			oneRowcell.setCellValue( columnInfo.getSqlColumnName() );
		}
	}
	
	void writeData() {
		
		if(fiteredColumnInfoList.size() == 0) return;
		
		for (int rownum = 0 ; rownum < workbookInfo.getDataRowCount(); rownum++) {	
			
			int column = 0;
			
			for(ColumnInfo columnInfo: fiteredColumnInfoList) {
				
				CellRangeAddress cellAddr = columnInfo.getCellAddressList().get(rownum);				
				Cell cell = CellUtil.getCell(CellUtil.getRow(rownum+1, validSheet), column+1);
			
				if(column == 0 ) {										
					Cell cellA =  CellUtil.getCell(CellUtil.getRow(rownum+1, validSheet), column);
					cellA.setCellStyle(styles.getLockText());
					cellA.setCellFormula(createDataValidFormula(cell));									 
				}

				cell.setCellFormula(createFormula(cellAddr,columnInfo));

				Hyperlink link = workbook.getCreationHelper().createHyperlink(HyperlinkType.DOCUMENT);
				link.setAddress(cellAddr.formatAsString(DATA_SHEET.toString(), false));
				cell.setHyperlink(link);

				
				column++;	
			}
		}		
	}
	
	
	private String createFormula(CellRangeAddress cell, ColumnInfo columnInfo) {
		
		StringBuilder sb = new StringBuilder();
		String dataCellRef = cell.formatAsString(DATA_SHEET.toString(), false);
		
		
		// 필수 그리고 공통코드없음
		if(columnInfo.isRequired() && columnInfo.getCodeName().isEmpty()) {
			sb.append("IF(LENB(TRIM(")
			.append(dataCellRef).append("))=0,\"")
			.append(dataCellRef).append("필수 입력입니다!\",\"\")");

		// 필수아니고 그리고 공통코드없음
		}else if(!columnInfo.isRequired() && !columnInfo.getCodeName().isEmpty()) {
			sb.append("IF(LENB(TRIM(")
			.append(dataCellRef).append("))=0,")
			.append("\"\",")
			.append("IF(ISERROR(VLOOKUP(")
			.append(dataCellRef)
			.append(",")
			.append(
					workbookInfo
					.getCommonCodeInfoMap()
					.get(columnInfo.getCodeName())
					.getCellAddr()
					.formatAsString(CODE_SHEET.toString(), true)
					)
			.append(",1,0)),\"")
			.append(dataCellRef)
			.append(" 코드값을 잘못입력하였습니다!\",\"\"))");
			
			// 필수 그리고 공통코드있음
		}else if(columnInfo.isRequired() && !columnInfo.getCodeName().isEmpty()) {
			sb.append("IF(LENB(TRIM(")
			.append(dataCellRef).append("))=0,\"")
			.append(dataCellRef).append("필수 입력입니다!\"");
			sb.append(",IF(ISERROR(VLOOKUP(")
			.append(dataCellRef)
			.append(",")
			.append(
					workbookInfo
					.getCommonCodeInfoMap()
					.get(columnInfo.getCodeName())
					.getCellAddr()
					.formatAsString(CODE_SHEET.toString(), true)
					)
			.append(",1,0)),\"")
			.append(dataCellRef)
			.append(" 코드값을 잘못입력하였습니다!\",\"\"))");
		}
			
		return sb.toString();
	}
	

}
