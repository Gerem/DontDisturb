package com.dontdisturb.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dontdirturb.model.Profile;
import com.ricenbeans.dontdirturb.R;

public class ProfilesAdapter extends ArrayAdapter<Profile>{
	private Activity context;
	private int layoutResourceId;
	private List<Profile> list;
	public ProfilesAdapter(Activity context, int layoutResourceId,List<Profile> list) {
		super(context, layoutResourceId, list);
		this.context 			= context;
		this.layoutResourceId 	= layoutResourceId;
		this.list = list;
	} 
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		Profile profile = list.get(position);
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
		}
		HolderView holderView = new HolderView();
		holderView.profileName    		= (TextView) row.findViewById(R.id.profileListName);
		holderView.profileName.setText(profile.getProfileName());
		
		return row;
	}
	
	static class HolderView {
		TextView profileName;	
	}
}
