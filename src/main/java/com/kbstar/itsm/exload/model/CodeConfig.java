package com.kbstar.itsm.exload.model;

import java.util.Map;
import java.util.Vector;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CodeConfig {
	
	private String id;
	
	@Builder.Default
	private String name = "";
	
	@Builder.Default
	private String sql = "";
	
	@Builder.Default
	private String parameters = "";
	
	@Builder.Default
	private Vector<Map<String,String>> list = new Vector<Map<String,String>>();
	
	
	public String toString() {
		return name;
	}
	

}
