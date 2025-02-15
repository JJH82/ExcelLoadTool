package com.kbstar.itsm.exload.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.kbstar.itsm.exload.dao.GenericVectorDAO;
import com.kbstar.itsm.exload.dao.SqlDAO;
import com.kbstar.itsm.exload.model.CodeConfig;
import com.kbstar.itsm.exload.model.MappingInfo;
import com.kbstar.itsm.exload.ui.IRegisterExcelExportForm;
import com.kbstar.itsm.test.util.GeneratorUtil;

public class TestRegisterExcelExportFormModel {

	
	private RegisterExcelExportFormModel model;
	
	private GenericVectorDAO<MappingInfo> fileDao = mock(GenericVectorDAO.class);
	private GenericVectorDAO<CodeConfig> codeDao = mock(GenericVectorDAO.class);
	private SqlDAO columnLableDao = mock(SqlDAO.class);

	private IRegisterExcelExportForm defaultView = mock(IRegisterExcelExportForm.class);
	
	
	@BeforeEach
	public void setup() {
		model = new RegisterExcelExportFormModel(fileDao, codeDao, columnLableDao);
	}
	
	@Test
	public void 저장된매핑명_로드시_데이터있는경우() {
		
		// 뷰을 모델의 등록 및 초기화
		when(fileDao.loadAll()).thenReturn(리스트_3개생성());
		when(codeDao.loadAll()).thenReturn(new Vector<CodeConfig>());
		
		model.setDefaultView(defaultView);
		
		//매핑명 콤보에 데이터출력
		verify(defaultView).initSavedMappingCombo(any(Vector.class));
		// 그리안에 공통코드 콤보에 데이터출력
		verify(defaultView).setCommonCodeComboInGrid(any(Vector.class));

		// 매핑정보 존재시 첫번째값선택
		ArgumentCaptor<MappingInfo> arg = ArgumentCaptor.forClass(MappingInfo.class);		
		verify(defaultView).displaySelectedMappingInfo(arg.capture());
		assertEquals("이름1", arg.getValue().getName());
	}
	
	@Test
	public void 저장된매핑명_로드시_데이터가없는경우() {	
		// 매핑정보가 없을시
		when(fileDao.loadAll()).thenReturn(new Vector<MappingInfo>());	
		model.setDefaultView(defaultView);
		verify(defaultView,never()).displaySelectedMappingInfo(any(MappingInfo.class));
	}
	
	
	
	@Test
	public void SQL미실행후_저장된매핑명_선택시() {		
		model.setDefaultView(defaultView);
		model.selectionChange(Optional.of( 리스트_3개생성().get(0)));
		
		verify(defaultView).setEnableExecuteBtn(eq(true));
		verify(defaultView).setEnableMappingUI(eq(false));
	}
	
	
	@Test
	public void SQL실행후_저장된매핑명_선택시() {		
		model.setDefaultView(defaultView);
		model.selectionChange(Optional.of(리스트_3개생성_SQL조회후결과값있음().get(0)));
		
		verify(defaultView).setEnableExecuteBtn(eq(true));
		verify(defaultView).setEnableMappingUI(eq(true));
	}

	
	@Test
	public void SQL실행버튼클릭시_검색결과있음() throws SQLException {		
	
		model.setDefaultView(defaultView);
		// 콤보박스를 선택하였을경우 SQL실행버튼 기동
		model.selectionChange(Optional.of(리스트_3개생성().get(0)));		
		when(columnLableDao.selectColumnLable(anyString())).thenReturn(SQL실행시_컬럼());
		
		model.executeSql("select * from test");
		
		verify(columnLableDao).selectColumnLable(anyString());
		verify(defaultView).setEnableMappingUI(eq(true));
		

	}
	
	@Test
	public void SQL실행버튼클릭시_검색결과없음() throws SQLException {		
	
		model.setDefaultView(defaultView);
		
		when(columnLableDao.selectColumnLable(anyString())).thenReturn(new ArrayList<>());
		
		model.executeSql("select * from test");
		
		verify(columnLableDao).selectColumnLable(anyString());
		verify(defaultView).setEnableMappingUI(eq(false));

	}
	
