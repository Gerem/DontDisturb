package com.dontdisturb.dao;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.dontdirturb.model.Contact;
import com.dontdisturb.types.BooleanType;
import com.dontdisturb.utils.Constants;
import com.dontdisturb.utils.GenericDao;
import com.nmolina.utils.Utils;

public class ContactDao extends GenericDao{
	int debug = BooleanType.YES.getCode();
	public ContactDao(Activity context) {
		super(context);
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		return null;
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

}
