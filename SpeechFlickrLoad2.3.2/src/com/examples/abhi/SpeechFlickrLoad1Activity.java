package com.examples.abhi;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class SpeechFlickrLoad1Activity extends Activity {
    //Activity to introduce the application and character
	private final int TTS_ACTIVITY_REQUEST_CODE=0;
	MediaPlayer myCheers;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);   	
        setContentView(R.layout.splash);
        myCheers = MediaPlayer.create(this, R.raw.magpiesound1);
        myCheers.start();
        
        String dialog = getResources().getString(R.string.dia1);
        //dialog = getResources().getString(R.string.dia1).concat(getResources().getString(R.string.dia2));
        Intent intent1 = new Intent("com.examples.abhi.TTS");
        intent1.putExtra("key",dialog);
        startActivityForResult(intent1, TTS_ACTIVITY_REQUEST_CODE);      
        //Intent intent2 = new Intent("com.examples.abhi.WISH");
        //startActivity(intent2);	          
        //Add bird sound file to play
    }
	

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		if (requestCode ==TTS_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
			super.onActivityResult(requestCode, resultCode, intent);
			Intent intent2 = new Intent("com.examples.abhi.WISH");
	        startActivity(intent2);	
		}		
	}

}