	@Test
	public void 저장버튼클릭시_매핑명중복시_업데이트() {		

		Vector<MappingInfo> infoList = 리스트_3개생성_SQL조회후결과값있음();
		when(fileDao.loadAll()).thenReturn(infoList);
		when(codeDao.loadAll()).thenReturn( 코드_3개생성() );
		
		model.setDefaultView(defaultView);
		
		MappingInfo info = MappingInfo.builder()
				.name("이름1")
				.sql("테스트SQL")
				.build() ;		
		model.save(info);
		
		ArgumentCaptor<Vector<MappingInfo>> arg = ArgumentCaptor.forClass(Vector.class);
		verify(fileDao).saveAll(arg.capture());
		assertEquals(info.getName(), arg.getValue().firstElement().getName());
		assertEquals(info.getSql(), arg.getValue().firstElement().getSql());
	
	
	}

	
	@Test
	public void 저장버튼클릭시_새로운매핑명_추가() {		
		
		Vector<MappingInfo> infoList = 리스트_3개생성_SQL조회후결과값있음();
		when(fileDao.loadAll()).thenReturn(infoList);
		model.setDefaultView(defaultView);
		
		MappingInfo info = MappingInfo.builder()
				.name("새로운매핑")
				.sql("sql")
				.build() ;		
		model.save(info);
		
		ArgumentCaptor<Vector<MappingInfo>> arg = ArgumentCaptor.forClass(Vector.class);
		verify(fileDao).saveAll(arg.capture());
		assertEquals(info, arg.getValue().lastElement());
		assertEquals(4,arg.getValue().size() );
		
	}

	
	@Test
	public void 삭제버튼클릭시() {		
		
		Vector<MappingInfo> infoList = 리스트_3개생성_SQL조회후결과값있음();
		when(fileDao.loadAll()).thenReturn(infoList);
		model.setDefaultView(defaultView);
		
		model.remove();
		
		ArgumentCaptor<Vector<MappingInfo>> arg = ArgumentCaptor.forClass(Vector.class);
		verify(fileDao).saveAll(arg.capture());
		assertEquals(2, arg.getValue().size());
		verify(defaultView).clearMappingInfoUI();		
		
	}

	
	@Test
	public void 그리드추가버튼클릭() {		
		when(fileDao.loadAll()).thenReturn(리스트_3개생성());
		model.setDefaultView(defaultView);
		
		model.addRow(1);
		
		verify(defaultView).updateUIMappingGrid();
	}
	
	@Test
	public void 그리드삭제버튼클릭() {		
		when(fileDao.loadAll()).thenReturn(리스트_3개생성());
		model.setDefaultView(defaultView);
		
		model.removeRow(0);;
		
		verify(defaultView).updateUIMappingGrid();
	}
	
	
	
	

	
	@Test
	public void 틀고정_전체_N초기화() {		
		Vector<MappingInfo> infoList = 리스트_3개생성();
		when(fileDao.loadAll()).thenReturn(infoList);
		model.setDefaultView(defaultView);
		
		model.clearFreezePane();
	
		for(Map<String,String> value : infoList.get(0).getMappingList()) {
			assertEquals("N", value.get("틀고정"));
		}
		
	}

	
	private Vector<String> SQL실행시_컬럼() {
		return GeneratorUtil.toVector(3, i -> "컬럼명" + i);
	}
	
	
	
	private Vector<CodeConfig> 코드_3개생성() {

		return GeneratorUtil.toVector(3, i -> {
			return CodeConfig
					.builder()
					.name("코드값 :" + i)
					.build();
		});
	}
	

	
	private Vector<MappingInfo> 리스트_3개생성() {

		return GeneratorUtil.toVector(3, i -> {
			return MappingInfo.builder()
			.name("이름" + i )
			.sql("sql" + i)
			.mappingList(
					GeneratorUtil.toVector(3, j -> {
						Map<String,String> m = new HashMap<>();
						m.put("key"+j, "value"+ j);
						m.put("틀고정", "Y");
						return m;
					})
					)
			.build();
		});
	}
	
	
	private Vector<MappingInfo> 리스트_3개생성_SQL조회후결과값있음() {

		return GeneratorUtil.toVector(3, i -> {
			return MappingInfo.builder()
			.name("이름" + i )
			.sql("sql" + i)
			.mappingList(
					GeneratorUtil.toVector(3, j -> {
						Map<String,String> m = new HashMap<>();
						m.put("key"+j, "value"+ j);
						return m;
					})
					)
			.columnList(new Vector<>(GeneratorUtil.toList(3, k -> "구성항목명"+k )) )
			.build();
		});
	}

}
