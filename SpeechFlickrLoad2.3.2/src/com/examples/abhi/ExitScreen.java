package com.examples.abhi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ExitScreen extends Activity{
	private final int TTS_ACTIVITY_REQUEST_CODE=0;
	String dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit);
	    dialog = getResources().getString(R.string.dia10);
        Intent intent = new Intent("com.examples.abhi.TTS");
        intent.putExtra("key",dialog);
        startActivityForResult(intent, TTS_ACTIVITY_REQUEST_CODE);

	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		if (requestCode ==TTS_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
			super.onActivityResult(requestCode, resultCode, intent);
			finish();
		}		
	}
	

}
