package com.kbstar.itsm.excel.loader;

import java.util.EnumSet;

import com.kbstar.itsm.excel.SheetNames;

public enum SqlMode {

	INSERT(SheetNames.DATA_SHEET),
	UPDATE(SheetNames.DATA_SHEET),
	RECOVERY(SheetNames.BACKUP_SHEET);
	
	private Enum<SheetNames> value;
	
	private SqlMode(Enum<SheetNames> value) {
		this.value = value;
	}
	
	public String getSheetName() {
		return value.toString();
	}
	
	public static SqlMode getSqlMode(String value ) {
		
		for(SqlMode sqlMode : EnumSet.allOf(SqlMode.class)) {
			if (sqlMode.name().equals(value) ) return sqlMode;			
		}

		return RECOVERY; 
	}
	
	
}
