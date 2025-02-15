package com.kbstar.itsm.excel.loader;


import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ExcelLoader {
	
	
	public static String loader(File file,SqlMode sqlMode) throws IOException, InvalidFormatException  {
		
		OPCPackage pkg = null;
		XSSFWorkbook workbook =  null;
		try  		
		{		
				pkg = OPCPackage.open(file, PackageAccess.READ);
				workbook = new XSSFWorkbook(pkg);
				
				ValidSheet vaildSheet = new ValidSheet(workbook);
			
				vaildSheet.checkSheetPassword();
				//vaildSheet.vaildate();
				
				CompareSheet compareSheet = new CompareSheet(workbook);
				
				DataSheet dataSheet = new DataSheet(workbook, compareSheet.getModifiedRowList(), sqlMode);

				return dataSheet.loadSql();

		}finally {
			if(workbook == null && pkg == null) throw new IOException("엑셀파일 양식 아닙니다!"); 
			if(workbook != null)try {workbook.close(); workbook = null;} catch (IOException e) {throw e;}
			if(pkg != null)try {pkg.close(); pkg = null;} catch (IOException e) {throw e;}			
		}
	}
	
	
	public static void main(String[] args) throws InvalidOperationException, InvalidFormatException, IOException {
		
		ExcelLoader.loader(new File("d:\\new_export.xlsx"),SqlMode.RECOVERY);
		
	}
	
}
