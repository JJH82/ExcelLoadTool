package com.kbstar.itsm.ui.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.junit.jupiter.api.Test;

public class TestTable {
	
	@Test
	public void test패널에_열로_3개_버튼추가() {
		
		Panel panel = new Panel();
		new Table
		.Builder(panel)
		.tr()
		.td( new TD.Builder(new JButton("테스트1")).build() )
		.td( new TD.Builder(new JButton("테스트2")).build() )
		.td( new TD.Builder(new JButton("테스트3")).build() )
		.build();
		
		assertEquals(panel.getComponentCount(), 3);
		
	}
		
	
	@Test
	public void test컨테이너_이름_미설정() {
		
		JButton test3 = new JButton("테스트3");
		
		new Table
		.Builder(new Panel())
		.tr()
		.td( new TD.Builder( "test1", new JButton("테스트1") ).build() )
		.td( new TD.Builder( "test2", new JButton("테스트2") ).build() )
		.td( new TD.Builder( "test3", test3 ).build() )
		.build();
		
		assertEquals(test3, Table.getElementByName("test3"));
		
		
	}
	
	
	@Test
	public void test컨테이너_이름_설정() {
		
		JButton test3 = new JButton("테스트3");
		
		new Table
		.Builder("panel",new Panel())
		.tr()
		.td( new TD.Builder( "test1", new JButton("테스트1") ).build() )
		.td( new TD.Builder( "test2", new JButton("테스트2") ).build() )
		.td( new TD.Builder( "test3", test3 ).build() )
		.build();
		
		assertEquals(test3, Table.getElementByName("test3"));
		
		
	}
	
	
	
	@Test
	public void test_getByName검색후변수에등록() {
		
	
		JButton test2;
		
		
		new Table
		.Builder("test",new Panel())
		.tr()
		.td( new TD.Builder( "test1", new JButton("테스트1") ).build() )
		.td( new TD.Builder( "test2", new JButton("테스트2") ).build() )
		.td( new TD.Builder( "test3", new JButton("테스트3") ).build() )
		.build();
		
		
		test2 = (JButton)Table.getElementByName("test2");
		assertEquals("테스트2", test2.getText());
		
	}
	
	
	@Test
	public void test_getByName메뉴테스트() {
		
	
		JMenuItem test2;
		
		new Table
		.Builder(new JMenu())
		.tableStyle(false)
		.tr()
		.td( new TD.Builder( "test1", new JMenuItem("테스트1") ).build() )
		.td( new TD.Builder( "test2", new JMenuItem("테스트2") ).build() )
		.td( new TD.Builder( "test3", new JMenuItem("테스트3") ).build() )
		.build();
		
		
		test2 = Table.getElementByName("test2",JMenuItem.class);
		assertEquals("테스트2", test2.getText());
		
	}
	
	

}
