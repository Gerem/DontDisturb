package com.dontdisturb.utils;

public class Constants {
	public static String TAB1_ID 					= "TAB1";
	public static String TAB2_ID 					= "TAB2";
	public static String IND_MODIFY_OPTION 			= "indModify";
	
	public static final String DATABASE_NAME 		= "dontdisturb.db";
	public static final int DATABASE_VERSION 		= 13;
		
	public static final String TABLE_PROFILE 		= "profile";
	public static final String COLUMN_ID 			= "profileId";
	public static final String COLUMN_NAME 			= "profileName";
	public static final String COLUMN_INIT_SCHEDULE	= "initSchedule";
	public static final String COLUMN_END_SCHEDULE 	= "endSchedule";
	public static final String COLUMN_BLOCK_CALLS 	= "blockCalls";
	public static final String COLUMN_BLOCK_SMS		= "blockSMS";
	public static final String COLUMN_REGISTRY_DATE	= "registryDat";
	public static final String SEPARATOR		 	= ",";
	
	public static final String TABLE_CONTACT 		= "contact";
	public static final String COLUMN_ID_CONTACT	= "contactId";
	public static final String COLUMN_CONTACT_NAME 	= "contactName";
	public static final String COLUMN_CONTACT_PHONE	= "phone";
	public static final String COLUMN_CONTACT_IMAGE	= "imageUrl";
	public static final String COLUMN_PROFILE_FK	= "profileFk";
	
	public static final String CONTACT_TYPE			= "CONTACT_TYPE";
	public static final String PRIVATE_CALL			= "-2";
	
	
	public static final String TABLE_REGISTRY_BLOCK	= "registry_block";
	public static final String COLUMN_ID_REGISTRY	= "id_registry_block_pk";	
	public static final String COLUMN_REGISTRY_TYPE	= "registry_call_type";
	
	public static final String COLUMN_MSG			= "msg";
	public static final String COLUMN_CALL_DURATION	= "callDuration";
	
	public static final String CREATE_REGISTRY_BLOCK = "create table "
		      + Constants.TABLE_REGISTRY_BLOCK 	+ "(" 
		      + Constants.COLUMN_ID_REGISTRY 	+ " INTEGER primary key autoincrement, " 
		      + Constants.COLUMN_CONTACT_NAME 	+ " VARCHAR(100) not null,"
		      + Constants.COLUMN_CONTACT_PHONE 	+ " VARCHAR(100) not null,"
		      + Constants.COLUMN_CONTACT_IMAGE	+ " VARCHAR(200),"
		      + Constants.COLUMN_MSG			+ " VARCHAR(200),"
		      + Constants.COLUMN_CALL_DURATION	+ " VARCHAR(50),"
		      + Constants.COLUMN_NAME			+ " VARCHAR(100),"	
		      + Constants.COLUMN_REGISTRY_DATE 	+ " DATE,"
		      + Constants.COLUMN_REGISTRY_TYPE 	+ " INTEGER);";
	
	public static final String CREATE_CONTACT_TABLE = "create table "
		      + Constants.TABLE_CONTACT 		+ "(" 
		      + Constants.COLUMN_ID_CONTACT 	+ " INTEGER primary key autoincrement, " 
		      + Constants.COLUMN_CONTACT_NAME 	+ " VARCHAR(100) not null,"
		      + Constants.COLUMN_CONTACT_PHONE 	+ " VARCHAR(100) not null,"
		      + Constants.COLUMN_CONTACT_IMAGE	+ " VARCHAR(200),"
		      + Constants.COLUMN_PROFILE_FK		+ " INTEGER,"	
		      + Constants.COLUMN_REGISTRY_DATE 	+ " DATE,"
		      + Constants.CONTACT_TYPE 			+ " INTEGER);";
	
	public static final String CREATE_PROFILE_TABLE = "create table "
		      + Constants.TABLE_PROFILE 		+ "(" 
		      + Constants.COLUMN_ID 			+ " INTEGER primary key autoincrement, " 
		      + Constants.COLUMN_NAME 			+ " VARCHAR(100) not null,"
		      + Constants.COLUMN_INIT_SCHEDULE	+ " VARCHAR(20),"
		      + Constants.COLUMN_END_SCHEDULE 	+ " VARCHAR(20),"
		      + Constants.COLUMN_REGISTRY_DATE 	+ " DATE,"
		      + Constants.COLUMN_BLOCK_CALLS 	+ " INT(1) not null,"
		      + Constants.COLUMN_BLOCK_SMS 		+ " INT(1) not null);";
}
