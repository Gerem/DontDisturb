package com.dontdisturb.types;

public enum CallType {
	INCOMING(new Integer(0)),
	OUTGOING(new Integer(3)),	
	MISSED(	 new Integer(2));
	
	
	private final Integer code;
	
	CallType(Integer code){
		this.code=code;
	}

	public Integer getCode() {
		return code;
	}

}
