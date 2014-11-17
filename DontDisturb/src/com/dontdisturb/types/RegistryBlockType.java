package com.dontdisturb.types;

public enum RegistryBlockType {
	CALLS(		 new Integer(0)),
	SMS(	     new Integer(1));
	
	
	private final Integer code;
	
	RegistryBlockType(Integer code){
		this.code=code;		
	}

	public Integer getCode() {
		return code;
	}

}
