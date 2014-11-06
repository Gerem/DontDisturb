package com.dontdisturb.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dontdirturb.model.Contact;
import com.dontdisturb.types.CallType;
import com.dontdisturb.types.ContactType;
import com.nmolina.utils.Validations;
import com.ricenbeans.dontdirturb.R;

public class ContactsAdapter extends ArrayAdapter<Contact>{
	private Activity context;
	private int layoutResourceId;
	private List<Contact> list;
	public ContactsAdapter(Activity context, int layoutResourceId,List<Contact> list) {
		super(context, layoutResourceId, list);
		this.context 			= context;
		this.layoutResourceId 	= layoutResourceId;
		this.list = list;
	} 
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		Contact contact = list.get(position);
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
		}
		HolderView holderView = new HolderView();
		holderView.name    	     = (TextView) row.findViewById(R.id.contactName);		
		holderView.selected      = (CheckBox) row.findViewById(R.id.chkContact);
		holderView.callDuration  = (TextView) row.findViewById(R.id.callDuration);
		holderView.timeAgo		 = (TextView) row.findViewById(R.id.timeAgo);
		holderView.callType  	 = (ImageView) row.findViewById(R.id.callType);
		holderView.photo  	 	 = (ImageView) row.findViewById(R.id.photo);
		holderView.callLayout  	 = (LinearLayout) row.findViewById(R.id.callTypeLayout);
		
		// If Profile please dont show the checkbox.
		if(ContactType.PROFILE.getCode().equals(contact.getContactType())){
			holderView.selected.setVisibility(View.GONE);
		}else{
			if(contact.isSelected())
				holderView.selected.setChecked(true);
			else
				holderView.selected.setChecked(false);			
		}
		
		if(Validations.validateIsNotNull(contact.getCallType())){
			holderView.photo.setVisibility(View.GONE);
			 
			holderView.callDuration.setText(context.getString(R.string.callDuration) + " " + contact.getCallDuration());
			holderView.timeAgo.setText(contact.getCallTime());
			if(CallType.INCOMING.equals(contact.getCallType())){
				holderView.callType.setImageResource(R.drawable.incoming);
				
			}else if(CallType.OUTGOING.equals(contact.getCallType())){
				holderView.callType.setImageResource(R.drawable.outgoing);
				
			}else if(CallType.MISSED.equals(contact.getCallType())){
				holderView.callType.setImageResource(R.drawable.missed);
			}
			holderView.callLayout.setVisibility(View.VISIBLE);
			holderView.callType.setVisibility(View.VISIBLE);
		}
		if(ContactType.TEXT_MESSAGE.getCode().equals(contact.getContactType())){
			holderView.photo.setVisibility(View.GONE);
			holderView.timeAgo.setVisibility(View.GONE);
			holderView.callDuration.setText(contact.getMsg());			
			holderView.callLayout.setVisibility(View.VISIBLE);
		}
		
		if(Validations.validateIsNotNull(contact.getName()))
			holderView.name.setText(contact.getName());
		else
			holderView.name.setText(contact.getPhone());
				
		return row;
	}
	
	static class HolderView {
		TextView name;
		TextView phone;
		TextView callDuration;
		TextView timeAgo;
		LinearLayout callLayout;
		ImageView callType;
		ImageView photo;
		CheckBox selected;
	}
}

