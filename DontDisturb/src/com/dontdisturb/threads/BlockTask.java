package com.dontdisturb.threads;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.dontdirturb.model.Profile;
import com.dontdirturb.model.RegistryBlock;
import com.dontdisturb.activities.ContactActivity;
import com.dontdisturb.activities.ProfileActivity;
import com.dontdisturb.adapter.BlockAdapter;
import com.dontdisturb.adapter.ProfilesAdapter;
import com.dontdisturb.dao.RegistryBlockDao;
import com.dontdisturb.types.BooleanType;
import com.dontdisturb.utils.Constants;
import com.nmolina.utils.Validations;
import com.ricenbeans.dontdirturb.R;

public class BlockTask extends AsyncTask<String, Integer, String>{
	private Activity context;
	private List<RegistryBlock> registryBlocks;
	private TextView unwantedCallsMsg;
	private ProgressBar loadingBar;
	private ListView listView;
	public BlockTask(Activity context){
		this.context 			= context;		
		this.loadingBar 		= (ProgressBar) context.findViewById(R.id.loadingUnwanted);
		this.unwantedCallsMsg 	= (TextView) context.findViewById(R.id.unwantedCallsMsg);	
		this.listView			= (ListView) context.findViewById(R.id.unwantedLst);
		unwantedCallsMsg.setVisibility(View.GONE);
		loadingBar.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected String doInBackground(String... params) {
		RegistryBlockDao blockDao = new RegistryBlockDao(context);
		registryBlocks = (List<RegistryBlock>) blockDao.getAll();
		return null;
	}
	@Override
	protected void onPostExecute(String result) {				
		if(Validations.validateListIsNullOrEmpty(registryBlocks)){
			loadingBar.setVisibility(View.GONE);
			unwantedCallsMsg.setVisibility(View.VISIBLE);
		}else{
			loadingBar.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			BlockAdapter adapter = new BlockAdapter(context, R.layout.contact_list, registryBlocks);
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			
			listView.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	                final RegistryBlock r = (RegistryBlock) parent.getAdapter().getItem(position);
	                CharSequence colors[] = new CharSequence[] {context.getString(R.string.delete)};
	                final int pos = position;
	                AlertDialog.Builder builder = new AlertDialog.Builder(context);	                
	                builder.setItems(colors, new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	
	                        if(which==1){
	                        	RegistryBlockDao blockDao = new RegistryBlockDao(context);
	                        	blockDao.delete(r.getIdRegistryBlockPk());
	                        	registryBlocks.remove(pos);
	                        	BlockAdapter adapter = new BlockAdapter(context, R.layout.profile_list, registryBlocks);
	                			listView.setAdapter(adapter);
	                			adapter.notifyDataSetChanged();
	                			
	                        }
	                    }
	                });
	                builder.show();
	            }
	        });
		}
	}
}
