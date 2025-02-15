package com.kbstar.itsm.ui.model;

import java.awt.Component;
import java.util.Optional;
import java.util.OptionalInt;

import javax.swing.table.TableCellEditor;

import lombok.Builder;



public class JColumn {
	
	private int colIdx;
	
	private String name;
	private TableCellEditor cellEditor;
	
	@Builder.Default
	private OptionalInt width = OptionalInt.empty();
	
	public void setColIdx(int colIdx) {
		this.colIdx = colIdx;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCellEditor(TableCellEditor cellEditor) {
		this.cellEditor = cellEditor;
	}

	public void setWidth(OptionalInt width) {
		this.width = width;
	}

	public Optional<String> getName() {
		return Optional.ofNullable(name);
	}
	
	public Optional<TableCellEditor> getCellEditor() {
		return Optional.ofNullable(cellEditor);
	}
	
	public OptionalInt getWidth() {
		return width;
	}
	
	public int getColIdx() {
		return colIdx;
	}
	
}
