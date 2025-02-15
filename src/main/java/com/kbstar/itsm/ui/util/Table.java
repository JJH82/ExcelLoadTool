package com.kbstar.itsm.ui.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;

public class Table {
	
	private final Container target;
	
	private static Map<String, Component> elements = new HashMap<String,Component>();
	
	public Table(Builder lb) {
		
		this.target = lb.target;
		
		if (lb.isTableStyle) setLayout(lb);
		else setNonLayout(lb);
	
	}

	private void setLayout(Builder lb) {
		// default 레이아웃
		lb.target.setLayout(new GridBagLayout());

		//레아웃 생성
		int row= 0;
		for(List<TD> td : lb.rows) {
			
			int col=0;
			for(TD node : td) {
				
				node.getLayoutConst().gridy = row;
				node.getLayoutConst().gridx = col;
				lb.target.add(node.getComponent(),node.getLayoutConst());
							
				putElementsMap(lb.target.getName(),node);
				
				col++;
			}
			row++;
		}
		

		// TD등록 불가능 요소(Element) elements에 추가
		lb.elements.keySet().forEach(o -> elements.put(getEnumName(o), lb.elements.get(o)));
		
	}
	
	private void setNonLayout(Builder lb) {
		for(List<TD> td : lb.rows) {	
			for(TD node : td) {
				lb.target.add(node.getComponent());
				putElementsMap(lb.target.getName(),node);
			}
		}
	}
	
	private void putElementsMap(String parentName, TD node) {
		if (node.getComponent().getName().isEmpty()) return;
		elements.put(node.getComponent().getName(), node.getComponent());
	}
	
	
	public Container getTarget() {
		return target;
	}
	
	public static Component getElementByName(String name) {
		return elements.get(name);
	}
	
	public static <T> T getElementByName(String name, Class<T> requiredType) {
		return (T)elements.get(name);
	}

	public static Component getElementByName(Enum name) {
		return elements.get(getEnumName(name));
	}
	
	public static <T> T getElementByName(Enum name, Class<T> requiredType) {
		return (T)elements.get(getEnumName(name));
	}

	private static String getEnumName(Enum name) {
		return name.getDeclaringClass() + "." + name.toString();
	}

	public static <T> T getElementByNameInJScrollPanel(Enum name, Class<T> requiredType) {
		return getElementByNameInJScrollPanel(name, requiredType ,0);
	}
	
	public static <T> T getElementByNameInJScrollPanel(Enum name, Class<T> requiredType, int idx) {
		JScrollPane panel = ((JScrollPane)elements.get(getEnumName(name)));
		return (T)panel.getViewport().getComponent(idx);
	}
	
	
	public static class Builder{
		
		private boolean isTableStyle = true;
		
		private Container target;
		private List<TD> cols = new ArrayList<TD>();
		private List<List<TD>> rows = new ArrayList<List<TD>>();
		private Map<Enum,Component> elements = new HashMap<>();
		
		
		public Builder(Container container) {
			this.target = container;
			this.target.setName("");
		}
		
		public Builder(String name, Container container) {
			this.target = container;
			this.target.setName(name);
		}
		
		public Builder(Enum name,Container component ) {
			this.target = component;
			this.target.setName(name.getDeclaringClass() + "." + name.toString());
		}

		public Builder tableStyle(boolean talbeStyle) {
			this.isTableStyle = talbeStyle;
			return this;
		}
		
		public Builder addElement(Enum name, Component c) {

			elements.put(name, c);

			return this;
		}
		
		public Builder tr() {
			cols = new ArrayList<TD>();
			rows.add(cols);
			return this;
		}
		
		public Builder td(TD p) {
			this.cols.add(p);
			return this;
		}
		
		
		public Container build() {
			return new Table(this).getTarget();
		}
	}

}
