package com.kbstar.itsm.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kbstar.itsm.test.util.GeneratorUtil;

public class TestMetaData {
	
	
	
	
	
	
	public List<Map<String,String>> MappingData() {
		
		return GeneratorUtil.toVector(3, i -> {
			Map<String,String> map = new HashMap<>();
			map.put("SQL컬럼명", "SQL컬럼명_"+i);
			map.put("DB테이블", "DB테이블_" + i);
			map.put("컬럼명", "컬럼명_"+ i);
			map.put("공통코드", "");
			map.put("크기", "10");
			map.put("조건절", "N");
			map.put("필수", "N");			
			map.put("틀고정", "N");			
			return map;
		});
		
	}
	
	
	public List<Map<String,String>> sqlData() {
		
		return GeneratorUtil.toVector(3, i -> {
			Map<String,String> map = new HashMap<>();
			map.put("SQL컬럼명_"+i, "값_"+i);
			return map;
		});
		
	}

}
