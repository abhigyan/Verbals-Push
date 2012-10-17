package com.examples.abhi;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class SpeechRecognition extends Activity {
	
	    private static final int REQUEST_CODE = 1234;

		
	    @Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.speech);
			
			//Button speechTestButton = (Button)findViewById(R.id.speechTestButton);
			
			// Disable button if no recognition service is present
	        PackageManager pm = getPackageManager();
	        List<ResolveInfo> activities = pm.queryIntentActivities(
	                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
	        startVoiceRecognitionActivity();
/*
	        if (activities.size() == 0)
	        {
	            speechTestButton.setEnabled(false);
	            speechTestButton.setText("Recognizer not present");
	        }
	        
	        speechTestButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startVoiceRecognitionActivity();
					
				}
			});
*/			
	    }	
			
	   
	    
	    /**
	     * Fire an intent to start the voice recognition activity.
	     */
	    private void startVoiceRecognitionActivity()
	    {
	        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
	        startActivityForResult(intent, REQUEST_CODE);
	    }	
	    
	    
	    /**
	     * Handle the results from the voice recognition activity.
	     */
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data)
	    {
	        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
	        {
	            // Populate the wordsList with the String values the recognition engine thought it heard
	            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS); 
	            //Toast.makeText(this, matches.get(0), Toast.LENGTH_SHORT).show();
	            Intent intent = new Intent();
	            Bundle extras = new Bundle();
	            extras.putStringArrayList("speechResultKey", matches);
	            intent.putExtras(extras);
	            //intent.putExtra("speechResultKey",temp);
	            //intent.putStringArrayListExtra("speechResultKey", matches);
	            setResult(RESULT_OK,intent);
	            finish();
	        }
	        super.onActivityResult(requestCode, resultCode, data);
	    }
}

