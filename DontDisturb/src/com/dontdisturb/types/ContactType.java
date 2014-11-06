package com.dontdisturb.types;

public enum ContactType {
	CALLS(		 new Integer(0),"FROM CALLS"),
	PROFILE(	 new Integer(3),"FROM PROFILE"),	
	CONTACTS(	 new Integer(2),"FROM CONTACTS"),
	MANUALLY(	 new Integer(3),"ADDED MANUALLY"),
	TEXT_MESSAGE(new Integer(1),"FROM TEXT MESSAGE");
	
	
	private final Integer code;
	private final String value;
	
	ContactType(Integer code, String value){
		this.code=code;
		this.value = value;
	}

	public Integer getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}
}
