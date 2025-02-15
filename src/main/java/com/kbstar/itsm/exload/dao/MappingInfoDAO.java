package com.kbstar.itsm.exload.dao;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;

import com.kbstar.itsm.exload.model.MappingInfo;

public class MappingInfoDAO extends BaseVectorToFileDAO<MappingInfo> {

	@Override
	public String configFileName() {

		return "mappingInfo.json";
	}

	@Override
	public Function<Map<String, Object>, MappingInfo> readInMapping() {

		return map -> MappingInfo
						.builder()
						.name((String)map.get("name"))
						.sql((String)map.get("sql"))
						.mappingList(new Vector<>((List<Map<String, String>>)map.get("mappingList")))
						.columnList(new Vector<>(((List<String>)map.get("columnList"))))
						.build();
							
	}

	
	
}
