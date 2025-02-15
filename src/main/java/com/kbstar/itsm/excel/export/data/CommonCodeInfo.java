package com.kbstar.itsm.excel.export.data;

import java.util.List;

import org.apache.poi.ss.util.CellRangeAddress;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonCodeInfo {
	
	private List<String> list;
	private CellRangeAddress cellAddr;

}
