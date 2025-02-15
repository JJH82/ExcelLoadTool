package com.kbstar.itsm.exload.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Component;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.kbstar.itsm.exload.dao.GenericVectorDAO;
import com.kbstar.itsm.exload.dao.ISelectMap;
import com.kbstar.itsm.exload.model.CodeConfig;
import com.kbstar.itsm.exload.ui.IPublicCodeEnrollment;

public class TestPublicCodeEnrollmentModel  {

	
	private IPublicCodeEnrollment view = mock(IPublicCodeEnrollment.class);
	private GenericVectorDAO<CodeConfig> dao = mock(GenericVectorDAO.class);
	private ISelectMap<CodeConfig> selectMap = mock(ISelectMap.class);

	private PublicCodeEnrollmentModel model;

	private Vector<CodeConfig> codeConfiglist;
	

	@Test
	public void test모델_생성시() {

		//given
		코드설정값_3개생성();

		
		//when		
		when(dao.loadAll()).thenReturn(codeConfiglist);
		model = new PublicCodeEnrollmentModel(dao,selectMap);
		model.setDefaultView(view);
		
		//then
		verify(dao).loadAll();
		verify(view).displaySavedCodeNameComboData(any());
		verify(view).displayCodeConfigUI(any());
	}

	
	
	
	@Test
	public void test저장된코드명_선택변경시_화면값_설정() {
		//given
		코드설정값_3개생성();
		
		when(dao.loadAll()).thenReturn(codeConfiglist);
		model = new PublicCodeEnrollmentModel(dao,selectMap);
		model.setDefaultView(view);
		
		//when
		model.selectionChange(Optional.of(codeConfiglist.get(0)));
		
		//then
		ArgumentCaptor<CodeConfig> argCap = ArgumentCaptor.forClass(CodeConfig.class);
		verify(view,times(2)).displayCodeConfigUI(argCap.capture());
		assertEquals(codeConfiglist.get(0), argCap.getAllValues().get(argCap.getAllValues().size()-1));
	}

	

	
	@Test
	public void test저장된코드명_선택시_삭제() {
		//given
		코드설정값_3개생성();
		
		when(dao.loadAll()).thenReturn(codeConfiglist);
		model = new PublicCodeEnrollmentModel(dao,selectMap);
		model.setDefaultView(view);
		
		//when
		model.selectionChange(Optional.of(codeConfiglist.get(0)));
		model.remove();
		
		//then
		assertEquals(2, codeConfiglist.size());
		verify(dao).saveAll(any());
		verify(view).clearCodeConfigUI();
		verify(view).updateUI();
	}
	

	@Test
	public void test저장된코드명_선택후_저장된코드명_다른경우_저장() {
		
		//given 
		코드설정값_3개생성();

		when(dao.loadAll()).thenReturn(codeConfiglist);
		model = new PublicCodeEnrollmentModel(dao,selectMap);
		model.setDefaultView(view);

				
		// when		
		model.selectionChange(Optional.of(codeConfiglist.get(0)));
		
		CodeConfig input = CodeConfig.builder()
				.name("name4")  // 저장된 코드명이 없음
				.sql("sql4")
				.parameters("param4")
				.build();		
		
		model.save(input);
		
		
		
		
		// then
		
		// 마지막에 추가됨
		assertEquals(codeConfiglist.lastElement(), input); 
		verify(view).clearCodeConfigUI();
		verify(view).updateUI();
		verify(dao).saveAll(any());
		
	}
	
	
	@Test
	public void test저장된코드명_선택후_저장된코드명_변경하지않을경우_저장() {
		
		//given
		코드설정값_3개생성();
		when(dao.loadAll()).thenReturn(codeConfiglist);
		model = new PublicCodeEnrollmentModel(dao,selectMap);
		model.setDefaultView(view);
		
		//when
		model.selectionChange(Optional.of(codeConfiglist.get(0)));
		
		CodeConfig input = CodeConfig.builder()
				.name("name1")
				.sql("sql4")
				.parameters("param4")
				.build();
		
		model.save(input);
		
		
		// then
		// 데이터 업데이트처리
		assertEquals(codeConfiglist.get(0).getName(),input.getName() ); 
		assertEquals(codeConfiglist.get(0).getSql(),input.getSql() );
		assertEquals(codeConfiglist.get(0).getParameters(),input.getParameters() );
		
		verify(dao).saveAll(any());
	}
	
	
	
	@Test
	public void test저장된코드명_미선택시_저장된코드명하고다른경우_저장() {
		
		//given
		코드설정값_3개생성();
		when(dao.loadAll()).thenReturn(codeConfiglist);
		model = new PublicCodeEnrollmentModel(dao,selectMap);
		model.setDefaultView(view);
		
		//when
		CodeConfig input = CodeConfig.builder()
				.name("name4")
				.sql("sql4")
				.parameters("param4")
				.build();
		
		
		model.save(input);
		
		//then
		// 데이터가 추가됨
		assertEquals(codeConfiglist.lastElement(), input); 
		verify(view).clearCodeConfigUI();
		verify(view).updateUI();
		verify(dao).saveAll(any());
	}


	
	@Test
	public void test저장된코드명_미선택시_같은경우_저장() {
		
		// given
		코드설정값_3개생성();
		when(dao.loadAll()).thenReturn(codeConfiglist);
		model = new PublicCodeEnrollmentModel(dao,selectMap);
		model.setDefaultView(view);

		// when
		CodeConfig input = CodeConfig.builder()
				.name("name1")
				.sql("sql4")
				.parameters("param4")
				.build();
		
		
		model.save(input);
		
		
		//then
		assertEquals(3, codeConfiglist.size());
		
		Optional<CodeConfig> codeConfig = codeConfiglist
											.stream()
											.filter( o ->  o.getName().equals("name1"))
											.findFirst();
		assertTrue(codeConfig.isPresent());		
		assertEquals(input.getName(),codeConfig.get().getName());
		assertEquals(input.getSql(),codeConfig.get().getSql());
		assertEquals(input.getParameters(),codeConfig.get().getParameters());
		
		verify(dao).saveAll(any());
	}


	@Test
	public void test실행버튼클릭시() throws SQLException {
		
		// given
		코드설정값_3개생성();
		when(dao.loadAll()).thenReturn(codeConfiglist);
		model = new PublicCodeEnrollmentModel(dao,selectMap);
		model.setDefaultView(view);

		
		//when
		CodeConfig input = CodeConfig.builder()
				.name("name1")
				.sql("sql4")
				.parameters("param4")
				.build();		
		model.execute(input);
		
		
		//then
		verify(selectMap).selectMap(any());
		verify(view).displaySavedCodeNameComboData(any());
		
	}

	
	
	
	private List<CodeConfig> 코드설정값_3개생성() {
		
		codeConfiglist = new Vector<CodeConfig>();
		codeConfiglist.add(
				CodeConfig.builder()
				.name("name1")
				.sql("sql1")
				.parameters("param1")
				.build()
				);

		codeConfiglist.add(
				CodeConfig.builder()
				.name("name2")
				.sql("sql2")
				.parameters("param2")
				.build()

				);

		codeConfiglist.add(
				CodeConfig.builder()
				.name("name3")
				.sql("sql3")
				.parameters("param3")
				.build()

				);
		
		return codeConfiglist;

		
	}

}
