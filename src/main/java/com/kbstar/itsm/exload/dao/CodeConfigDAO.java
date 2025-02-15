package com.kbstar.itsm.exload.dao;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;

import com.kbstar.itsm.exload.model.CodeConfig;

public class CodeConfigDAO extends BaseVectorToFileDAO<CodeConfig>{
	
	/*
	private static String RUN_DIR = System.getProperties().get("user.dir").toString();
	private static String TARGET_DIR = RUN_DIR + File.separator + "conf" ;
	public static String TARGET_FILE_PATH =TARGET_DIR + File.separator+ "CodeConfig.json";
	
	private File tFile;
	private ObjectMapper mapper = new ObjectMapper();
	
	
	
	public CodeConfigDAO() {
		File dirFile = new File(TARGET_DIR);
		if(!dirFile.exists())  dirFile.mkdir();
		
		tFile = new File(TARGET_FILE_PATH);
	}

	@Override
	public boolean saveAll(Vector<CodeConfig> info) {
		boolean result;
		try {
			mapper.writeValue(tFile, info);
			result=true;
		} catch (IOException e) {
			result=false;
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<CodeConfig> loadAll() {

		Vector<CodeConfig> v = new Vector<CodeConfig>();
		
		try {
			List<Map<String, Object>> CodeConfigList = mapper.readValue(tFile,List.class );
			
			v = new Vector<>(ListUtil.tranform(CodeConfigList, (Map<String,Object> map) -> {
				
				return CodeConfig.builder()
						.id((String)map.get("id"))
						.name((String)map.get("name"))
						.sql((String)map.get("sql"))
						.parameters((String)map.get("parameters"))
						.list( (List<Map<String, String>>)map.get("list") )
						.build();						
			}));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return v;
	}
*/


	@Override
	public Function<Map<String, Object>, CodeConfig> readInMapping() {

		return map -> {
			return CodeConfig.builder()
					.id((String)map.get("id"))
					.name((String)map.get("name"))
					.sql((String)map.get("sql"))
					.parameters((String)map.get("parameters"))
					.list( new Vector<>((List<Map<String,String>>) map.get("list")) )
					.build();
		};
	}

	@Override
	public String configFileName() {
		// TODO Auto-generated method stub
		return "CodeConfig.json";
	}


	


}
