package com.dontdisturb.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.dontdisturb.threads.ProfilesTask;
import com.dontdisturb.utils.Constants;
import com.ricenbeans.dontdirturb.R;


public class MainActivity extends ActionBarActivity {
	private MenuItem addProfileMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProfilesTask profilesTask = new ProfilesTask(this);
        profilesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        configureTabs();
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
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.appColor))));
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
}
