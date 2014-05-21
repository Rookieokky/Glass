package com.example.glance;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Camera;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.MultipartEntity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

//
////////////////////////////////////Picture////////////////////////////////////
public class Picture extends Activity {
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final String UPLOAD_OCR = "http://ec2-54-186-34-253.us-west-2.compute.amazonaws.com/upload_ocr.php"; //to upload the image and get the Words
	public static final String DICTIONARY = "http://ec2-54-186-34-253.us-west-2.compute.amazonaws.com/dictionary.php"; //to send the Words and get the Meanings
	public static final String WORD_IMAGE = "http://ec2-54-186-34-253.us-west-2.compute.amazonaws.com/word_image.php"; //to get the first Image from Google --NOT IMPLEMENTED
	
    // "http://ec2-54-186-34-253.us-west-2.compute.amazonaws.com/Sprintz.html"   //not implemented

	
    //Words that will be sent to the server
	List<String>words = new ArrayList<String>();
    
    //Response for the words sent to the server
	List<String>meanings = new ArrayList<String>();
    
    //This is probably not doing anything
	private String current;
    
    
	Bitmap bitmap;
	
	private Uri fileUri;
    Button ButtonClick;
    int CAMERA_PIC_REQUEST = 2;

	//
	@Override
	//*****************************onCreate()*****************************
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
		//requestWindowFeature(Window.FEATURE_NO_TITLE); //Removes the app name to save space on the screen

        ButtonClick =(Button) findViewById(R.id.Camera);
        ButtonClick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory(),"test.jpg");
                fileUri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //for full-sized picture

                startActivityForResult(intent, MEDIA_TYPE_IMAGE);

                bitmap = (Bitmap) intent.getExtras().get("data"); //I believe this was a try to send the image to the server
            }
        });


	}

    public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        getWords(data);
        //meow - doesn't reach here
        getMeanings();
	}

	public void getWords(Intent data)
	{

        Log.d("chicken1","chicken1");
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(UPLOAD_OCR);
        Button ButtonClick;
        int  TAKE_PICTURE=0;
        Camera camera;
        final TextView messageText;
        Button uploadButton;
        int serverResponseCode = 0;
        ProgressDialog dialog = null;
        String upLoadServerUri = null;
        Log.d("chicken1","chicken1");
        final String uploadFilePath = "/mnt/sdcard/";
        final String uploadFileName = "service_lifecycle.png";
        messageText  = (TextView)findViewById(R.id.messageText);
        Log.d("chicken1","chicken1");
        upLoadServerUri = UPLOAD_OCR;
        //super.onActivityResult(requestCode, resultCode, data);
        Log.d("chicken1","chicken1");

        Log.d("chicken1","chicken1 meow");

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data"); //meow this line doesn't execute

        Log.d("chicken1","chicken1 meow");
        ImageView image =(ImageView) findViewById(R.id.PhotoCaptured);

        Log.d("chicken1","chicken1 meow");
        image.setImageBitmap(thumbnail);
//Log.d("chicken1","chicken1");
        //create a file to write bitmap data
        //Context context = this;
        Log.d("chicken1","chicken1 meow");
        File f = new File(Picture.this.getCacheDir(), "filename");
        Log.d("chicken1","chicken1 meow2");
        try {
            f.createNewFile();
            Log.d("chicken1","chicken1 meow3");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("chicken1","chicken1 meow4");
        }
        Log.d("chicken1","chicken1meow5");


    //Convert bitmap to byte array
        Bitmap bitmap = thumbnail;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        Log.d("chicken1","chicken1");
        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert fos != null;
        try {
            fos.write(bitmapdata);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName = "filename";

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = f; ///////////////////////////
        Log.d("chicken2","chicken2");
        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    +uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                    messageText.setText("Source File not exist :"
                            +uploadFilePath + "" + uploadFileName);
                }
            });


        }
        else
        {
            try {
                Log.d("chicken3","chicken3");
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                Log.d("chicken9","chicken9");
                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.d("serverResponseMessage",serverResponseMessage);
                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                words.add(serverResponseMessage);

                if(serverResponseCode == 200){
                    Log.d("chicken4","chicken4");
                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +" http://ec2-54-186-34-253.us-west-2.compute.amazonaws.com/upload/"
                                    +uploadFileName;

                            messageText.setText(msg);
                            Toast.makeText(Picture.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(Picture.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(Picture.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);
            }
            // dialog.dismiss();

        } // End else block
        ////

	        

	}

	//*****************************getMeanings()*****************************
	//It sends word to the server, the server sends the request to the dictionary API and returns the meaning
    //This method and LongOperation class were made with help from one of the Spritz' engineers, 
    //we were in a rush to deliver the project so I couldn't ask much what was going on, thus I can't explain much
	public void getMeanings() 
	{//
		//Sending requests are time consuming, anything that uses more than 5 seconds in Android should be done in a separate thread
        LongOperation op = new LongOperation() {
			protected void onPostExecute(java.util.List<String> result) {
				Picture.this.meanings = result;
				 //Now that we have the meanings we can create the cards

			};
		};
		
		op.execute(words);
	} 

    
    
    ////////////////////////////////////LongOperation////////////////////////////////////
    //This class and getMeanings() were made with help from one of the Spritz' engineers, 
    //we were in a rush to deliver the project so I couldn't ask much what was going on, thus I can't explain much
    
    //AsyncTask in a nut shell:
    //  It is Thread to execute time consuming operations, 
    //  doInBackground() is where the magic happens
    //  onPreExecute() is what happens Before the execution --not implemented
    //  onPostExecute() is what happens After the execution -- implemented anonymously in the getMeanings()
    //  It takes 3 args, #1 Argument, #2 is to show progress, #3 Return type
	private class LongOperation extends AsyncTask<List<String>, Void, List<String>>
	{

		@Override
        //*****************************doInBackground()*****************************
		protected List<String> doInBackground(List<String>... arg0) 
        {
            List<String> meanings2 = new ArrayList<String>(); //This will be the return
			for(String word : arg0[0])
            {
				try 
		        {
		    		HttpClient httpclient = new DefaultHttpClient();
		            HttpPost httppost = new HttpPost(DICTIONARY); //DICTIONARY is the variable that contains the website that will receive the word and gives the meaning back
	
		            try 
		            {
		                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); //The server wants a key-value request, that's why we are using this
		                nameValuePairs.add(new BasicNameValuePair("word", word));
		                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
		                // Execute HTTP Post Request
		                HttpResponse res = httpclient.execute(httppost);
		                HttpEntity entity = res.getEntity();
		                
		                meanings2.add( EntityUtils.toString(entity) );  //Translates the server's response to String
	
		            } catch (ClientProtocolException e) { } 
                      catch (IOException e) { }
		        } catch (Exception e) { e.printStackTrace(); }
			}
			return meanings2;
		}
		
	}


}
