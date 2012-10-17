package com.examples.abhi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;


public class FlickrLoad extends Activity{
	
	ProgressDialog progressDialog;
	BackgroundThread backgroundThread;
	private final int TTS_ACTIVITY_REQUEST_CODE=0;
	private final int SPEECH_ACTIVITY_REQUEST_CODE =1;
	String DEFAULT_SEARCH = "Holland";
	String text ="Netherlands";

	
	public class FlickrImage {
    	String Id;
    	String Owner;
    	String Secret;
    	String Server;
    	String Farm;
    	String Title;
    	
    	Bitmap FlickrBitmap;
    	
    	FlickrImage(String _Id, String _Owner, String _Secret, 
    			String _Server, String _Farm, String _Title){
    		Id = _Id;
        	Owner = _Owner;
        	Secret = _Secret;
        	Server = _Server;
        	Farm = _Farm;
        	Title = _Title;
        	
        	FlickrBitmap = preloadBitmap();
    	}
    	
    	private Bitmap preloadBitmap(){
    		Bitmap bm= null;
        	
        	String FlickrPhotoPath = 
        			"http://farm" + Farm + ".static.flickr.com/" 
        			+ Server + "/" + Id + "_" + Secret + "_m.jpg";
        	
        	URL FlickrPhotoUrl = null;
        	
        	try {
    			FlickrPhotoUrl = new URL(FlickrPhotoPath);
    			
    			HttpURLConnection httpConnection 
    				= (HttpURLConnection) FlickrPhotoUrl.openConnection();
    			httpConnection.setDoInput(true);
    			httpConnection.connect();
    			InputStream inputStream = httpConnection.getInputStream();
    			bm = BitmapFactory.decodeStream(inputStream);
    			
    		} catch (MalformedURLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	
        	return bm;
    	}
    	
    	public Bitmap getBitmap(){
    		return FlickrBitmap;
    	}

	}
	
	class FlickrAdapter extends BaseAdapter{
		private Context context;
		private FlickrImage[] FlickrAdapterImage;;

		FlickrAdapter(Context c, FlickrImage[] fImage){
			context = c;
			FlickrAdapterImage = fImage;
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return FlickrAdapterImage.length;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return FlickrAdapterImage[position];
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ImageView image;  
			if (convertView == null) {
				image = new ImageView(context);  
				image.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  
				image.setScaleType(ImageView.ScaleType.CENTER_CROP);  
				image.setPadding(8, 8, 8, 8);  	
			} else {  
				image = (ImageView) convertView;  	
			}  
 	
			image.setImageBitmap(FlickrAdapterImage[position].getBitmap());
		     
		    return image; 
		}
		
	}
	
	FlickrImage[] myFlickrImage;

	/*
	 * FlickrQuery = FlickrQuery_url 
	 * + FlickrQuery_per_page 
	 * + FlickrQuery_nojsoncallback 
	 * + FlickrQuery_format
	 * + FlickrQuery_tag + q
	 * + FlickrQuery_key + FlickrApiKey
	 */

	String FlickrQuery_url = "http://api.flickr.com/services/rest/?method=flickr.photos.search";
	String FlickrQuery_per_page = "&per_page=10";
	String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
	String FlickrQuery_format = "&format=json";
	String FlickrQuery_tag = "&tags=";
	String FlickrQuery_key = "&api_key=";

	String FlickrApiKey = "cc64b77f04506630b22ae77d1f2eba2d";

	
	EditText searchText;
    Button searchButton; 
    //Button startAgainBt;
    Button exitBt;
    Gallery photoBar;
    Bitmap bmFlickr;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flickr);
       
