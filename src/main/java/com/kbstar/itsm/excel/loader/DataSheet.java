package com.kbstar.itsm.excel.loader;

import static com.kbstar.itsm.excel.SheetNames.DATA_SHEET;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbstar.itsm.excel.SheetNames;

class DataSheet {

	private ObjectMapper mapper = new ObjectMapper();
	private List<Integer> modifiedRowList;
	private List<Map<String, String>> colInfoList = new ArrayList<>();
	private List<Map<String, List<Map<String, String>>>> dataList = new ArrayList<>();

	private Enum<SqlMode> sqlMode;
	private XSSFSheet sheet;
	

	DataSheet(XSSFWorkbook workbook, List<Integer> modifiedRowList, SqlMode sqlMode)
			throws JsonMappingException, JsonProcessingException {
		sheet = workbook.getSheet(sqlMode.getSheetName());
		
		this.sqlMode = sqlMode;
		
		this.modifiedRowList = modifiedRowList;

		readHeadData();
		readData();
	}
	
	 
	
	void readHeadData() throws JsonMappingException, JsonProcessingException {
		
		Row row = sheet.getRow(0);
		for (int colnum = 0; colnum < row.getLastCellNum(); colnum++) {
			String colInfoStr = row.getCell(colnum).getStringCellValue();
			
			Map<String, String> map = mapper.readValue(row.getCell(colnum).getStringCellValue(), Map.class);
			colInfoList.add(map);
		}
	}

	void readData() throws JsonProcessingException, JsonMappingException {

		for (int rownum : modifiedRowList) {
			Row row = sheet.getRow(rownum);

			Map<String, List<Map<String, String>>> rowData = new HashMap<>();
			for (int colnum = 0; colnum < row.getLastCellNum(); colnum++) {

				if(colInfoList.get(colnum).get("컬럼명").isEmpty() || colInfoList.get(colnum).get("DB테이블명").isEmpty() ) continue;
				
				Cell cell = row.getCell(colnum);				
				
				Map<String, String> colData = new HashMap<>();				
				colData.putAll(colInfoList.get(colnum));
				
				colData.put("값", "Y".equals(colInfoList.get(colnum).get("조건절")) ? cell.getStringCellValue() : stripDisplayValue(cell.getStringCellValue()));

				if (!rowData.containsKey(colData.get("DB테이블명"))) {
					List<Map<String, String>> colList = new ArrayList<>();
					colList.add(colData);
					rowData.put(colData.get("DB테이블명"), colList);
				} else {
					rowData.get(colData.get("DB테이블명")).add(colData);
				}
			}
			dataList.add(rowData);
		}
	}
	
	
	private String stripDisplayValue(String value) {		
		if(value.contains("|")) value =  value.substring(value.lastIndexOf("|")+1, value.length());
		return value;
	}

	String loadSql() {

		StringBuilder sb = new StringBuilder();
		for(Map<String,List<Map<String,String>>> rowData : dataList) {
			  for(String table : rowData.keySet()) {
				 rowData.get(table).sort((s1,s2) ->  s1.get("조건절").compareTo(s2.get("조건절")));
				 sb.append(  SqlMode.INSERT.equals(sqlMode) ? getInsertSqlStr(table, rowData.get(table)) : getUpdateSqlStr(table, rowData.get(table)) );
				 sb.append(";\n");
			  } 
		  }
		return sb.toString();
	}
	
	
	private String getUpdateSqlStr(String table,List<Map<String,String>> rowData) {
		StringBuilder sb = new StringBuilder();

		sb.append("UPDATE ").append(table).append(" SET ");
		for(Map<String,String> rowMap : rowData) {
			if("N".equals(rowMap.get("조건절")) ) {
				sb.append(rowMap.get("컬럼명")).append(" = ").append(getValueSql(rowMap)).append(", ");
			}else {
				//엑셀에 첫행 첫컬럼애 키값
				sb.deleteCharAt(sb.toString().length()-2);
				sb.append(" WHERE ").append(rowMap.get("컬럼명")).append(" = ").append(getValueSql(rowMap));
			}
		}
		
		return sb.toString();
	}
		
	private String getInsertSqlStr(String table,List<Map<String,String>> rowData) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(table).append(" ");

		sb.append("(");
		for(Map<String,String> rowMap : rowData) {
			sb.append(rowMap.get("컬럼명")).append(", ");
		}
		sb.deleteCharAt(sb.toString().length()-2);
		sb.append(")");
		
		sb.append(" VALUES(");
		for(Map<String,String> rowMap : rowData) {
			sb.append(getValueSql(rowMap)).append(", ");
		}
		sb.deleteCharAt(sb.toString().length()-2);
		sb.append(")");

		return sb.toString();
	}
	
	private String getValueSql(Map<String,String> map) {
		
		if("Y".equals(map.get("수기입력"))) return map.get("값") ;
		
		return "'" + map.get("값") + "'";
		
	}
}
