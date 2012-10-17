package com.examples.abhi;

import java.util.HashMap;
import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.widget.Toast;

public class Text2Speech extends Activity implements OnInitListener {
	private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech tts;
    private String text = "Sorry, no results found";
    private static final String END_OF_SPEECH = "END";

    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.t2s);	
		  Bundle bundle = this.getIntent().getExtras();
		  if(bundle !=null)
		  {
            text = bundle.getString("key");
		  }
		  Intent checkIntent = new Intent();
	      checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
	      startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);		
	}
    

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                tts = new TextToSpeech(this, this);  
            } 
            else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

	
	@Override
	public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            speakText();
        }
        else if (status == TextToSpeech.ERROR) {
            Toast.makeText(Text2Speech.this, 
                    "Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
        }
		
	}
	

	public void speakText(){
		if (text!=null && text.length()>0) {
			HashMap<String, String> myHash = new HashMap<String, String>();
			myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,END_OF_SPEECH);
			tts.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener(){
				@Override			
				public void onUtteranceCompleted(String utteranceId) {	
					if (0 == utteranceId.compareToIgnoreCase(END_OF_SPEECH)) {
				    	Intent intent = new Intent();
						setResult(RESULT_OK,intent);
						finish();
				    } 					
				}			
    		});			
		    tts.setLanguage(Locale.UK);
        	tts.setSpeechRate((float) .9);
        	tts.setPitch((float)1);
        	tts.speak(text, TextToSpeech.QUEUE_FLUSH, myHash);
	       }
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		tts.shutdown();
	}

	
}
