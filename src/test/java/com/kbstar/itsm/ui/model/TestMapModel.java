package com.kbstar.itsm.ui.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JTable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestMapModel {
	
	private MapModel mapModel;
	
	@BeforeEach
	public void setUp() {
		
		mapModel = MapModel.builder().dataList(list()).build();
	}
	
	@Test
	public void testValueAt() {
		
		String ouput = (String)mapModel.getValueAt(0, 0);
		assertEquals("value1", ouput);
		
	}
	
	
	@Test
	public void testRowCnt_ColumCnt() {
		
		assertEquals(3, mapModel.getColumnCount());
		assertEquals(1, mapModel.getRowCount());
		
	}
	
	@Test
	public void testColumnName() {
		
		assertEquals("test1", mapModel.getColumnName(0));
		assertEquals("test2", mapModel.getColumnName(1));
		assertEquals("test3", mapModel.getColumnName(2));

		
	}
	
	
	
	private Vector<Map<String,String>> list() {
		
		Vector<Map<String,String>> list = new Vector<Map<String,String>>();
		
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("test1", "value1");
		map.put("test2", "value2");
		map.put("test3", "value2");
		list.add(map);
		
		return  list;
	}
	
	
	@Test
	public void test모델을그리드에적용() throws InterruptedException {
		
		JTable  grid = new JTable();
		Vector<Map<String,String>> codeList = new Vector<Map<String,String>>();
		
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("key5", "value1");
		map.put("key4", "value2");
		map.put("1", "value3");
		codeList.add(map);
				
		grid.setModel(
					MapModel.builder()
					 .dataList(codeList)
					 .build()
				);
		
		
		assertTrue(grid.getModel().getRowCount()>0);
		assertEquals(grid.getColumnName(0), "key5");
		assertEquals(grid.getColumnName(1), "key4");
		
		assertEquals("value1",grid.getModel().getValueAt(0, 0));
		assertEquals("value2",grid.getModel().getValueAt(0, 1));
		assertEquals("value3",grid.getModel().getValueAt(0, 2));
	}
	
	@Test
	public void test데이터가0건인경우() throws InterruptedException {
		
		JTable  grid = new JTable();
		Vector<Map<String,String>> codeList = new Vector<Map<String,String>>();
		
				
		grid.setModel(
					MapModel.builder()
				 .dataList(codeList)
				 .build()
				 );
		
		
		assertTrue(grid.getModel().getRowCount()==0);
		assertEquals(grid.getColumnName(0), "결과값_없음");
		
	}
	

}
