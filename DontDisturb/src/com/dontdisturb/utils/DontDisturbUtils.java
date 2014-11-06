package com.dontdisturb.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dontdirturb.model.Contact;
import com.nmolina.utils.Validations;
import com.ricenbeans.dontdirturb.R;


public class DontDisturbUtils {
	public static void showCustomToast(Activity context, String message, Integer color){
		LayoutInflater inflater = context.getLayoutInflater();

        View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) context.findViewById(R.id.custom_toast_layout_id));
        if(Validations.validateIsNotNull(color)){
        	layout.setBackgroundColor(color);
        }
        // set a message
        TextView text = (TextView) layout.findViewById(R.id.custom_toast_text);
        text.setText(message);

        // Toast...
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
	}
	public static String capitalize(String line)
	{
	  return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
	public List<Contact> getContactsFromList(Activity context){
		List<Contact>contacts = new ArrayList<Contact>();
		ContentResolver cr = context.getContentResolver();
    	Cursor phones = cr.query(Phone.CONTENT_URI, null,null,null, null);
        // use the cursor to access the contacts    
        while (phones.moveToNext()){ // Validating if has number
        	if (Integer.parseInt(phones.getString(phones.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
            	Contact contact = new Contact();
            	contact.setName(phones.getString(phones.getColumnIndex(Phone.DISPLAY_NAME)));
				contact.setPhone(phones.getString(phones.getColumnIndex(Phone.NUMBER)));
				contact.setImageUri(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)));
				try {
					contact.setPhoto(MediaStore.Images.Media.getBitmap(cr,Uri.parse(contact.getImageUri())));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				contacts.add(contact);
        	}        	
        }
        return contacts;
	}
}
