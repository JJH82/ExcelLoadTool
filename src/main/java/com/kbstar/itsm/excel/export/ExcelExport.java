package com.kbstar.itsm.excel.export;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kbstar.itsm.excel.export.data.WorkbookInfo;

public class ExcelExport {
	
	public static void export(WorkbookInfo workbookInfo) throws JsonProcessingException, IOException, InvalidFormatException  {

		SXSSFWorkbook sxworkbook = null;

		try (
			 FileOutputStream fos = new FileOutputStream(workbookInfo.getExportFile());
			){

			sxworkbook = new SXSSFWorkbook();
			
			//new BackupSheet(new ValidSheet(new CodeSheet(new DataSheet(sxworkbook, workbookInfo))));
			new CompareSheet(new BackupSheet(new ValidSheet(new CodeSheet(new DataSheet(sxworkbook, workbookInfo)))));			

			sxworkbook.setForceFormulaRecalculation(true);
			sxworkbook.getXSSFWorkbook().setCellFormulaValidation(true);
			sxworkbook.getXSSFWorkbook().lockStructure();
			
			sxworkbook.write(fos);
			
		}finally {			
			if(sxworkbook != null)try {sxworkbook.close(); sxworkbook = null;} catch (IOException e) {throw e;}						
		}		
	}
}
