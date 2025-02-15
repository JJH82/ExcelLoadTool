package com.kbstar.itsm.exload.dao;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbstar.itsm.exload.model.CodeConfig;
import com.kbstar.itsm.exload.model.DBInfo;
import com.kbstar.itsm.util.ListUtil;

public class DBInfoFileDAO extends BaseVectorToFileDAO<DBInfo>{

	/*
	private static String RUN_DIR = System.getProperties().get("user.dir").toString();
	private static String TARGET_DIR = RUN_DIR + File.separator + "conf" ;
	public static String TARGET_FILE_PATH =TARGET_DIR + File.separator+ "DBInfo.json";

	
	private File tFile;
	private ObjectMapper mapper = new ObjectMapper();
	
	public DBInfoFileDAO() {
		
		File dirFile = new File(TARGET_DIR);
		if(!dirFile.exists())  dirFile.mkdir();
		
		tFile = new File(TARGET_FILE_PATH);
	}
	

	@Override
	public boolean saveAll(Vector<DBInfo> infoList) {
		boolean result;
		try {
			mapper.writeValue(tFile, infoList);
			result=true;
		} catch (IOException e) {
			result=false;
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<DBInfo> loadAll() {

		Vector<DBInfo> v = new Vector<DBInfo>();
		
		try {
			List<Map<String, Object>> readDbInfoList = mapper.readValue(tFile,List.class );
			
			v = new Vector<>(ListUtil.tranform(readDbInfoList, (Map<String,Object> map) -> {
				
				return DBInfo.builder()
						.id((String)map.get("id"))
						.connectName((String)map.get("connectName"))
						.jdbcUrl((String)map.get("jdbcUrl"))
						.userName((String)map.get("userName"))
						.password((String)map.get("password"))
						.defaultConnectSetting( (boolean)map.get("defaultConnectSetting")  )
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
	public String configFileName() {
		return "DBInfo.json";
	}


	@Override
	public Function<Map<String, Object>, DBInfo> readInMapping() {

		return map -> DBInfo.builder()
				.id((String)map.get("id"))
				.connectName((String)map.get("connectName"))
				.jdbcUrl((String)map.get("jdbcUrl"))
				.userName((String)map.get("userName"))
				.password((String)map.get("password"))
				.defaultConnectSetting( (boolean)map.get("defaultConnectSetting")  )
				.build();
	}



}
