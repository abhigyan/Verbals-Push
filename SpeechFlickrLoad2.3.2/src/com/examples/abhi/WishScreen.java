package com.examples.abhi;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class WishScreen extends Activity{
	private final int TTS_ACTIVITY_REQUEST_CODE=0;
	private final int SPEECH_ACTIVITY_REQUEST_CODE =1;
	String dialog;
	Button wishButton;

	
	ArrayList<String> matches = new ArrayList<String>();
    private ListView wordsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wish);
		
		dialog = getResources().getString(R.string.dia3);
        Intent intent1 = new Intent("com.examples.abhi.TTS");
        intent1.putExtra("key",dialog);
        startActivityForResult(intent1, TTS_ACTIVITY_REQUEST_CODE);
	
		wordsList = (ListView) findViewById(R.id.list);
		wishButton = (Button)findViewById(R.id.wishButton);
		wishButton.setEnabled(false);
		wishButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent ("com.examples.abhi.SPEECH");
				startActivityForResult(intent2, SPEECH_ACTIVITY_REQUEST_CODE);
			}
		});
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode)
		{
		  case TTS_ACTIVITY_REQUEST_CODE:
			 if(resultCode==RESULT_OK){
				 wishButton.setEnabled(true);
				// Toast.makeText(this, "inside TTS", Toast.LENGTH_SHORT).show();
			 }
			 break;
			 
		  case SPEECH_ACTIVITY_REQUEST_CODE:
			  if(resultCode==RESULT_OK){
				   // Toast.makeText(this, "inside Speech", Toast.LENGTH_SHORT).show();			
					Bundle extras = intent.getExtras();
					ArrayList<String> matchesReturn = extras.getStringArrayList("speechResultKey");	
					//Toast.makeText(this, matchesReturn.get(0), Toast.LENGTH_LONG).show();
					wordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,matchesReturn));					
					startConfirmation(matchesReturn.get(0).trim());
			  }	
			  break;
	  
		}
	}	
	
	//Below changing "final String result" to just "String Result"
	protected void startConfirmation(final String result){
		
		String dialogConfirm = getResources().getString(R.string.dia4).concat(result);	
		Intent intent = new Intent("com.examples.abhi.TTS");	
		intent.putExtra("key", dialogConfirm);
		startActivityForResult(intent, TTS_ACTIVITY_REQUEST_CODE);
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setMessage("Shall I fly to the past?");
		//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "Ok button clicked", Toast.LENGTH_LONG).show();	
				
				Bundle extras = new Bundle();
				extras.putString("key", result);
				//extras.putString("key", text);
				//Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
				Intent intent = new Intent("com.examples.abhi.FLICKRLOAD");
				//intent.putExtra("key", extras);
				intent.putExtras(extras);
				startActivity(intent);
			}
		});
		
		alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "Cancel button clicked", Toast.LENGTH_SHORT).show();
				String dialogCancel = getResources().getString(R.string.dia5);			
				Intent intent = new Intent("com.examples.abhi.TTS");	
				intent.putExtra("key", dialogCancel);
				startActivityForResult(intent, TTS_ACTIVITY_REQUEST_CODE);
			}
		});
		alertbox.show();
	}

	
}
