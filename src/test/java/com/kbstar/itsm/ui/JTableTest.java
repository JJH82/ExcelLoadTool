package com.kbstar.itsm.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JTable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kbstar.itsm.ui.model.MapModel;

public class JTableTest {
	
	
	private	JTable  grid;
	
	@BeforeEach
	public void setup() {
		
		JTable  grid = new JTable();	
	}
	
	
	
	
	@Test
	public void test그리드_헤드표출() throws InterruptedException {
		
		JTable  grid = new JTable();
		Vector<Map<String,String>> codeList = new Vector<Map<String,String>>();
		
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("key5", "value1");
		map.put("key4", "value2");
		map.put("1", "value3");
		codeList.add(map);
				
		grid.setModel(MapModel.builder()
						.dataList(codeList)
						.build()
					);
		
		
		assertTrue(grid.getModel().getRowCount()>0);
		assertEquals(grid.getColumnName(0), "key5");
		assertEquals(grid.getColumnName(1), "key4");
		
	}
	
	
	

}
