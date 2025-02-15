package com.kbstar.itsm.ui.model;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class JGrid extends JTable {
	
	private List<JColumn> columns;
	private String[] headNames;
	private Vector<Map<String,String>> listData = new Vector<>();

	
	private JGrid(JGridBuilder b) {
		columns = b.columns;
		
		initModel();
		initColumnWith();
		initEditorCell();	
	}


	/**
	 * 자동 크기 조절시 최소크기지정
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(100,100);
	}


	private void initModel() {

		setModel(MapModel			
				.builder()
				.dataList(listData)
				.headNames(
							columns
							.stream()
							.map(JColumn::getName)
							.map(Optional::get)
							.toArray(String[]::new)
						)
				.build()
			   );
	}


	private void initColumnWith() {
		columns
		.stream()
		.filter(o -> o.getWidth().isPresent())
		.forEach(o -> getColumnModel().getColumn(o.getColIdx()).setMinWidth(o.getWidth().getAsInt()));
	}


	private void initEditorCell() {
		columns
		.stream()
		.filter(o -> o.getCellEditor().isPresent())
		.forEach(o -> getColumnModel().getColumn(o.getColIdx()).setCellEditor(o.getCellEditor().get() )   );
	}
	
	
	public void setListData(Vector<Map<String,String>> listData) {
		this.listData = listData;
		initModel();
		initColumnWith();
		initEditorCell();
	}

	public static JGridBuilder builder() {
		return new JGridBuilder();
	}
	
	public static class JGridBuilder {
		
		private int colIdx;
		private List<JColumn> columns = new ArrayList<>();
		private JColumn col =  new JColumn();
		
		public JGridBuilder colName(String name) {
			col.setName(name);
			return this;
		}
		
		public JGridBuilder editorCell(TableCellEditor c) {	

			col.setCellEditor(c);
			return this;
		}
		
		public JGridBuilder width(int width) {
			col.setWidth(OptionalInt.of(width));
			return this;
		}
		
		public JGridBuilder nextCol() {
			
			if(!col.getName().isPresent()) return this;
			col.setColIdx(colIdx);
			columns.add(colIdx++,col);
			col = new JColumn();
			
			return this;
		}
		
		
		public JGrid build() {
			return new JGrid(this);
		}
		
	}

}
