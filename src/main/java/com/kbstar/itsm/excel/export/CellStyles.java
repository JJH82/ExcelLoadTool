package com.kbstar.itsm.excel.export;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.Getter;


class CellStyles {
	
	private Workbook workbook;
	
	
	@Getter
	private CellStyle lockText;
	
	@Getter
	private CellStyle unlockText;
	
	public CellStyles(Workbook workbook) {
		this.workbook = workbook;
		lockText = createCellStyle(true);
		unlockText = createCellStyle(false);	
	}
	
	private CellStyle createCellStyle(boolean isLock) {
		CellStyle dataCellStyle = workbook.createCellStyle();
		DataFormat df = workbook.createDataFormat();
		dataCellStyle.setLocked(isLock);
		dataCellStyle.setDataFormat(df.getFormat("@"));
		return dataCellStyle;
	}
}
