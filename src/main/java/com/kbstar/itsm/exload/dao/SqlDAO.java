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

import com.kbstar.itsm.exload.jdbc.DataSource;

public class SqlDAO {
	
	
	public List<String> selectColumnLable(String sql) throws SQLException{
		
		List<String> result = new ArrayList<>();
		ResultSet rs = null;
		
		try(
				Connection con = DataSource.INSTANCE.getConnection();
				PreparedStatement s = con.prepareStatement(sql);
			) {
				if(!s.execute()) return result;
				rs = s.getResultSet();
				
				ResultSetMetaData metaData = rs.getMetaData();				

				if(!rs.next()) return result; 
				
				for(int i =1 ; i <= metaData.getColumnCount(); i++) {
					result.add(metaData.getColumnLabel(i));
				}
								
		}finally {
			if(rs != null) try {rs.close(); } catch (SQLException e) {e.printStackTrace();}
		}
		
		return result;
	}
	
	

	public List<Map<String,String>> selectMap(String sql) throws SQLException{
		
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement s = null;
		try {
				con = DataSource.INSTANCE.getConnection();
				s = con.prepareStatement(sql);
				System.out.println("SQL 실행중ㅇ....");
				if(!s.execute()) return result;
				rs = s.getResultSet();
				
				ResultSetMetaData metaData = rs.getMetaData();				
				System.out.println("결과값 읽어옴....");
				while(rs.next()) {
					
					Map<String, String> map = new LinkedHashMap<String, String>();
					
					for(int i =1 ; i <= metaData.getColumnCount(); i++) {
						map.put(metaData.getColumnLabel(i), rs.getString(i));
					}

					result.add(map);
				}
				System.out.println("변복완료....");
		
		} catch (SQLException e) {
			throw e;
		}finally {
			if(rs != null) try {rs.close(); } catch (SQLException e) {throw e;}
			if(s != null) try {s.close(); } catch (SQLException e) {throw e;}
			if(con != null) try {con.close(); } catch (SQLException e) {throw e;}

		}
		
		return result;
	}

}
