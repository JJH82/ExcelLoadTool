package com.kbstar.itsm.exload.controller;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.CellRangeAddress;

import com.kbstar.itsm.excel.export.ExcelExport;
import com.kbstar.itsm.excel.export.data.ColumnInfo;
import com.kbstar.itsm.excel.export.data.CommonCodeInfo;
import com.kbstar.itsm.excel.export.data.WorkbookInfo;
import com.kbstar.itsm.exload.dao.GenericVectorDAO;
import com.kbstar.itsm.exload.dao.SqlDAO;
import com.kbstar.itsm.exload.model.CodeConfig;
import com.kbstar.itsm.exload.model.MappingInfo;
import com.kbstar.itsm.exload.ui.IRegisterExcelExportForm;
import com.kbstar.itsm.ui.util.Alert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisterExcelExportFormModel implements IRegisterExcelExportFormController {
	
	private IRegisterExcelExportForm defaultView;	 
	private Optional<MappingInfo> selectedItem = Optional.empty();
	private Vector<MappingInfo> mappingInfoList = new Vector<>();
	private List<Map<String,String>> mappingList;
	private List<CodeConfig> codeList;
	
	
	private GenericVectorDAO<MappingInfo> fileDao;
	private GenericVectorDAO<CodeConfig> codeDao;
	private SqlDAO sqlDao;
	
	
	public RegisterExcelExportFormModel(GenericVectorDAO<MappingInfo> fileDao, GenericVectorDAO<CodeConfig> codeDao, SqlDAO columnLableDao) {
		
		this.fileDao = fileDao;	
		this.codeDao = codeDao;
		this.sqlDao = columnLableDao;
		
	}
	

	@Override
	public void setDefaultView(IRegisterExcelExportForm defaultView) {
		this.defaultView = defaultView;
		this.defaultView.setModel(this);
		
		mappingInfoList = this.fileDao.loadAll();
		
		initSelectedIteam();
		
		defaultView.initSavedMappingCombo(mappingInfoList);
		
		initCommonCode();
	}


	private void initSelectedIteam() {
		Optional
		.ofNullable(mappingInfoList)
		.ifPresent( list -> selectionChange(list.stream().findFirst()) );
	}

	private void initCommonCode() {
		
		
		Optional
		.ofNullable( this.codeDao.loadAll() )
		.ifPresent( o -> {
			codeList = o;
			List<String> commonCodeList = new ArrayList<String>();
			commonCodeList.add("");
			commonCodeList.addAll(o.stream().map(in -> in.getName()).collect(toList()));
			defaultView.setCommonCodeComboInGrid( new Vector<String>(commonCodeList) );
		});
		
		
	}


	@Override
	public void selectionChange(Optional<MappingInfo> mappingInfo) {
		selectedItem = mappingInfo;
		
		if(!selectedItem.isPresent()) {
			defaultView.setEnableExecuteBtn(false);
			defaultView.setEnableMappingUI(false);
			return;
		}
		
		selectedItem.ifPresent( o -> {
			defaultView.displaySelectedMappingInfo(o);
			defaultView.setEnableMappingUI(o.getColumnList().size() > 0);
			defaultView.setEnableExecuteBtn(true);
			
			
		});
	}
	
	
	@Override
	public void save(MappingInfo mappingInfo) {		
		saveAndUpdateMapping(mappingInfo);
	}


	/**
	 * 
	 * @param mappingInfo 값은  name과 sql값을 화면서에서 설정해서 넘겨줌 나머지는 데이터 없음 
	 * 
	 */
	private void saveAndUpdateMapping(MappingInfo mappingInfo) {
		if(!mappingInfo.getName().trim().isEmpty()) {
			if(!isUpdateMappingInfo(mappingInfo)) addMappingInfo(mappingInfo);
			
			fileDao.saveAll(mappingInfoList);
		}
	}
	
	
	
	private void addMappingInfo(MappingInfo mappingInfo) {
		
		//현재 선택된 매핑값이 있으면 새로 생긴 매핑값에 넣어서 파일로 저장
		selectedItem.ifPresent( o -> {
			mappingInfo.setColumnList(o.getColumnList());
			mappingInfo.setMappingList(o.getMappingList());
		});
		
		mappingInfoList.add(mappingInfo);		
		defaultView.clearMappingInfoUI();
	}
	
	
	/**
	 * mappingInfoList 콤보박스에 들어있는 이름중 같은 이름을 찾은 다음 그이름에 SQL만 업데이트
	 * @param mInfo
	 * @return
	 */
	private boolean isUpdateMappingInfo(MappingInfo mInfo) {
		 
		Optional<MappingInfo> updateTarget = mappingInfoList.stream()
														.filter( o -> mInfo.getName().equals(o.getName()))
														.findFirst();
		
		
		updateTarget.ifPresent((o) -> {
			o.setSql(mInfo.getSql());			
		});

		return updateTarget.isPresent();
	}

	@Override
	public void remove() {
		
		selectedItem.ifPresent((o) -> {
			mappingInfoList.remove(o);
			fileDao.saveAll(mappingInfoList);
		});
		
		defaultView.clearMappingInfoUI();
	}


	@Override
	public void executeSql(String sql) {
		
		List<String> resultList;
		try {
		
			resultList = sqlDao.selectColumnLable(sql);
		
			
			if( resultList.size()  == 0 ) {
				defaultView.setEnableMappingUI(false);
				return;
			}
			
			selectedItem.ifPresent(o -> {
				defaultView.setEnableMappingUI(true);
				o.getColumnList().removeAllElements();
				o.getColumnList().addAll(resultList);
			});
			
		
		} catch (SQLException e) {
			
			Alert.ERROR.show(e.toString());
		}
		
		
	}


	@Override
	public void addRow(final int index) {
		
		
		selectedItem.ifPresent(o -> {
			
			Map<String,String> map = new HashMap<>();			
			map.put("SQL컬럼명", "");
			map.put("DB테이블", "");
			map.put("컬럼명", "");
			map.put("공통코드", "");
			map.put("크기", "");
			map.put("조건절", "N");
			map.put("필수", "N");			
			map.put("틀고정", "N");
			map.put("수기입력", "N");
			if (index == -1) o.getMappingList().add(map);
			else o.getMappingList().add(index,map);
			
			
			defaultView.updateUIMappingGrid();
		});
	
	}


	@Override
	public void removeRow(int index) {
		
		if(index ==  -1) return;
		
		selectedItem.ifPresent(o -> { 
			o.getMappingList().remove(index);
			defaultView.clearSelectionMappingGrid();
			defaultView.updateUIMappingGrid();
		});
		
	}


	


	@Override
	public void clearFreezePane() {
		
		selectedItem.ifPresent(o -> {
			for(Map<String,String> map : o.getMappingList()) {
				map.put("틀고정", "N");
			}
		});
		
		defaultView.updateUIMappingGrid();
	}

	
	public List<String> findCodeList(String codeName) {
		
		return codeList.stream()
		.filter(code -> codeName.equals(code.getName()))
		.findFirst()
		.orElse(CodeConfig.builder().build())
		.getList()
		.stream().map( o -> o.get("값") +"|"+ o.get("코드"))
		.collect(toList());
				
	}
	
	private int toInt(String s) {
		try {
			return Integer.parseInt(s);
		}catch (NumberFormatException e) {
			return 0;
		}
	}
	

	@Override
	public void exportExcel(File exportFile, MappingInfo mappingInfo) {
		
		saveAndUpdateMapping(mappingInfo);
 
		selectedItem.ifPresent(o -> {
			try {	
					int i=0;
					for(Map<String,String> m :o.getMappingList()) m.put("IDX", String.valueOf(i++));
					final List<Map<String,String>> sqlResultList =  sqlDao.selectMap(o.getSql());
					
					List<ColumnInfo> mataDataList = o.getMappingList()					
					.stream()
					.map( mapping ->  {
						
						Map<String,String> mataData = new HashMap<>();
						mataData.put("DB테이블명", mapping.get("DB테이블"));
						mataData.put("컬럼명", mapping.get("컬럼명"));
						mataData.put("조건절", mapping.get("조건절"));
						mataData.put("수기입력", mapping.get("수기입력"));
		
						return ColumnInfo.builder()
								.sqlColumnName( mapping.get("SQL컬럼명") )
								.codeName( mapping.get("공통코드") )
								.colIdx( toInt(mapping.get("IDX")) )
								.required( "Y".equals(mapping.get("필수")) )
								.where( "Y".equals(mapping.get("조건절")) )
								.mataData(mataData)
								.dataList( 
										
											sqlResultList.stream()
												 .map(d -> d.get(mapping.get("SQL컬럼명")))
												 .collect(toList())
										)
								.size( toInt(mapping.get("크기")) )
								.freezeColumnIdx(  mapping.get("틀고정").equals("Y") ?  toInt(mapping.get("IDX"))+1 : 0  )
								.build();
								
					}).collect(toList());
					
					
					
					ExcelExport.export(
							WorkbookInfo.builder()
							.exportFile(exportFile)
							.ColumnInfoList(mataDataList)
							.dataRowCount(sqlResultList.size())
							.commonCodeInfoMap(createCommonCodeMap(o))
							.build()
							);
					
			} catch (IOException|InvalidFormatException|SQLException  e) {
				Alert.ERROR.show(e.toString());
			}
		});
		
	}


	
	 // 공통코드 값이 있을경우만 공통코드명과 공통코드정보(공통코드명,공통코드 엑셀 주소값)
	private Map<String,CommonCodeInfo> createCommonCodeMap(MappingInfo o) {
		

		int i=0;
		List<String> list = o.getMappingList()
							.stream()
							.filter(m-> !m.getOrDefault("공통코드", "").isEmpty() )
							.map(m->m.get("공통코드"))
							.distinct()
							.collect(toList());
		
		Map<String,CommonCodeInfo> map = new LinkedHashMap<>();
		for(String  value: list) {
			
			List<String> codeList = findCodeList(value);
			if(codeList.size() == 0) continue;
			
			map.put(value, 
					CommonCodeInfo
					.builder()
					.list(codeList )
					.cellAddr(new CellRangeAddress(1,codeList.size()+1,i, i  ) )
					.build()
					);			
			
			i++;
		}
		
		return map;
	}


	@Override
	public void clearData() {
		
		selectedItem = Optional.empty();
		
		defaultView.setEnableExecuteBtn(false);
		defaultView.setEnableMappingUI(false);
		defaultView.displaySelectedMappingInfo(MappingInfo.builder().build());
				
		
	}

}
