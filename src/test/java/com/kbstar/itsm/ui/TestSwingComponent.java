package com.kbstar.itsm.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JComboBox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kbstar.itsm.exload.model.DBInfo;

public class TestSwingComponent {
	
  
	private Vector<DBInfo> dbList  = new Vector<DBInfo>();
	private DBInfo dbInfoTwo;
	
	
	@BeforeEach
	public void setup() {
		
		dbList.add( 
				DBInfo
				.builder()
				.connectName("테스트")
				.jdbcUrl("tcp://1929")
				.id("egene")
				.password("itsm!test")
				.build()
		);
		
		dbInfoTwo =
				DBInfo
				.builder()
				.connectName("테스트2")
				.jdbcUrl("tcp://1929")
				.id("egene")
				.password("itsm!test")
				.build();

		
		dbList.add( dbInfoTwo);
	}
	
	@Test
	public void testComboViewValue() {
		
		JComboBox<DBInfo> combo = new JComboBox<DBInfo>(dbList);
		combo.setSelectedIndex(1);
		
		assertEquals("테스트2", combo.getSelectedItem().toString()); 
	}
	
	
	@Test
	public void testComboBoxSelectedEvent() {

		JComboBox<DBInfo> combo = new JComboBox<DBInfo>(dbList);
		
		combo.addActionListener((ActionEvent e) -> {
			
			assertEquals("comboBoxChanged", e.getActionCommand());
			assertEquals(dbInfoTwo, combo.getItemAt(combo.getSelectedIndex()));
			
		});
		
		combo.setSelectedIndex(1);

	}
	
	
	

}
