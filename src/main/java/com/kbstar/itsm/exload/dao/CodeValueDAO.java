package com.kbstar.itsm.exload.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import com.kbstar.itsm.exload.jdbc.DataSource;
import com.kbstar.itsm.exload.model.CodeConfig;

public class CodeValueDAO implements ISelectMap<CodeConfig> {
	
	@Override
	public List<Map<String,String>> selectMap(CodeConfig codeConfig) throws SQLException{
		
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		String[] colNm = new String[] {"코드","값"};
		ResultSet rs = null;
		try(
				Connection con = DataSource.INSTANCE.getConnection();
				PreparedStatement s = con.prepareStatement(mappingedSql(codeConfig));
			) {
			
				if(!s.execute()) return result;
				rs = s.getResultSet();
				
				ResultSetMetaData metaData = rs.getMetaData();				
				
				while(rs.next()) {
					
					Map<String, String> map = new LinkedHashMap<String, String>();
					for(int i =0 ; i < 2; i++) map.put(colNm[i], rs.getString(i+1));						

					result.add(map);
				}

		}finally {
			if(rs != null) try {rs.close(); } catch (SQLException e) {e.printStackTrace();}
		}

		return result;
	}
	
	
	public String mappingedSql(CodeConfig codeConfig) {
		
		String result = codeConfig.getSql();
		
		System.out.println(codeConfig.getParameters().indexOf(":"));
		if( codeConfig.getParameters().indexOf(":") == -1 ) return result;
		
		for(String paramStr : StringUtils.split(codeConfig.getParameters(),",")) {
			String[] keyValue = StringUtils.split(paramStr, ":");
			
			if(keyValue.length == 2) result = RegExUtils.replaceAll(result, "\\$\\b"+keyValue[0]+"\\b", keyValue[1]);
		}
		
		return result;
	}

}
