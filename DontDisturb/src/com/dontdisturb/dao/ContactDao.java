package com.dontdisturb.dao;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.dontdirturb.model.Contact;
import com.dontdirturb.model.Profile;
import com.dontdisturb.types.BooleanType;
import com.dontdisturb.utils.Constants;
import com.dontdisturb.utils.GenericDao;
import com.nmolina.utils.Utils;
import com.nmolina.utils.Validations;

public class ContactDao extends GenericDao{
	int debug = BooleanType.YES.getCode();
	
	
	public ContactDao(Activity context) {
		super(context);
	}
	public ContactDao(Context context) {
		super(context);
	}
	@Override
	public Object insert(Object o) {
		open();
		Contact contact = (Contact) o;
		ContentValues values = new ContentValues();		
		String now = Utils.getNow(Utils.DATE_PATTERN);
		values.put(Constants.COLUMN_CONTACT_NAME,  contact.getName());
	    values.put(Constants.COLUMN_CONTACT_IMAGE, contact.getImageUri());
	    values.put(Constants.COLUMN_PROFILE_FK,    contact.getProfileFk().toString());
	    values.put(Constants.COLUMN_REGISTRY_DATE, now);
	    values.put(Constants.COLUMN_CONTACT_PHONE, 	contact.getPhone());
	    
	    if(Validations.validateIsNotNull(contact.getContactType()))
	    	values.put(Constants.CONTACT_TYPE, contact.getContactType());
	    	    	    	 	    
	    long insertId = connection.insert(Constants.TABLE_CONTACT, null,values); // Inserting to DB
	    close();
	    contact.setContactId(insertId);
		return  contact;
	}

	@Override
	public void delete(Long id) {
		open();
		connection.delete(Constants.TABLE_CONTACT, Constants.COLUMN_ID_CONTACT + " = " + id, null);
		close();
		
	}

	@Override
	public void update(Object o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object find(Long id) {
		open();
		Contact contact = new Contact();
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append("C." + Constants.COLUMN_ID_CONTACT	    + Constants.SEPARATOR);
		query.append("C." + Constants.COLUMN_CONTACT_NAME	+ Constants.SEPARATOR);
		query.append("C." + Constants.COLUMN_CONTACT_PHONE	+ Constants.SEPARATOR);
		query.append("C." + Constants.COLUMN_CONTACT_IMAGE	+ Constants.SEPARATOR);	    	    
		query.append("C." + Constants.COLUMN_REGISTRY_DATE	+ Constants.SEPARATOR);
		query.append("C." + Constants.COLUMN_PROFILE_FK     + Constants.SEPARATOR);
		query.append("P." + Constants.COLUMN_NAME    		+ Constants.SEPARATOR);
		query.append("P." + Constants.COLUMN_INIT_SCHEDULE  + Constants.SEPARATOR);
		query.append("P." + Constants.COLUMN_END_SCHEDULE);
		query.append(" FROM ");
		query.append(Constants.TABLE_CONTACT + " C" + Constants.SEPARATOR + Constants.TABLE_PROFILE + " P");		
		query.append(" WHERE " + Constants.COLUMN_PROFILE_FK + " = " + Constants.COLUMN_ID);
		
		if(BooleanType.YES.getCode().equals(debug))
			Log.d("SQL",query.toString());
		
		Cursor cursor = connection.rawQuery(query.toString(),null);
		cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	contact = getContactProfile(cursor);
	    	cursor.moveToNext();
	    }
	    cursor.close();
	    close();
	    return contact;
	}

