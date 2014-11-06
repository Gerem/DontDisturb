package com.dontdisturb.utils;

import java.util.List;

import android.app.Activity;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dontdisturb.helpers.MySQLiteHelper;

public abstract class GenericDao {
	// Database fields
	public SQLiteDatabase connection;
	private MySQLiteHelper dbHelper;
	public int debug;
	public abstract Object insert(Object o);
	public abstract void delete(Long id);
	public abstract void update(Object o);
	public abstract Object find(Long id);
	public abstract List<?> getAll();
	public GenericDao(Activity context){
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		connection = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
}