        photoBar = (Gallery)findViewById(R.id.photoBar);
        Bundle bundle =this.getIntent().getExtras();
        if(bundle !=null)
          {
            text = bundle.getString("key");
            //Toast.makeText(this, text, Toast.LENGTH_LONG).show();
          }
        else{
        	text = DEFAULT_SEARCH;
        	//Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
        

        //String dialogTemp2 = dialogTemp1.concat(text);
        // String dialogTemp3 = getResources().getString(R.string.dia7);
        //String dialogWait = dialogTemp1.concat(dialogTemp3);
        // String dialogTemp4 = getResources().getString(R.string.dia6);
        //String dialogWait = dialogTemp.concat(getResources().getString(R.string.dia7));
        
        String dialogTemp1 = getResources().getString(R.string.dia6);
        String location = text;
        String dialogWait = dialogTemp1 + location;
		Intent intent = new Intent("com.examples.abhi.TTS");	
		intent.putExtra("key", dialogWait);
		startActivityForResult(intent, TTS_ACTIVITY_REQUEST_CODE);
		
		progressDialog = ProgressDialog.show(FlickrLoad.this, 
                "ProgressDialog", "Wait!");
		
        backgroundThread = new BackgroundThread();
        backgroundThread.setRunning(true);
        backgroundThread.start();
        
        /*
        startAgainBt = (Button)findViewById(R.id.startAgainButton); 
        startAgainBt.setOnClickListener( new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                //Intent intent = new Intent("com.examples.abhi.WISH");
				//startActivity(intent);
				Intent intent2 = new Intent ("com.examples.abhi.SPEECH");
				startActivityForResult(intent2, SPEECH_ACTIVITY_REQUEST_CODE);				
			}
		});
        */
        
    }
    
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode)
		{
		  case TTS_ACTIVITY_REQUEST_CODE:
			 if(resultCode==RESULT_OK){
				   //Toast.makeText(this, "inside TTS", Toast.LENGTH_SHORT).show();
			 }
			 break;
			 
		  case SPEECH_ACTIVITY_REQUEST_CODE:
			  if(resultCode==RESULT_OK){
				    //Toast.makeText(this, "inside Speech", Toast.LENGTH_SHORT).show();	
				    //String text1;
					Bundle extras = intent.getExtras();
					ArrayList<String> matches = extras.getStringArrayList("speechResultKey");			
		            if ( (matches.contains("geopad")) || (matches.contains("experts")) || (matches.contains("connect me to experts")) || (matches.contains("expert")) || (matches.contains("connect me to expert")) || (matches.contains("connect me to experts"))  )
		            {
		            	//text1 = getResources().getString(R.string.dia10);
                        String url = "http://www.geopaden.nl/portal/index.php?option=com_content&view=article&id=74&Itemid=107&lang=nl";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
		            }
		            else if ( (matches.contains("flickr")) || (matches.contains("connect me to flickr")) || (matches.contains("Tell me more")) || (matches.contains("more on images")) || (matches.contains("tell me about location")) || (matches.contains("location")) || (matches.contains("tell me")))
		            {
		            	//text1 = getResources().getString(R.string.statementOk);
		            	 String url = "http://www.flickr.com/photos/62716311@N06";
	                     Intent i = new Intent(Intent.ACTION_VIEW);
	                     i.setData(Uri.parse(url));
	                     startActivity(i);
		            }
		            else
		            {
		                Intent intent3 = new Intent("com.examples.abhi.WISH");
						startActivity(intent3);
		            }
			  }	
			  break;
	  
		}
	}
	

	
    
    private String QueryFlickr(String q){
    	
    	String qResult = null;
    	
    	String qString = 
    			FlickrQuery_url 
    			+ FlickrQuery_per_page 
    			+ FlickrQuery_nojsoncallback 
    			+ FlickrQuery_format 
    			+ FlickrQuery_tag + q  
    			+ FlickrQuery_key + FlickrApiKey;
    	
    	HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(qString);
        
        try {
			HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();
			
			if (httpEntity != null){
				InputStream inputStream = httpEntity.getContent();
				Reader in = new InputStreamReader(inputStream);
				BufferedReader bufferedreader = new BufferedReader(in);
				StringBuilder stringBuilder = new StringBuilder();
				
				String stringReadLine = null;
				
				while ((stringReadLine = bufferedreader.readLine()) != null) {
					stringBuilder.append(stringReadLine + "\n");
					}
				
				qResult = stringBuilder.toString();
				inputStream.close();
			}
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return qResult;
    }
    
    private FlickrImage[] ParseJSON(String json){

    	FlickrImage[] flickrImage = null;
    	
    	bmFlickr = null;
    	String flickrId;
    	String flickrOwner;
    	String flickrSecret;
    	String flickrServer;
    	String flickrFarm;
    	String flickrTitle;
    	
    	try {
			JSONObject JsonObject = new JSONObject(json);
			JSONObject Json_photos = JsonObject.getJSONObject("photos");
			JSONArray JsonArray_photo = Json_photos.getJSONArray("photo");
			
			flickrImage = new FlickrImage[JsonArray_photo.length()];
			for (int i = 0; i < JsonArray_photo.length(); i++){
				JSONObject FlickrPhoto = JsonArray_photo.getJSONObject(i);
				flickrId = FlickrPhoto.getString("id");
				flickrOwner = FlickrPhoto.getString("owner");
				flickrSecret = FlickrPhoto.getString("secret");
				flickrServer = FlickrPhoto.getString("server");
				flickrFarm = FlickrPhoto.getString("farm");
				flickrTitle = FlickrPhoto.getString("title");
				flickrImage[i] = new FlickrImage(flickrId, flickrOwner, flickrSecret,
						flickrServer, flickrFarm, flickrTitle);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return flickrImage;
    }
    
    public class BackgroundThread extends Thread{
    	volatile boolean running = false;
    	int cnt;
    	
    	void setRunning(boolean b){
    		running = b;	
    		cnt = 10;
    	}

		@Override
		public void run() {
			// TODO Auto-generated method stub			
	        String searchResult = QueryFlickr(text);
	        myFlickrImage = ParseJSON(searchResult);				
			handler.sendMessage(handler.obtainMessage());
		}
    }
    
    Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			progressDialog.dismiss();
			photoBar.setAdapter(new FlickrAdapter(FlickrLoad.this, myFlickrImage));
			//Toast.makeText(FlickrLoad.this, "Flickr images loaded", Toast.LENGTH_LONG).show();
			/*
		    String dialogReq = getResources().getString(R.string.dia8);
			Intent intent4 = new Intent("com.examples.abhi.TTS");	
			intent4.putExtra("key", dialogReq);
			startActivityForResult(intent4, TTS_ACTIVITY_REQUEST_CODE);
			*/
		}
    	
    };

}