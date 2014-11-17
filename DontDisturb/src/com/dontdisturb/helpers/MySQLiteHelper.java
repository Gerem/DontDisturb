package com.dontdisturb.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dontdisturb.utils.Constants;

public class MySQLiteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "dontdisturb.db";
	private static final int DATABASE_VERSION = 5;
	private Context context;
	private StringBuilder query;
	// Database creation sql statement
	private static final String CREATE_PROFILE_TABLE = "create table "
	      + Constants.TABLE_PROFILE 		+ "(" 
	      + Constants.COLUMN_ID 			+ " INTEGER primary key autoincrement, " 
	      + Constants.COLUMN_NAME 			+ " VARCHAR(100) not null,"
	      + Constants.COLUMN_INIT_SCHEDULE	+ " VARCHAR(20),"
	      + Constants.COLUMN_END_SCHEDULE 	+ " VARCHAR(20),"
	      + Constants.COLUMN_REGISTRY_DATE 	+ " DATE,"
	      + Constants.COLUMN_BLOCK_CALLS 	+ " INT(1) not null,"
	      + Constants.COLUMN_BLOCK_SMS 		+ " INT(1) not null);";
	  
	private static final String CREATE_CONTACT_TABLE = "create table "
		      + Constants.TABLE_CONTACT 		+ "(" 
		      + Constants.COLUMN_ID_CONTACT 	+ " INTEGER primary key autoincrement, " 
		      + Constants.COLUMN_CONTACT_NAME 	+ " VARCHAR(100) not null,"
		      + Constants.COLUMN_CONTACT_PHONE 	+ " VARCHAR(100) not null,"
		      + Constants.COLUMN_CONTACT_IMAGE	+ " VARCHAR(200),"
		      + Constants.COLUMN_PROFILE_FK		+ " INTEGER,"	
		      + Constants.COLUMN_REGISTRY_DATE 	+ " DATE,"
		      + Constants.CONTACT_TYPE 			+ " INTEGER);";
	  
	private static final String CREATE_REGISTRY_BLOCK = "create table "
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
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.setContext(context);
	}
	
	@Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(CREATE_PROFILE_TABLE);
	    database.execSQL(CREATE_CONTACT_TABLE);
	    database.execSQL(CREATE_REGISTRY_BLOCK);
	  }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		    db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_PROFILE);
		    db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CONTACT);
		    db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_REGISTRY_BLOCK);
		    onCreate(db);

	}
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

}
