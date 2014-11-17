package com.dontdisturb.service;

import java.util.Calendar;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.dontdirturb.model.Contact;
import com.dontdirturb.model.RegistryBlock;
import com.dontdisturb.activities.MainActivity;
import com.dontdisturb.dao.ContactDao;
import com.dontdisturb.dao.RegistryBlockDao;
import com.dontdisturb.types.BooleanType;
import com.dontdisturb.types.RegistryBlockType;
import com.dontdisturb.utils.DontDisturbUtils;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.nmolina.utils.Validations;
import com.ricenbeans.dontdirturb.R;

public class SmsReceiver  extends BroadcastReceiver {
	// Get the object of SmsManager
	final SmsManager sms = SmsManager.getDefault();
	private Uri deleteUri = Uri.parse("content://sms");
	private Context context;
	private NotificationManager mNotificationManager;
	private int NOTIFICATION_ID = 100;
	@Override
	public void onReceive(Context context, Intent intent) {
		// Retrieves a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();
		this.context = context;		
		try {
			abortBroadcast();
		    if (bundle != null) {		         
		        ContactDao contactDao 		= new ContactDao(context);
				RegistryBlockDao blockDao 	= new RegistryBlockDao(context);
				SmsMessage[] msgs = null;
				String messageReceived = "";
				Object[] pdus = (Object[]) bundle.get("pdus");
	            msgs = new SmsMessage[pdus.length];            	            	            
	            
	            for (int i=0; i<msgs.length; i++){
	                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
	                messageReceived += msgs[i].getMessageBody().toString();	                  
	            }
						            			        
	            String phoneNumber = msgs[0].getOriginatingAddress(); 		            		            
	            
	            PhoneNumber phone = PhoneNumberUtil.getInstance().parse(phoneNumber, null);
	            
	            String senderNum = PhoneNumberUtils.formatNumber(String.valueOf(phone.getNationalNumber()));
	            String message = messageReceived;
	            
	            Contact contact = (Contact) contactDao.find(senderNum);
	            
	            Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);	            	            	            
	            
	            if(Validations.validateIsNotNull(contact) && BooleanType.YES.getCode().equals(contact.getProfile().getBlockSMS())){			
					boolean isOnRage = DontDisturbUtils.isOnRange(contact.getProfile().getInitSchedule(), contact.getProfile().getEndSchedule());
					if(isOnRage){													
						//Registering a registry block
						RegistryBlock block = new RegistryBlock();
						block.setContactName(contact.getName());
						block.setContactPhone(contact.getPhone());
						block.setProfile(contact.getProfile().getProfileName());
						block.setBlockType(RegistryBlockType.SMS.getCode());
						block.setCallTime(new Date().toString());
						block.setMsg(message);
						blockDao.insert(block);
						displayNotification(contact);
						
						DontDisturbUtils.deleteSms(DontDisturbUtils.getSmsId(context), context);
					}
				
				}		        
	      } // bundle is null
		 
		} catch (Exception e) {
		    Log.e("SmsReceiver", "Exception smsReceiver" +e);
		     
		}
		
	}

	 protected void displayNotification(Contact contact) {
	// get the notification manager
		mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent notificationIntent = new Intent(context, MainActivity.class);
		PendingIntent onClickIntent = PendingIntent.getActivity(context, Calendar.getInstance().get(Calendar.MILLISECOND),  notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		 Notification n = new Notification.Builder(context).setContentTitle(contact.getName())
		.setContentText("SMS has been blocked by profile " + contact.getProfile().getProfileName())
		    .setWhen(System.currentTimeMillis())
		    .setSmallIcon(R.drawable.ic_launcher)	        
		    .setContentIntent(onClickIntent).build();
			
		  // Cancel the notification after its selected
		 n.flags |= Notification.FLAG_AUTO_CANCEL;
		// show the notification
		mNotificationManager.notify(NOTIFICATION_ID, n);

	 }

}
