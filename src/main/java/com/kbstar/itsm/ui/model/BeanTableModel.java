package com.kbstar.itsm.ui.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.table.AbstractTableModel;



public class BeanTableModel<T> extends AbstractTableModel {
	
	private List<T> data;
	private List<String> keys;
	private List<String> headNames;
	

	public List<T> getData() {
		return data;
	}

	public BeanTableModel(List<T> data,List<String> keys) {
		this.data = data;
		this.keys = keys;
	}
	
	private BeanTableModel(builder<T> builder) {
		this.data = builder.data;
		this.keys  = builder.keys;
		this.headNames = builder.headNames;
	}

	@Override
	public String getColumnName(int column) {
		return headNames.get(column);
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return keys.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		
		T oneRow = data.get(row);

		Method[] ms = oneRow.getClass().getMethods();
		for(Method m : ms) {
			if(m.getName().toLowerCase().endsWith(this.keys.get(col).toLowerCase()))
			try {
				return m.invoke(oneRow);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
		
		
		return "";
	}
	
	public static class builder<T> {
		
		private final List<T> data;
		private final List<String> keys;
		private List<String> headNames;
		
		public builder(List<T> data,List<String> keys) {
			this.data = data;
			this.keys = keys;
			this.headNames = keys;
			
		}
				
		public builder<T> headNames(List<String> headNames) {
			this.headNames = headNames;
			return this;
		}
		
		public BeanTableModel<T> build() {
			return new BeanTableModel<T>(this);
		}
		
	}
	
	public static void main(String args[]) {
		/*
		List<Host> hostList = new ArrayList<Host>();
		hostList.add(
					Host.builder()
					.ip("127.0.0.1")
					.name("PC")
					.port(8888)
					.build()
				);
		
		hostList.add(
				Host.builder()
				.ip("10.2.38.182")
				.name("PC")
				.port(8888)
				.build()
			);
		
		BeanTableModel<Host> h = new BeanTableModel<Host>(hostList,Arrays.asList(new String[] {"ip"}));
		System.out.println("실행전");
		System.out.println(h.getValueAt(0, 0));
		*/
	}

}
