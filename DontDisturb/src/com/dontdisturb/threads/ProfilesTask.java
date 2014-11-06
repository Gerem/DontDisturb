package com.dontdisturb.threads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dontdirturb.model.Contact;
import com.dontdirturb.model.Profile;
import com.dontdisturb.activities.ContactActivity;
import com.dontdisturb.activities.ProfileActivity;
import com.dontdisturb.adapter.ProfilesAdapter;
import com.dontdisturb.dao.ProfileDao;
import com.dontdisturb.types.BooleanType;
import com.dontdisturb.utils.Constants;
import com.nmolina.utils.Validations;
import com.ricenbeans.dontdirturb.R;

public class ProfilesTask extends AsyncTask<String, Integer, String>{
	private Activity context;
	private TextView notProfileMsg;
	private ListView listView;
	private ProgressBar loadingBar;
	private List<Profile> list;
	private ProfileDao profileDao;
	private final int CONTACTS	  = 0;
	private final int DELETE	  = 1;
	private final int MODIFY	  = 2;
	
	public ProfilesTask(Activity context){
		this.context 		= context;
		this.listView		= (ListView) context.findViewById(R.id.profileList);
		this.loadingBar 	= (ProgressBar) context.findViewById(R.id.loadingBar);
		this.notProfileMsg 	= (TextView) context.findViewById(R.id.textView2);
		this.list			= new ArrayList<Profile>();
		this.loadingBar.setVisibility(View.VISIBLE);
		this.notProfileMsg.setVisibility(View.GONE);
		this.profileDao = new ProfileDao(context);
	}
	@Override
	protected String doInBackground(String... arg0) {		 
		list = (List<Profile>) profileDao.getAll();
		return null;
	}
	@Override
	protected void onPostExecute(String result) {				
		if(Validations.validateListIsNullOrEmpty(list)){
			notProfileMsg.setVisibility(View.VISIBLE);
		}else{
			listView.setVisibility(View.VISIBLE);
			ProfilesAdapter adapter = new ProfilesAdapter(context, R.layout.profile_list, list);
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			
			listView.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	                final Profile p = (Profile) parent.getAdapter().getItem(position);
	                CharSequence colors[] = new CharSequence[] {context.getString(R.string.contact), 
	                											context.getString(R.string.delete), 
	                											context.getString(R.string.modify)};
	                final int pos = position;
	                AlertDialog.Builder builder = new AlertDialog.Builder(context);	                
	                builder.setItems(colors, new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	
	                        if(which==DELETE){
	                        	profileDao.delete(p.getProfileId());
	                        	list.remove(pos);
	                        	ProfilesAdapter adapter = new ProfilesAdapter(context, R.layout.profile_list, list);
	                			listView.setAdapter(adapter);
	                			adapter.notifyDataSetChanged();
	                        }else if(which==MODIFY){
	                            Intent intent = new Intent(context, ProfileActivity.class);
	                            intent.putExtra(Constants.IND_MODIFY_OPTION,    BooleanType.YES.getCode());
	                            intent.putExtra(Constants.COLUMN_NAME, 			p.getProfileName());
	                            intent.putExtra(Constants.COLUMN_ID, 			p.getProfileId());	                            
	                            intent.putExtra(Constants.COLUMN_BLOCK_CALLS, 	p.getBlockCalls());
	                            intent.putExtra(Constants.COLUMN_BLOCK_SMS, 	p.getBlockSMS());
	                            intent.putExtra(Constants.COLUMN_INIT_SCHEDULE, p.getInitSchedule());
	                            intent.putExtra(Constants.COLUMN_END_SCHEDULE, 	p.getEndSchedule());
	                            context.startActivity(intent);
	                        } if(which==CONTACTS){
	                        	// Start main activity
	                            Intent intent = new Intent(context, ContactActivity.class);
	                            intent.putExtra(Constants.COLUMN_PROFILE_FK,	p.getProfileId());
	                            intent.putExtra(Constants.COLUMN_NAME, 			p.getProfileName());
	                            context.startActivity(intent); 
	                        }
	                    }
	                });
	                builder.show();
	            }
	        });
		}
		loadingBar.setVisibility(View.GONE);		
	}
	
	
}
