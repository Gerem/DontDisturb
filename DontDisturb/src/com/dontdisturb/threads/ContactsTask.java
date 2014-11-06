package com.dontdisturb.threads;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.PrettyTime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dontdirturb.model.Contact;
import com.dontdisturb.adapter.ContactsAdapter;
import com.dontdisturb.dao.ContactDao;
import com.dontdisturb.types.CallType;
import com.dontdisturb.types.ContactType;
import com.dontdisturb.utils.Constants;
import com.dontdisturb.utils.DontDisturbUtils;
import com.nmolina.utils.Utils;
import com.nmolina.utils.Validations;
import com.ricenbeans.dontdirturb.R;

public class ContactsTask extends AsyncTask<String, Integer, String>{
	private Activity context;
	private TextView contactsHereMsg;
	private ListView listView;
	private ProgressBar loadingBar;
	private List<Contact> list;
	private List<Contact> contactSelected;
	private ContactDao contactDao;	
	private Contact contact;
	private final int DELETE	  = 0;
	private final int MODIFY	  = 1;
	private Button addButton;
	public ContactsTask(Activity context, Contact contact){
		this.context 			= context;
		this.contact			= contact;
		this.list				= new ArrayList<Contact>();
		this.contactSelected	= new ArrayList<Contact>();
		this.listView			= (ListView) context.findViewById(R.id.contactList);
		this.loadingBar 		= (ProgressBar) context.findViewById(R.id.loadingBar);
		this.contactsHereMsg 	= (TextView) context.findViewById(R.id.contactsHereMsg);		
		this.addButton			= (Button)context.findViewById(R.id.addButton);
		
		this.loadingBar.setVisibility(View.VISIBLE);
		this.contactsHereMsg.setVisibility(View.GONE);
		this.contactDao = new ContactDao(context);
		
		addButton();
	}
	@Override
	protected String doInBackground(String... arg0) {		 
		if(ContactType.PROFILE.getCode().equals(contact.getContactType())){
			list = (List<Contact>) contactDao.getAll(contact.getProfileFk());
			for (Contact c : list) {
				c.setContactType(ContactType.PROFILE.getCode());
			}
		} else if(ContactType.CONTACTS.getCode().equals(contact.getContactType())){
			list = this.getContactsList(context);
		} else if(ContactType.CALLS.getCode().equals(contact.getContactType())){
			list = this.getCalls();
		} else if(ContactType.TEXT_MESSAGE.getCode().equals(contact.getContactType())){
			list = getAllSms();
		}
		return null;
	}
	@Override
	protected void onPostExecute(String result) {				
		if(Validations.validateListIsNullOrEmpty(list)){
			contactsHereMsg.setVisibility(View.VISIBLE);
		}else{
			listView.setVisibility(View.VISIBLE);
			final ContactsAdapter adapter = new ContactsAdapter(context, R.layout.contact_list, list);			
			adapter.notifyDataSetChanged();
			listView.setAdapter(adapter);
			if(ContactType.PROFILE.getCode().equals(contact.getContactType())){
				listView.setOnItemClickListener(new OnItemClickListener() {
		            @Override
		            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		            	onClickProfileContact(parent, view, position, id);
		            }
		        });
			}else if(!ContactType.PROFILE.getCode().equals(contact.getContactType())){				
				listView.setOnItemClickListener(new OnItemClickListener() {
		            @Override
		            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {		            	
		            	final Contact c = (Contact) parent.getAdapter().getItem(position);
		            	
		            	if(c.isSelected()){
		            		c.setSelected(false);
		            		contactSelected.remove(c);		            	
		            	}else{		            	
		            		c.setSelected(true);
		            		contactSelected.add(c);
		            	}
		            	adapter.notifyDataSetChanged();
		            }
		        });
			}
		}
		loadingBar.setVisibility(View.GONE);		
	}
	public void addButton(){
		addButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				ContactDao contactDao = new ContactDao(context);
				List<Contact> contactSaved = (List<Contact>) contactDao.getAll(contact.getProfileFk());
				for(Contact c: contactSelected){
					boolean found = false;
					for (Contact contact : contactSaved) {
						if(contact.getPhone().equals(c.getPhone())){
							found = true;
							break;
						}
					}
					if(found)
						break;
					if(Validations.validateIsNull(c.getName())) // Validating contacts without names
						c.setName(c.getPhone());
					
					contactDao.insert(c);
				}
					
				context.finish();
			}
		});
	}
	public void onClickProfileContact (AdapterView<?> parent, View view, int position,long id){
		 final Contact c = (Contact) parent.getAdapter().getItem(position);
         CharSequence colors[] = new CharSequence[] {context.getString(R.string.delete)};		                
         final int pos = position;
         AlertDialog.Builder builder = new AlertDialog.Builder(context);	                
         builder.setItems(colors, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
             	
                 if(which==DELETE){
                 	contactDao.delete(c.getContactId());
                 	list.remove(pos);
                 	ContactsAdapter adapter = new ContactsAdapter(context, R.layout.contact_list, list);
         			listView.setAdapter(adapter);
         			adapter.notifyDataSetChanged();
                 }else if(which==MODIFY){
                    
                 } 
             }
         });
         builder.show();
	}
	public List<Contact> getContactsList(Activity context){
		List<Contact>contacts = new ArrayList<Contact>();
		ContentResolver cr = context.getContentResolver();
    	Cursor phones = cr.query(Phone.CONTENT_URI, null,null,null, null);
        // use the cursor to access the contacts    
        while (phones.moveToNext()){ // Validating if has number
        	if (Integer.parseInt(phones.getString(phones.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
            	Contact contact = new Contact();
            	contact.setName(phones.getString(phones.getColumnIndex(Phone.DISPLAY_NAME)));            	            	
            	contact.setPhone(phones.getString(phones.getColumnIndex(Phone.NUMBER)));
            	contact.setContactType(ContactType.CALLS.getCode());
            	contact.setProfileFk(this.contact.getProfileFk());
            	
            	if(Validations.validateIsNull(contact.getName()))
            		contact.setName(contact.getPhone());
            	
				contact.setImageUri(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)));
//				try {
////					contact.setPhoto(MediaStore.Images.Media.getBitmap(cr,Uri.parse(contact.getImageUri())));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				contacts.add(contact);
        	}        	
        }
        phones.close();
        return contacts;
	}
	
	private List<Contact> getCalls() {
		List<Contact> contacts 	 = new ArrayList<Contact>();
		ContentResolver cr		 = context.getContentResolver();
        Cursor managedCursor 	 = cr.query(CallLog.Calls.CONTENT_URI, null,null, null, null);
        int number 				 = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type 				 = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date 				 = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration 			 = managedCursor.getColumnIndex(CallLog.Calls.DURATION);  
        int name 				 = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int counter = 0;
        while (managedCursor.moveToNext()) {        	
            String phNumber 	= managedCursor.getString(number);
            
            if(phNumber.equals(Constants.PRIVATE_CALL))
            	continue;
            
            boolean found 		= false;
            
            for (Contact contact : contacts) {
				if(contact.getPhone().equals(phNumber)){
					found		= true;
					break;
				}
			}
            if(found)
            	continue;
            
            String callType 	= managedCursor.getString(type);
            String callDate 	= managedCursor.getString(date);
            String contactName	= managedCursor.getString(name);
            Date callDayTime= new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);            
                        
            PrettyTime p = new PrettyTime();
            Contact contact = new Contact();
            
            if(Validations.validateIsNotNull(contactName))
            	contact.setName(contactName);
            else
            	contact.setName(phNumber);
            
            contact.setCallTime(DontDisturbUtils.capitalize(p.format(callDayTime)));
            contact.setContactType(ContactType.CALLS.getCode());
            
            contact.setPhone(phNumber);
            contact.setProfileFk(this.contact.getProfileFk());
            contact.setCallDuration(Utils.secondsToMinutes(Long.valueOf(callDuration)));
            
            
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
            case CallLog.Calls.OUTGOING_TYPE:
            	contact.setCallType(CallType.OUTGOING);
                break;

            case CallLog.Calls.INCOMING_TYPE:
            	contact.setCallType(CallType.INCOMING);
                break;

            case CallLog.Calls.MISSED_TYPE:
            	contact.setCallType(CallType.MISSED);
                break;
            }
            contacts.add(contact);
            ++counter;
            if(counter==50)
        		break;
        }
        managedCursor.close();
        return contacts;
    }
	
	public List<Contact> getAllSms() {
		List<Contact> contacts = new ArrayList<Contact>();
	    
	    Uri message = Uri.parse("content://sms/");
	    ContentResolver cr = context.getContentResolver();

	    Cursor c = cr.query(message, null, null, null, null);

	    while (c.moveToNext()) {
	        
//	        	if(!BooleanType.YES.getCode().equals(c.getColumnIndexOrThrow("type")))
	    	String phNumber		= c.getString(c.getColumnIndexOrThrow("address"));
	    	boolean found 		= false;
            
            for (Contact contact : contacts) {
				if(contact.getPhone().equals(phNumber)){
					found		= true;
					break;
				}
			}
            if(found)
            	continue;
            Contact contact = new Contact();
            PrettyTime p = new PrettyTime();
            
            contact.setName(getContactName(context.getApplicationContext(),c.getString(c.getColumnIndexOrThrow("address"))));
            contact.setPhone(c.getString(c.getColumnIndexOrThrow("address")));
            contact.setMsg(c.getString(c.getColumnIndexOrThrow("body")));	            
            contact.setCallTime(c.getString(c.getColumnIndexOrThrow("date")));
            contact.setProfileFk(this.contact.getProfileFk());
            
            String callDate 	= c.getString(c.getColumnIndexOrThrow("date"));
            Date calldayTime	= new Date(Long.valueOf(callDate));
            contact.setCallTime(DontDisturbUtils.capitalize(p.format(calldayTime)));
            contact.setContactType(ContactType.TEXT_MESSAGE.getCode());
            contacts.add(contact);
	        
	    }
	    // else {
	    // throw new RuntimeException("You have no SMS");
	    // }
	    c.close();

	    return contacts;
		
	}
	
	public String getContactName(Context context, String phoneNumber) {
	    ContentResolver cr = context.getContentResolver();
	    Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));
	    Cursor cursor = cr.query(uri,
	            new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
	    if (cursor == null) {
	        return null;
	    }
	    String contactName = null;
	    if (cursor.moveToFirst()) {
	        contactName = cursor.getString(cursor
	                .getColumnIndex(PhoneLookup.DISPLAY_NAME));
	    }
	    if (cursor != null && !cursor.isClosed()) {
	        cursor.close();
	    }
	    return contactName;
	}
}
