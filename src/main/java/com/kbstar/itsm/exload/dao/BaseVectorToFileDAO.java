package com.kbstar.itsm.exload.dao;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbstar.itsm.exload.jdbc.DataSource;
import com.kbstar.itsm.util.ListUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseVectorToFileDAO<T> implements GenericVectorDAO<T> {
	
	private static String TARGET_DIR = new File(".") + File.separator + "conf" ;
 

	
	private File tFile;
	private ObjectMapper mapper = new ObjectMapper();
	
	
	
	public  BaseVectorToFileDAO() {
				 
		// DBInfo.json 프로그램 설치 된 conf 및에 위치하기 위해서
		String  datasoureFolder = "DBInfo.json".equals(configFileName()) ?  "" : File.separator + DataSource.INSTANCE.getDBInfo().getConnectName();
		
		File dirFile = new File(TARGET_DIR + datasoureFolder);		
		
		if(!dirFile.exists())  dirFile.mkdir();
		
		tFile = new File(dirFile+File.separator +configFileName());
		
	}
	
	@Override
	public boolean saveAll(Vector<T> infoList) {
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
	

	@Override
	public Vector<T> loadAll() {
		Vector<T> v = new Vector<T>();
		
		try {
			Vector<Map<String,Object>> readDbInfoList = mapper.readValue(tFile,Vector.class );
			v = ListUtil.tranformVector(readDbInfoList, readInMapping());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return v;
	}
	
	public abstract String configFileName();
	public abstract Function<Map<String,Object>,T> readInMapping();

	
	

}
