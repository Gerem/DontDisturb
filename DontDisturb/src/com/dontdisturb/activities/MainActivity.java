package com.dontdisturb.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.dontdisturb.threads.BlockTask;
import com.dontdisturb.threads.ProfilesTask;
import com.dontdisturb.utils.Constants;
import com.dontdisturb.utils.DontDisturbUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.ricenbeans.dontdirturb.R;


public class MainActivity extends ActionBarActivity {
	private MenuItem addProfileMenu;	
	private InterstitialAd interstitial;
	private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProfilesTask profilesTask = new ProfilesTask(this);
        DontDisturbUtils.executeAsyncTask(profilesTask, null);
        
        BlockTask blockTask = new BlockTask(this);
        DontDisturbUtils.executeAsyncTask(blockTask, null);        
        configureTabs();
        loadInterstitial();
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
    }

    public void configureTabs(){
    	 TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
         tabs.setup();
          
         TabHost.TabSpec spec=tabs.newTabSpec(Constants.TAB1_ID);
         spec.setContent(R.id.tab1);
         spec.setIndicator(getString(R.string.blocked));
         tabs.addTab(spec);
          
         spec=tabs.newTabSpec(Constants.TAB2_ID);
         spec.setContent(R.id.tab2);
         spec.setIndicator(getString(R.string.profiles));
         tabs.addTab(spec);
          
         tabs.setCurrentTab(0);
         
         tabs.setOnTabChangedListener(new OnTabChangeListener() {
             public void onTabChanged(String tabId) {
                 if(Constants.TAB2_ID.equals(tabId)){
                	 addProfileMenu.setVisible(true);
         		 } else {
         			addProfileMenu.setVisible(false);
         		 }
             }
         });
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        addProfileMenu = menu.findItem(R.id.addProfile);     
        addProfileMenu.setVisible(false);
                
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);          
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.appColor))));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addProfile) {
        	// Start main activity
            Intent intent = new Intent(this, ProfileActivity.class);
            this.startActivity(intent);            
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	adView.pause();    	
    }

    public void onDestroy(){
    	super.onDestroy();
    	displayInterstitial();
    }
    @Override
    public void onResume(){
    	super.onResume();  
    	adView.resume();  
    	ProfilesTask profilesTask = new ProfilesTask(this);
        DontDisturbUtils.executeAsyncTask(profilesTask, null);
        
        BlockTask blockTask = new BlockTask(this);
        DontDisturbUtils.executeAsyncTask(blockTask, null);
    }
    
    
    public void loadInterstitial(){
    	// Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(this.getString(R.string.fullScreen));

        // Create ad request.
        AdRequest interstitialRequest = new AdRequest.Builder().build();

        // Begin loading your interstitial.
        interstitial.loadAd(interstitialRequest);        
    }
 // Invoke displayInterstitial() when you are ready to display an interstitial.
    public void displayInterstitial() {
	  if (interstitial.isLoaded()) {		  
		  interstitial.show();
	  }
    }
}
