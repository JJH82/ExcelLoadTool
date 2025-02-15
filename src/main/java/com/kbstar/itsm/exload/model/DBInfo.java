package com.kbstar.itsm.exload.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DBInfo {
	
	
	String id;
	
	@Builder.Default
	String connectName="";
	String jdbcUrl;
	String userName;
	String password;
	boolean defaultConnectSetting;
	
	
	@Override
	public String toString() {
		return connectName;
	}

}
