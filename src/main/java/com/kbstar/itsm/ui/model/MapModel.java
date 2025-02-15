package com.kbstar.itsm.ui.model;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class MapModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	
	private Vector<Map<String,String>> data;
	private String[] headNames;
	private boolean useSelectCheckBox;
	
	private MapModel(MapModelBuilder b) {
		data = b.data;
		headNames = b.headNames;
		useSelectCheckBox = b.useSelectCheckBox;
		headNames =  (headNames == null) ? getHeadsByListMapKey(data): headNames;
	}
	

	public Vector<Map<String,String>> getData() {
		return data;
	}
	

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}


	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
		if(aValue == null) return ;
		
		data.get(rowIndex).put(getColumnName(columnIndex), aValue.toString());
		super.setValueAt(aValue, rowIndex, columnIndex);
	}


	private String[] getHeadsByListMapKey(List<Map<String, String>> data) {
		
		if(data == null || data.size() == 0 & this.headNames == null) return new String[]{"결과값_없음"};

		int i = 0;
		String[] keys = new String[data.get(0).keySet().size()];		
		for(String key : data.get(0).keySet()) keys[i++] = key;
		
		return keys;
	}
	

	@Override
	public String getColumnName(int column) {
		return headNames[column];
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return headNames.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Map<String,String> oneRow = data.get(row);
		return oneRow.get(headNames[col]);
	}
	
	public static MapModelBuilder builder() {
		return new MapModelBuilder();
	}
	
	public static class MapModelBuilder {
		
		private Vector<Map<String,String>> data = new Vector<Map<String,String>>();
		private String[] headNames;
		private boolean useSelectCheckBox;
		
		public MapModelBuilder dataList(Vector<Map<String,String>> data) {
			this.data = data;
			return this;
		}
		
		public MapModelBuilder headNames(String[] headNames) {
			this.headNames = headNames;
			return this;
		}
		
		public MapModelBuilder useSelectCheckBox(boolean useSelectCheckBox) {
			this.useSelectCheckBox = useSelectCheckBox;
			return this;
		}
		
		public MapModel build() {
			return new MapModel(this);
		}
		
	}

}
