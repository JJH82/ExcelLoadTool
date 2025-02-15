package com.kbstar.itsm.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;

import javax.xml.transform.Transformer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbstar.itsm.exload.model.DBInfo;
import com.kbstar.itsm.util.ListUtil;

public class TestDBInfoToJson {
	
	private final Vector<DBInfo> dbInfoList = new Vector<DBInfo>();
	private File dbInfoFile;
	private static String RUN_DIR = System.getProperties().get("user.dir").toString();
	private ObjectMapper mapper =new ObjectMapper();
	
	private DBInfo one;
	private DBInfo two;
	private DBInfo three;
	
	@BeforeEach
	public void setup() {
		
		one = DBInfo.builder()
				.connectName("테스트")
				.jdbcUrl("tcp://12348")
				.id("sqe000")
				.password("test")
				.build();

		two = DBInfo.builder()
				.connectName("테스트2")
				.jdbcUrl("tcp://12348")
				.id("sqe000")
				.password("test")
				.build();

		three = DBInfo.builder()
				.connectName("테스트2")
				.jdbcUrl("tcp://12348")
				.id("sqe000")
				.password("test")
				.build();
		
		dbInfoList.add(one);
		dbInfoList.add(two);
		dbInfoList.add(three);
		
		dbInfoFile  = new File(RUN_DIR + File.separator + "dbInfo.json");

	}
	
	@Test
	public void testSaveJsonFile() throws IOException {
		
		
		mapper.writeValue(dbInfoFile,dbInfoList);
		
		
		String savedJsonStr ="";
		try(InputStreamReader isr = new InputStreamReader(new FileInputStream(dbInfoFile), "UTF-8"); 
				BufferedReader br = new BufferedReader(isr) ) {
			savedJsonStr = br.readLine();	
		}
		
		String jsonstr =  mapper.writeValueAsString(dbInfoList);
		
		assertEquals(jsonstr, savedJsonStr);	
	}
	
	@Test
	public void testReadJsonInFile() throws JsonGenerationException, JsonMappingException, IOException {
		
		
		Vector<Map<String, String>> readDbInfoList = mapper.readValue(dbInfoFile,Vector.class );
		
		assertEquals(one.getConnectName(), readDbInfoList.get(0).get("connectName"));

	}
	
	@Test
	public void testDBInfo빌드메소드명찾기() {
		String name="";
		for(Method  m : DBInfo.builder().getClass().getMethods()) {
			if(m.getName().equals("connectName")) {
				name = m.getName();
				break;
			}
			
		}
		
		assertEquals("connectName", name);
	}
	
	@Test
	public void testJson값을빌더클래스에매핑() throws JsonParseException, JsonMappingException, IOException {
		
		List<Map<String, Object>> readDbInfoList = mapper.readValue(dbInfoFile,List.class );
		
		
		Vector<DBInfo> v = new Vector<>(ListUtil.tranform(readDbInfoList, (Map<String,Object> map) -> {
			
			return DBInfo.builder()
					.connectName((String)map.get("connectName"))
					.jdbcUrl((String)map.get("jdbcUrl"))
					.id((String)map.get("id"))
					.password((String)map.get("password"))
					.defaultConnectSetting( (boolean)map.get("defaultConnectSetting")  )
					.build();
					
		}));
		
		assertEquals(one.getConnectName(), v.get(0).getConnectName());
		
		
	}
	

	


}
