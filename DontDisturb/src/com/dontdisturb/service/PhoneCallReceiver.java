package com.dontdisturb.service;



import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.dontdirturb.model.Contact;
import com.dontdirturb.model.RegistryBlock;
import com.dontdisturb.activities.MainActivity;
import com.dontdisturb.dao.ContactDao;
import com.dontdisturb.dao.RegistryBlockDao;
import com.dontdisturb.types.BooleanType;
import com.dontdisturb.types.RegistryBlockType;
import com.dontdisturb.utils.DontDisturbUtils;
import com.nmolina.utils.Validations;
import com.ricenbeans.dontdirturb.R;

public class PhoneCallReceiver extends BroadcastReceiver {
	Context context = null;
	private static final String TAG = "Phone call";
	private ITelephony telephonyService;
	private String incommingNumber;
	private Contact contact;
	private NotificationManager mNotificationManager;
	private int NOTIFICATION_ID = 100;
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, "Receving....");		
		TelephonyManager telephony 	= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);		
		ContactDao contactDao 		= new ContactDao(context);
		RegistryBlockDao blockDao 	= new RegistryBlockDao(context);
		this.context = context;
		try {
			Class c = Class.forName(telephony.getClass().getName());
			Method m = c.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			
			Bundle b = intent.getExtras();
	        
	        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
	        	incommingNumber = PhoneNumberUtils.formatNumber(b.getString(TelephonyManager.EXTRA_INCOMING_NUMBER));
		        contact = (Contact) contactDao.find(incommingNumber);
				Log.d(TAG, incommingNumber);			
				if(Validations.validateIsNotNull(contact) && BooleanType.YES.getCode().equals(contact.getProfile().getBlockCalls())){			
					boolean isOnRage = DontDisturbUtils.isOnRange(contact.getProfile().getInitSchedule(), contact.getProfile().getEndSchedule());
					if(isOnRage){
						telephonyService = (ITelephony) m.invoke(telephony);
		//				telephonyService.silenceRinger();
						telephonyService.endCall();
						
						//Registering a registry block
						RegistryBlock block = new RegistryBlock();
						block.setContactName(contact.getName());
						block.setContactPhone(contact.getPhone());
						block.setProfile(contact.getProfile().getProfileName());
						block.setBlockType(RegistryBlockType.CALLS.getCode());
						block.setCallTime(new Date().toString());
						blockDao.insert(block);
						displayNotification(contact);
						DontDisturbUtils.deleteLastCallLog(context, incommingNumber);
					}
				
				}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	 protected void displayNotification(Contact contact) {
		// get the notification manager
			mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			Intent notificationIntent = new Intent(context, MainActivity.class);
			PendingIntent onClickIntent = PendingIntent.getActivity(context, Calendar.getInstance().get(Calendar.MILLISECOND),  notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
		     Notification n = new Notification.Builder(context).setContentTitle(contact.getName())
	        .setContentText("Has been blocked by profile " + contact.getProfile().getProfileName())
	        .setWhen(System.currentTimeMillis())
	        .setSmallIcon(R.drawable.ic_launcher)	        
	        .setContentIntent(onClickIntent).build();
			
		  // Cancel the notification after its selected
		     n.flags |= Notification.FLAG_AUTO_CANCEL;
			// show the notification
			mNotificationManager.notify(NOTIFICATION_ID, n);

	   }
	
}