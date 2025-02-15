package com.kbstar.itsm.excel.export.data;

import java.io.File;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class WorkbookInfo {
	
	private File exportFile;
	private List<ColumnInfo> ColumnInfoList;
	private int dataRowCount;
	private Map<String,CommonCodeInfo> commonCodeInfoMap;

}