	public Object find(String phone) {
		open();
		Contact contact = null;
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append("C." + Constants.COLUMN_ID_CONTACT	    + Constants.SEPARATOR);
		query.append("C." + Constants.COLUMN_CONTACT_NAME	+ Constants.SEPARATOR);
		query.append("C." + Constants.COLUMN_CONTACT_PHONE	+ Constants.SEPARATOR);
		query.append("C." + Constants.COLUMN_CONTACT_IMAGE	+ Constants.SEPARATOR);	    	    
		query.append("C." + Constants.COLUMN_REGISTRY_DATE	+ Constants.SEPARATOR);
		query.append("C." + Constants.COLUMN_PROFILE_FK     + Constants.SEPARATOR);
		query.append("P." + Constants.COLUMN_NAME    		+ Constants.SEPARATOR);
		query.append("P." + Constants.COLUMN_INIT_SCHEDULE  + Constants.SEPARATOR);
		query.append("P." + Constants.COLUMN_END_SCHEDULE   + Constants.SEPARATOR);
		query.append("P." + Constants.COLUMN_BLOCK_CALLS  	+ Constants.SEPARATOR);
		query.append("P." + Constants.COLUMN_BLOCK_SMS);
		query.append(" FROM ");
		query.append(Constants.TABLE_CONTACT + " C" + Constants.SEPARATOR + Constants.TABLE_PROFILE + " P");		
		query.append(" WHERE " + Constants.COLUMN_ID + " = " + Constants.COLUMN_PROFILE_FK);
		query.append("   AND C." + Constants.COLUMN_CONTACT_PHONE + " = '" + phone + "'");
		
		if(BooleanType.YES.getCode().equals(debug))
			Log.d("SQL",query.toString());
		
		Cursor cursor = connection.rawQuery(query.toString(),null);
		cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	contact = getContactProfile(cursor);
	    	cursor.moveToNext();
	    }
	    cursor.close();
	    close();
	    return contact;
	}
	
	@Override
	public List<?> getAll() {
		return null;
	}
	public List<?> getAll(Long profilePk) {
		open();
		List<Contact> contacts = new ArrayList<Contact>();
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append(Constants.COLUMN_ID_CONTACT	+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_CONTACT_NAME	+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_CONTACT_PHONE	+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_CONTACT_IMAGE	+ Constants.SEPARATOR);	    	    
		query.append(Constants.COLUMN_REGISTRY_DATE	+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_PROFILE_FK);							
		query.append(" FROM ");
		query.append(Constants.TABLE_CONTACT);		
		query.append(" WHERE " + Constants.COLUMN_PROFILE_FK + " = " + profilePk);
		
		if(BooleanType.YES.getCode().equals(debug))
			Log.d("SQL",query.toString());
		
		Cursor cursor = connection.rawQuery(query.toString(),null);
		cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	contacts.add(getContact(cursor));
	    	cursor.moveToNext();
	    }
	    cursor.close();
	    close();
		return contacts;
	}
	public Contact getContact(Cursor cursor){
		Contact contact = new Contact();
		
		contact.setContactId(cursor.getLong(0));
		contact.setName(cursor.getString(1));
		contact.setPhone(cursor.getString(2));
		contact.setImageUri(cursor.getString(3));
		contact.setRegistryDate(Utils.stringToDate(cursor.getString(4),  Utils.DATE_PATTERN));
		contact.setProfileFk(cursor.getLong(5));		
		return contact;
	}
	public Contact getContactProfile(Cursor cursor){
		Contact contact = new Contact();
		Profile profile	= new Profile();
		
		contact.setContactId(cursor.getLong(0));
		contact.setName(cursor.getString(1));
		contact.setPhone(cursor.getString(2));
		contact.setImageUri(cursor.getString(3));
		contact.setRegistryDate(Utils.stringToDate(cursor.getString(4),  Utils.DATE_PATTERN));
		contact.setProfileFk(cursor.getLong(5));
		
		profile.setProfileId(cursor.getLong(5));
		profile.setProfileName(cursor.getString(6));
		profile.setInitSchedule(cursor.getString(7));
		profile.setEndSchedule(cursor.getString(8));
		profile.setBlockCalls(cursor.getInt(9));
		profile.setBlockSMS(cursor.getInt(10));
		contact.setProfile(profile);
		return contact;
	}
}
