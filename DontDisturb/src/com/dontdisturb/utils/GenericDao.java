package com.dontdisturb.utils;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class GenericDao extends SQLiteOpenHelper {	
	public SQLiteDatabase connection;
	public int debug;
	public abstract Object insert(Object o);
	public abstract void delete(Long id);
	public abstract void update(Object o);
	public abstract Object find(Long id);
	public abstract List<?> getAll();
	
	
	public GenericDao(Context context){		
		super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);		

	}
	
	public GenericDao(Activity context){		
		super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);			

	}
	@Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(Constants.CREATE_REGISTRY_BLOCK);
	    database.execSQL(Constants.CREATE_PROFILE_TABLE);
	    database.execSQL(Constants.CREATE_CONTACT_TABLE);
	  }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		    db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CONTACT);
		    db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_PROFILE);
		    db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_REGISTRY_BLOCK);
		    onCreate(db);

	}
	public void open() throws SQLException {
		connection = getWritableDatabase();
	}			
	
	
}
