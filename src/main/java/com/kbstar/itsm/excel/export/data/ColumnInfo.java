package com.kbstar.itsm.excel.export.data;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import lombok.Getter;

@Getter
public class ColumnInfo {
	
	private String sqlColumnName;
	private String codeName;
	private int colIdx;
	private Map<String,String> mataData;
	private List<String> dataList;	
	private List<CellRangeAddress> cellAddressList = new ArrayList<>();
	private CellRangeAddress dataAddr;
	private int size;
	private boolean required;
	private boolean where; // 조건절 여부
	private int freezeColumnIdx;
	
	
	private ColumnInfo(Builder b) {
		this.sqlColumnName = b.sqlColumnName;
		this.codeName = b.codeName;
		this.colIdx = b.colIdx;
		this.mataData = b.mataData;
		this.dataList = b.dataList;
		this.size = b.size;
		this.required = b.required;
		this.where = b.where;
		this.freezeColumnIdx = b.freezeColumnIdx;

		this.dataAddr = new CellRangeAddress(2,this.dataList.size()+1,this.colIdx, this.colIdx);

		for(int i =0; i<this.dataList.size(); i++) {
			cellAddressList.add(new CellRangeAddress(i+2,i+2, this.colIdx,this.colIdx));
		}
		
	}
	
	public static Builder builder() {
		return new Builder();
	}

	
	public static class Builder {
		
		private String sqlColumnName;
		private String codeName;
		private int colIdx;
		private Map<String,String> mataData;
		private List<String> dataList = new ArrayList<>();
		private int size;
		private boolean required;
		private boolean where;
		private int freezeColumnIdx;
		
		public Builder sqlColumnName(String sqlColumnName) {
			this.sqlColumnName = sqlColumnName;
			return this;
		}

		public Builder codeName(String codeName) {
			this.codeName = codeName;
			return this;
		}
		
		public Builder colIdx(int colIdx) {
			this.colIdx =  colIdx;
			return this;
		}
		
		public Builder mataData(Map<String,String> mataData) {
			this.mataData = mataData;
			return this;
		}
		
		public Builder dataList(List<String> dataList) {
			this.dataList = dataList;
			
			return this;
		}
		
		public Builder size(int size) {
			this.size = size> 0 ? size : 20;
			return this;
		}
		
		public Builder required(boolean required) {
			this.required = required;
			return this;
		}
		
		public Builder where(boolean where) {
			this.where = where;
			return this;
		}
		
		public Builder freezeColumnIdx(int freezeColumnIdx) {
			this.freezeColumnIdx = freezeColumnIdx;
			return this;
		}
		
		public ColumnInfo build() {
			return new ColumnInfo(this);
		}
		 
		
	}
}
