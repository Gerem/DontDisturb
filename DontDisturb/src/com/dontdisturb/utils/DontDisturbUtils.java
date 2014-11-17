package com.dontdisturb.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.MediaStore;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
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
	public static void showCustomToast(Activity context, String message,
			Integer color) {
		LayoutInflater inflater = context.getLayoutInflater();

		View layout = inflater.inflate(R.layout.custom_toast,
				(ViewGroup) context.findViewById(R.id.custom_toast_layout_id));
		if (Validations.validateIsNotNull(color)) {
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

	public static String capitalize(String line) {
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}

	public List<Contact> getContactsFromList(Activity context) {
		List<Contact> contacts = new ArrayList<Contact>();
		ContentResolver cr = context.getContentResolver();
		Cursor phones = cr.query(Phone.CONTENT_URI, null, null, null, null);
		// use the cursor to access the contacts
		while (phones.moveToNext()) { // Validating if has number
			if (Integer
					.parseInt(phones.getString(phones
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
				Contact contact = new Contact();
				contact.setName(phones.getString(phones
						.getColumnIndex(Phone.DISPLAY_NAME)));
				contact.setPhone(phones.getString(phones
						.getColumnIndex(Phone.NUMBER)));
				contact.setImageUri(phones.getString(phones
						.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)));
				try {
					contact.setPhoto(MediaStore.Images.Media.getBitmap(cr,
							Uri.parse(contact.getImageUri())));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				contacts.add(contact);
			}
		}
		return contacts;
	}

	public static boolean isOnRange(String fromTime, String toTime) {
		DateFormat dateFormat = new SimpleDateFormat("h:mm a");
		try {
			Date date1, date2, dateNueva;
			date1 = dateFormat.parse(fromTime);
			date2 = dateFormat.parse(toTime);
			String now = dateFormat.format(new Date());
			dateNueva = dateFormat.parse(now);

			if ((date1.compareTo(dateNueva) <= 0)&& (date2.compareTo(dateNueva) >= 0)) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static String [] getSmsId(Context context){
		Uri uri = Uri.parse("content://sms/inbox");
		Cursor cursor = context.getContentResolver().query(uri, null, null, null,null);
		String [] ids = new String[2];
		while (cursor.moveToFirst()) {
			ids[0] = String.valueOf(cursor.getInt(0));
			ids[1] = String.valueOf(cursor.getInt(1));
			break;
		}
        return ids;
	}
	public static boolean deleteSms(String[] ids, Context context) {
		boolean isSmsDeleted = false;
		try {
			Uri thread = Uri.parse( "content://sms");
			context.getApplicationContext().getContentResolver().delete(Uri.parse("content://sms/" + ids[0]), null, null);
			isSmsDeleted = true;

		} catch (Exception ex) {
			isSmsDeleted = false;
		}
		return isSmsDeleted;
	}
	public static void deleteLastCallLog(Context context, String phoneNumber) {

        try {
            //Thread.sleep(4000);            
            Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,null, null, CallLog.Calls.DATE + " DESC");

            if (cursor.moveToFirst()) {
                    int idOfRowToDelete = cursor.getInt(cursor.getColumnIndex(CallLog.Calls._ID));
                    String phNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    phNumber = PhoneNumberUtils.formatNumber(phNumber);
                    if(phNumber.equals(phoneNumber)){
                    	context.getContentResolver().delete(CallLog.Calls.CONTENT_URI,CallLog.Calls._ID + " = ? ",new String[] { String.valueOf(idOfRowToDelete) });                    	
                    }
            }
        } catch (Exception ex) {
            Log.v("deleteNumber","Exception, unable to remove # from call log: "+ ex.toString());
        }
    }

	public static <T> void executeAsyncTask(AsyncTask<T, ?, ?> task,T... params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		} else {
			task.execute(params);
		}
	}
}
