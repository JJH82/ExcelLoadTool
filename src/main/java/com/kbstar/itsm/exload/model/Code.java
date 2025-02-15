package com.kbstar.itsm.exload.model;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;


@Getter
@Builder
public class Code {
	
	@Builder.Default
	public String code = "";
	
	@Builder.Default
	public String value = "";
	
	
	public String toString() {
		
		return (code+value).isEmpty() ? "" :  value+"|"+code;
	}

}
