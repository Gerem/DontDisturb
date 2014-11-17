package com.dontdisturb.activities;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.dontdirturb.model.Profile;
import com.dontdisturb.dao.ProfileDao;
import com.dontdisturb.types.BooleanType;
import com.dontdisturb.utils.Constants;
import com.dontdisturb.utils.DontDisturbUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.nmolina.utils.Utils;
import com.nmolina.utils.Validations;
import com.ricenbeans.dontdirturb.R;

public class ProfileActivity extends ActionBarActivity {
	// Initializes the RangeBar in the application
    private Button initSchedule;
    private Button endSchedule;
    private EditText profileName;
    private CheckBox blockCalls;
    private CheckBox blockSMS;
    private Profile profile;
    private AdView adView;
    private int indModifyView = 0;
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_profile_activity);		
		initSchedule = (Button) findViewById(R.id.initSchedule);		
		endSchedule  = (Button) findViewById(R.id.endSchedule);
		blockCalls	 = (CheckBox)findViewById(R.id.blockCallsChk);
		blockSMS	 = (CheckBox)findViewById(R.id.blockSmsChk);
		profileName	 = (EditText) findViewById(R.id.profileName);
		configureListener();
		
		adView = new AdView(this);
        adView.setAdUnitId(this.getString(R.string.fBanner));
        adView.setAdSize(AdSize.BANNER);
        AdRequest adRequest = new AdRequest.Builder().build();
        // el atributo android:id="@+id/mainLayout".
        LinearLayout layout = (LinearLayout)findViewById(R.id.fBanner);

        // Añadirle adView.
        layout.addView(adView);
        // Cargar adView con la solicitud de anuncio.
        adView.loadAd(adRequest);
		
		Intent intent = getIntent();
		this.profile = new Profile();
		if(Validations.validateIsNotNull(intent.getExtras())){

			 profile.setProfileId(intent.getLongExtra(Constants.COLUMN_ID,0));
			 indModifyView = intent.getIntExtra(Constants.IND_MODIFY_OPTION,0);
			 profileName.setText(intent.getStringExtra(Constants.COLUMN_NAME));			 
			 			 
			 initSchedule.setText(intent.getStringExtra(Constants.COLUMN_INIT_SCHEDULE));
			 endSchedule.setText(intent.getStringExtra(Constants.COLUMN_END_SCHEDULE));
			 
			 if(BooleanType.YES.getCode().equals(intent.getIntExtra(Constants.COLUMN_BLOCK_SMS,0)))
				 blockSMS.setChecked(true);			 			 
			 if(BooleanType.YES.getCode().equals(intent.getIntExtra(Constants.COLUMN_BLOCK_CALLS,0)))
				 blockCalls.setChecked(true);			
		}
	}
	
	public void registerProfile(){
		if(!validate()){			
			profile.setInitSchedule(initSchedule.getText().toString());
			profile.setEndSchedule(endSchedule.getText().toString());
			profile.setProfileName(profileName.getText().toString());
			if(blockCalls.isChecked())
				profile.setBlockCalls(BooleanType.YES.getCode());
			if(blockSMS.isChecked())
				profile.setBlockSMS(BooleanType.YES.getCode());
			
			profile.setBlockCalls(BooleanType.YES.getCode()); // provisional
			ProfileDao profileDao = new ProfileDao(this);
			if(BooleanType.NO.getCode() == indModifyView){
				profileDao.insert(profile);
				DontDisturbUtils.showCustomToast(this,getString(R.string.profileSavedMsg),null);
			}else if(BooleanType.YES.getCode() == indModifyView){
				profileDao.update(profile);
				DontDisturbUtils.showCustomToast(this,getString(R.string.profileModifiedMsg),null);
			}
			
		}
	}
	
	@Override	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.create_profile_menu, menu);
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
		case R.id.saveItem:
			registerProfile();
			break;
		case R.id.helpBtn:
			String message = getApplicationContext().getString(R.string.profileHelp);
			DontDisturbUtils.showCustomToast(this,message,null);
		default:
			break;
		}

		return true;
	}
	public Boolean validate(){
		 if(Validations.validateIsNullOrEmpty(profileName.getText())){
			 String message = getApplicationContext().getString(R.string.provideProfileName);
			 DontDisturbUtils.showCustomToast(this, message, Color.RED);
			 return Boolean.TRUE;
		 }
		 if(!blockCalls.isChecked() && !blockSMS.isChecked()){
			 String message = getApplicationContext().getString(R.string.noOptionToBlock);
			 DontDisturbUtils.showCustomToast(this, message, Color.RED);
			 return Boolean.TRUE;
		 }
		 if(initSchedule.getText().equals(endSchedule.getText())){
			 String message = getApplicationContext().getString(R.string.rangeTimeError	);
			 DontDisturbUtils.showCustomToast(this, message, Color.RED);
			 return Boolean.TRUE;
		 }
			 
		 return Boolean.FALSE;		 
	}
	
	public void configureListener(){		
		initSchedule.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        
	            // TODO Auto-generated method stub
	            Calendar mcurrentTime = Calendar.getInstance();
	            int hour = mcurrentTime.get(Calendar.HOUR);
	            int minute = mcurrentTime.get(Calendar.MINUTE);
	            TimePickerDialog mTimePicker;
	            mTimePicker = new TimePickerDialog(ProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {	            	
	                @Override
	                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
	                	initSchedule.setText( Utils.getHour12Picker(selectedHour, selectedMinute));	                    
	                }

	            }, hour, minute, false);
	            mTimePicker.setTitle(getString(R.string.selectTime));
	            mTimePicker.show();

	        }			
	    });
		endSchedule.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            Calendar mcurrentTime = Calendar.getInstance();
	            int hour = mcurrentTime.get(Calendar.HOUR);
	            int minute = mcurrentTime.get(Calendar.MINUTE);
	            TimePickerDialog mTimePicker;
	            mTimePicker = new TimePickerDialog(ProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {	            	
	                @Override
	                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
	                	endSchedule.setText( Utils.getHour12Picker(selectedHour, selectedMinute));	                    
	                }

	            }, hour, minute, false);//Yes 24 hour time
	            mTimePicker.setTitle(getString(R.string.selectTime));
	            mTimePicker.show();

	        }			
	    });
	}
}
