package com.kbstar.itsm.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbstar.itsm.exload.model.CodeConfig;
import com.kbstar.itsm.util.ListUtil;

public class TestCodeConfigToJson {
	
	private final Vector<CodeConfig> codeConfigList = new Vector<CodeConfig>();
	private File codeConfigFile;
	private static String RUN_DIR = System.getProperties().get("user.dir").toString();
	private ObjectMapper mapper =new ObjectMapper();
	
	private CodeConfig one;
	private CodeConfig two;
	private CodeConfig three;
	
	@BeforeEach
	public void setup() {
		
		
		Vector<Map<String,String>> code = new Vector<Map<String,String>>();
		
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("key5", "value1");
		map.put("key4", "value2");
		map.put("1", "value3");
		code.add(map);
		
		
		one= CodeConfig.builder()
				.name("테스트1")
				.sql("sql1")
				.parameters("param1")
				.list(code)
				.build();

		two = CodeConfig.builder()
				.name("테스트2")
				.sql("sql2")
				.parameters("param2")
				.build();

		three = CodeConfig.builder()
				.name("테스트3")
				.sql("sql3")
				.parameters("param3")
				.build();

		
		codeConfigList.add(one);
		codeConfigList.add(two);
		codeConfigList.add(three);
		
		codeConfigFile  = new File(RUN_DIR + File.separator + "codeConfig.json");

	}
	
	@Test
	public void testSaveJsonFile() throws IOException {
		
		
		mapper.writeValue(codeConfigFile,codeConfigList);
		
		
		String savedJsonStr ="";
		try(InputStreamReader isr = new InputStreamReader(new FileInputStream(codeConfigFile), "UTF-8"); 
				BufferedReader br = new BufferedReader(isr) ) {
			savedJsonStr = br.readLine();	
		}
		
		String jsonstr =  mapper.writeValueAsString(codeConfigList);
		
		assertEquals(jsonstr, savedJsonStr);	
	}
	
	@Test
	public void testReadJsonInFile() throws JsonGenerationException, JsonMappingException, IOException {
		
		
		Vector<Map<String, String>> readDbInfoList = mapper.readValue(codeConfigFile,Vector.class );
		
		
		assertEquals(one.getName(), readDbInfoList.get(0).get("name"));

	}
	
	
	@Test
	public void testJson값을빌더클래스에매핑() throws JsonParseException, JsonMappingException, IOException {
		
		Vector<Map<String, Object>> readDbInfoList = mapper.readValue(codeConfigFile,Vector.class );
		
		Vector<CodeConfig> v = new Vector<>(ListUtil.tranformVector(readDbInfoList, (Map<String,Object> map) -> {
			
			return CodeConfig.builder()
					.name((String)map.get("name"))
					.sql((String)map.get("sql"))
					.parameters((String)map.get("parameters"))
					.list(new Vector((List<Map<String, String>>)map.get("list")))
					.build();
					
		}));
		
		assertEquals("value1", v.get(0).getList().get(0).get("key5"));
		
		assertEquals(one.getName(), v.get(0).getName());
		
		
		for(String s : v.get(0).getList().get(0).keySet()) {
			assertEquals("key5", s);
			break;
		}
		
		
	}
	

	


}
