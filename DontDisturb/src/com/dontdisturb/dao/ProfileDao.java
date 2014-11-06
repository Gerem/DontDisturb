package com.dontdisturb.dao;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;

import com.dontdirturb.model.Profile;
import com.dontdisturb.utils.Constants;
import com.dontdisturb.utils.GenericDao;
import com.nmolina.utils.Utils;
public class ProfileDao extends GenericDao{
	
	public ProfileDao(Activity context){
		super(context);
	}
	
	public Object insert(Object o){
		open();
		Profile profile = (Profile) o;
		ContentValues values = new ContentValues();		
		String now = Utils.getNow(Utils.DATE_PATTERN);
		values.put(Constants.COLUMN_NAME, 			profile.getProfileName());
	    values.put(Constants.COLUMN_INIT_SCHEDULE, profile.getInitSchedule().toString());
	    values.put(Constants.COLUMN_END_SCHEDULE, 	profile.getEndSchedule().toString());
	    values.put(Constants.COLUMN_REGISTRY_DATE, now);	    	    
	    values.put(Constants.COLUMN_BLOCK_CALLS, 	profile.getBlockCalls());
	    values.put(Constants.COLUMN_BLOCK_SMS, 	profile.getBlockSMS());	 
	    
	    long insertId = connection.insert(Constants.TABLE_PROFILE, null,values); // Inserting to DB
	    close();
	    profile.setProfileId(insertId);
		return  profile;
	}

	@Override
	public void delete(Long id) {
		open();
		connection.delete(Constants.TABLE_PROFILE, Constants.COLUMN_ID+ " = " + id, null);
		close();
	}
	
	@Override
	public void update(Object o) {
		open();
		Profile profile = (Profile) o;
		ContentValues values = new ContentValues();				
		values.put(Constants.COLUMN_NAME, 			profile.getProfileName());
	    values.put(Constants.COLUMN_INIT_SCHEDULE, 	profile.getInitSchedule().toString());
	    values.put(Constants.COLUMN_END_SCHEDULE, 	profile.getEndSchedule().toString());	    	    	   
	    values.put(Constants.COLUMN_BLOCK_CALLS, 	profile.getBlockCalls());
	    values.put(Constants.COLUMN_BLOCK_SMS, 		profile.getBlockSMS());	 
		connection.update(Constants.TABLE_PROFILE, values, Constants.COLUMN_ID + "=" + profile.getProfileId(),null);		
		close();
	}

	@Override
	public List<?> getAll() {
		open();
		List<Profile> profiles = new ArrayList<Profile>();
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append(Constants.COLUMN_ID 			+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_NAME 			+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_INIT_SCHEDULE	+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_END_SCHEDULE 	+ Constants.SEPARATOR);	    	    
		query.append(Constants.COLUMN_REGISTRY_DATE	+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_BLOCK_CALLS 	+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_BLOCK_SMS);						
		query.append(" FROM ");
		query.append(Constants.TABLE_PROFILE);		
		Cursor cursor = connection.rawQuery(query.toString(),null);
		cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	profiles.add(getProfile(cursor));
	    	cursor.moveToNext();
	    }
	    cursor.close();
	    close();
		return profiles;
	}

	
	@Override
	public Object find(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Profile getProfile(Cursor cursor){
		Profile profile = new Profile();
		
		profile.setProfileId(cursor.getLong(0));
		profile.setProfileName(cursor.getString(1));
		profile.setInitSchedule(cursor.getString(2));
		profile.setEndSchedule(cursor.getString(3));
		profile.setRegistryDate(Utils.stringToDate(cursor.getString(4),  Utils.DATE_PATTERN));
		profile.setBlockCalls(cursor.getInt(5));
		profile.setBlockSMS(cursor.getInt(6));		
		return profile;
	}
}
