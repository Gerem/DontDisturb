package com.dontdisturb.adapter;

import java.util.List;

import org.ocpsoft.prettytime.PrettyTime;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dontdirturb.model.RegistryBlock;
import com.dontdisturb.types.RegistryBlockType;
import com.nmolina.utils.Validations;
import com.ricenbeans.dontdirturb.R;

public class BlockAdapter extends ArrayAdapter<RegistryBlock>{
	private Activity context;
	private int layoutResourceId;
	private List<RegistryBlock> list;
	PrettyTime p = new PrettyTime();
	public BlockAdapter(Activity context, int layoutResourceId,List<RegistryBlock> list) {
		super(context, layoutResourceId, list);
		this.context 			= context;
		this.layoutResourceId 	= layoutResourceId;
		this.list = list;
	} 
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RegistryBlock block = list.get(position);
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
		
		holderView.selected.setVisibility(View.GONE);		
		holderView.name.setText(block.getContactName());
		
		if(RegistryBlockType.CALLS.getCode().equals(block.getBlockType())){
			holderView.photo.setVisibility(View.GONE);			 
			holderView.callDuration.setVisibility(View.VISIBLE);
			holderView.callDuration.setText(context.getString(R.string.cancelledBy) + " " + block.getProfile());
			
			if(Validations.validateIsNotNull(block.getRegistryDate())){
				holderView.timeAgo.setText(p.format(block.getRegistryDate()));			
				holderView.timeAgo.setVisibility(View.VISIBLE);
			}
			holderView.callType.setImageResource(R.drawable.missed);
			holderView.callLayout.setVisibility(View.VISIBLE);
			holderView.callType.setVisibility(View.VISIBLE);
			holderView.callType.getLayoutParams().width  = 40;
			holderView.callType.getLayoutParams().height = 40;
		}
		if(RegistryBlockType.SMS.getCode().equals(block.getBlockType())){
			holderView.photo.setVisibility(View.GONE);
			holderView.callType.setImageResource(R.drawable.ic_message_icon);			
						
			holderView.callType.getLayoutParams().width  = 60;
			holderView.callType.getLayoutParams().height = 60;
			
			if(Validations.validateIsNotNull(block.getRegistryDate())){
				holderView.timeAgo.setText(p.format(block.getRegistryDate()));			
				holderView.timeAgo.setVisibility(View.VISIBLE);
			}
			holderView.callDuration.setText(block.getMsg());			
			holderView.callLayout.setVisibility(View.VISIBLE);
			holderView.callType.setVisibility(View.VISIBLE);
		}				
				
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

