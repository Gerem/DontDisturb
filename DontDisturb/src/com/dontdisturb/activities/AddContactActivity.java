package com.dontdisturb.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dontdirturb.model.Contact;
import com.dontdisturb.threads.ContactsTask;
import com.dontdisturb.utils.Constants;
import com.dontdisturb.utils.DontDisturbUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.nmolina.utils.Validations;
import com.ricenbeans.dontdirturb.R;

public class AddContactActivity extends ActionBarActivity {
	private Contact contact;
	private Button cancelButton;
	private AdView adView;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_activity);	
		cancelButton = (Button) findViewById(R.id.cancelButton);
		Intent intent = getIntent();
		if(Validations.validateIsNotNull(intent.getExtras())){
			contact = new Contact();			
			contact.setProfileFk(intent.getLongExtra(Constants.COLUMN_PROFILE_FK, 0));
			contact.setContactType(intent.getIntExtra(Constants.CONTACT_TYPE, 0));
										
			ContactsTask contactsTask = new ContactsTask(this, contact); // Getting Contacts from profile
			DontDisturbUtils.executeAsyncTask(contactsTask, null);
		}
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		adView = new AdView(this);
        adView.setAdUnitId(this.getString(R.string.fBanner));
        adView.setAdSize(AdSize.BANNER);
        AdRequest adRequest = new AdRequest.Builder().build();
     // el atributo android:id="@+id/mainLayout".
        LinearLayout layout = (LinearLayout)findViewById(R.id.fBanner);
        layout.setVisibility(View.GONE);
        // Añadirle adView.
        layout.addView(adView);
        // Cargar adView con la solicitud de anuncio.
        adView.loadAd(adRequest);
	}
	@Override	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.appColor))));
		return true;
	}
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);        
        return true;
    }
	
}
