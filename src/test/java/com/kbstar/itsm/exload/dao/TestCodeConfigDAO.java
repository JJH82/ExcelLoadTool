package com.kbstar.itsm.exload.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kbstar.itsm.exload.model.CodeConfig;

public class TestCodeConfigDAO {
	
	
	private Vector<CodeConfig> codeConfigList;
	private CodeConfigDAO dao;

	
	@BeforeEach
	public void setUp() {
		dao = new CodeConfigDAO();
		addThreeCodeConfig();
	}
	
	@Test
	public void test파일저장() {
		dao.saveAll(codeConfigList);
		assertTrue(new File( new File(".").getAbsoluteFile()+File.separator + "conf"+File.separator+"테스트2"+File.separator+dao.configFileName() ).exists());
	}
	
	
	
	@Test
	public void test파일로드() {
		dao.saveAll(codeConfigList);
		
		CodeConfig input = codeConfigList.get(1);
		CodeConfig load = dao.loadAll().get(1);
		
		assertEquals(input.getName(), load.getName());
		assertEquals(input.getSql(), load.getSql());
		assertEquals(input.getParameters(), load.getParameters());
		assertEquals(input.getList(), load.getList());
		
	}
	
	private void addThreeCodeConfig() {
		

		codeConfigList = new Vector<CodeConfig>();
		codeConfigList.add(
				CodeConfig
				.builder()
				.name("테스트1")
				.sql("sql1")
				.parameters("파라미터2")
				.list(createCodeList("key1","value1"))
				.build()
				);
		
		codeConfigList.add(
				CodeConfig
				.builder()
				.name("테스트2")
				.sql("sql2")
				.parameters("파라미터2")
				.list(createCodeList("key2","value2"))
				.build()
				);

		codeConfigList.add(
				CodeConfig
				.builder()
				.name("테스트3")
				.sql("sql3")
				.parameters("파라미터3")
				.list(createCodeList("key3","value3"))
				.build()
				);
	}
	
	public Vector<Map<String,String>> createCodeList(String key,String value) {
		
		Vector<Map<String,String>> list = new Vector<Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
		map.put(key,value);		
		list.add(map);
		
		return list;
	}

	
	
	

}
