package com.kbstar.itsm.exload.model;

import java.util.Map;
import java.util.Vector;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Builder
public class MappingInfo {
	
	@Builder.Default
	private String name = "";
	@Builder.Default
	private String sql = "";
	
	
	@Builder.Default
	private Vector<Map<String,String>> mappingList = new Vector<>();
	
	
	@Builder.Default
	private Vector<String> columnList = new Vector<>();


	@Override
	public String toString() {

		return name.toString();
	}
	

}
