package com.kbstar.itsm.excel;

public enum SheetNames {
	
	 DATA_SHEET("데이터")
	,CODE_SHEET("코드")
	,VAILD_SHEET("검증")
	,BACKUP_SHEET("데이터백업")
	,COMPARE_SHEET("수정된정보");
	

	private String value;
	
	private SheetNames(String value) {
		this.value = value;
	}

	@Override
	public String toString() {		
		return value;
	}
	
	
	

}
