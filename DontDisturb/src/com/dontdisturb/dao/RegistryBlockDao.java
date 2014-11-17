package com.dontdisturb.dao;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.dontdirturb.model.RegistryBlock;
import com.dontdisturb.utils.Constants;
import com.dontdisturb.utils.GenericDao;
import com.nmolina.utils.Utils;

public class RegistryBlockDao extends GenericDao{

	
	public RegistryBlockDao(Context context) {
		super(context);
	}
	public RegistryBlockDao(Activity context) {
		super(context);
	}
	@Override
	public List<?> getAll() {
		open();
		List<RegistryBlock> blocks = new ArrayList<RegistryBlock>();
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append(Constants.COLUMN_ID_REGISTRY 		+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_CONTACT_NAME 		+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_CONTACT_PHONE		+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_CONTACT_IMAGE 	+ Constants.SEPARATOR);	    	    
		query.append(Constants.COLUMN_MSG				+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_CALL_DURATION 	+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_NAME				+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_REGISTRY_DATE 	+ Constants.SEPARATOR);
		query.append(Constants.COLUMN_REGISTRY_TYPE);		
		query.append(" FROM ");
		query.append(Constants.TABLE_REGISTRY_BLOCK);
		query.append(" ORDER BY " + Constants.COLUMN_REGISTRY_DATE + " DESC ");		
		Cursor cursor = connection.rawQuery(query.toString(),null);
		cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	blocks.add(getBlocks(cursor));
	    	cursor.moveToNext();
	    }
	    cursor.close();
	    close();
		return blocks;
	}
	
	public RegistryBlock getBlocks(Cursor cursor){
		RegistryBlock block = new RegistryBlock();
		
		block.setIdRegistryBlockPk(cursor.getLong(0));
		block.setContactName(cursor.getString(1));
		block.setContactPhone(cursor.getString(2));
		block.setImageUri(cursor.getString(3));
		block.setMsg(cursor.getString(4));
		block.setCallTime(cursor.getString(5));
		block.setProfile(cursor.getString(6));
		block.setRegistryDate(Utils.stringToDate(cursor.getString(7),  "dd/MM/yyyy hh:mm a"));
		block.setBlockType(cursor.getInt(8));						
		return block;
	}
	
	@Override
	public Object insert(Object o) {
		open();
		RegistryBlock registryBlock = (RegistryBlock) o;
		ContentValues values = new ContentValues();		
		String now = Utils.getNow("dd/MM/yyyy hh:mm a");
		values.put(Constants.COLUMN_CONTACT_NAME, 			registryBlock.getContactName());
	    values.put(Constants.COLUMN_CONTACT_PHONE, 			registryBlock.getContactPhone());
	    values.put(Constants.COLUMN_CONTACT_IMAGE, 			registryBlock.getImageUri());
	    
	    values.put(Constants.COLUMN_MSG, 					registryBlock.getMsg());
	    values.put(Constants.COLUMN_CALL_DURATION, 			registryBlock.getCallTime());
	    values.put(Constants.COLUMN_NAME, 					registryBlock.getProfile());
	    values.put(Constants.COLUMN_REGISTRY_TYPE, 			registryBlock.getBlockType());
	    
	    values.put(Constants.COLUMN_REGISTRY_DATE, now);	    	    
	    
	    long insertId = connection.insert(Constants.TABLE_REGISTRY_BLOCK, null,values); // Inserting to DB
	    close();
	    registryBlock.setIdRegistryBlockPk(insertId);
		return  registryBlock;
	}

	@Override
	public void delete(Long id) {
		open();
		connection.delete(Constants.TABLE_REGISTRY_BLOCK, Constants.COLUMN_ID_REGISTRY + " = " + id, null);
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

	

}
