package com.dontdisturb.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dontdirturb.model.Contact;
import com.dontdisturb.threads.ContactsTask;
import com.dontdisturb.types.ContactType;
import com.dontdisturb.utils.Constants;
import com.nmolina.utils.Validations;
import com.ricenbeans.dontdirturb.R;

public class ContactActivity extends ActionBarActivity {
	private Contact contact;
	private String profileName;
	private final int FROM_REG = 0;
	private final int FROM_CON = 1;
	private final int FROM_MSG = 2;
	private final int FROM_MAN = 3;
	private Activity context;
	private Button cancelButton, addButton;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_activity);
		
		addButton = (Button)findViewById(R.id.addButton);
		cancelButton = (Button)findViewById(R.id.cancelButton);
		addButton.setVisibility(View.GONE);
		cancelButton.setVisibility(View.GONE);
		
		context = this;
		Intent intent = getIntent();
		if(Validations.validateIsNotNull(intent.getExtras())){
			contact = new Contact();
			profileName = intent.getStringExtra(Constants.COLUMN_NAME);
			this.getSupportActionBar().setTitle(profileName);
			
			contact.setProfileFk(intent.getLongExtra(Constants.COLUMN_PROFILE_FK, 0));
			contact.setContactType(ContactType.PROFILE.getCode());						
			ContactsTask contactsTask = new ContactsTask(this, contact); // Getting Contacts from profile
			contactsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
		}
	}
	
	@Override	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.appColor))));
		return true;
	}
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);        
        return true;
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {		
		case R.id.addProfile:
			 CharSequence options[] = new CharSequence[] {getString(R.string.fromRegCalls), 
					 									  getString(R.string.fromContacts), 
					 									  getString(R.string.fromTxtMsg),
					 									  getString(R.string.manually)};
			 
			AlertDialog.Builder builder = new AlertDialog.Builder(this);			
			builder.setItems(options, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == FROM_REG) {
						// Start main activity
			            Intent intent = new Intent(context, AddContactActivity.class);
			            intent.putExtra(Constants.CONTACT_TYPE, ContactType.CALLS.getCode());
			            intent.putExtra(Constants.COLUMN_PROFILE_FK, contact.getProfileFk());
			            context.startActivity(intent); 
					}else if (which == FROM_CON) {
						// Start main activity
			            Intent intent = new Intent(context, AddContactActivity.class);
			            intent.putExtra(Constants.CONTACT_TYPE, ContactType.CONTACTS.getCode());
			            intent.putExtra(Constants.COLUMN_PROFILE_FK, contact.getProfileFk());
			            context.startActivity(intent);  
					}else if (which == FROM_MSG) {
						// Start main activity
			            Intent intent = new Intent(context, AddContactActivity.class);
			            intent.putExtra(Constants.CONTACT_TYPE, ContactType.TEXT_MESSAGE.getCode());
			            intent.putExtra(Constants.COLUMN_PROFILE_FK, contact.getProfileFk());
			            context.startActivity(intent); 
					}else if (which == FROM_MAN) {
						
					}
				}
			});
			builder.show();
		default:
			break;
		}

		return true;
	}
	
}
