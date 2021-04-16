/**
 * @author Zi Jian
 * @version 1.1
 @since 2021-04-06
 */

package com.ce2006.schoolconnect;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class for teachers to upload new timetable to the database
 */
public class UploadFile extends Activity {

    EditText Name;
    EditText Type;
    ImageView theImage;

    boolean succeed = true;

    private static final int IMAGE_REQUEST_CODE = 3;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static String url_upload = Config.upload;

    private Uri filePath;
    private Bitmap bitmap;

    int serverResponseCode = 0;
    static InputStream is = null;
    static JSONObject jObj = null;

    String path= "";
    //FileTransfer file;

    private AlertDialog.Builder builder;
    private int uploaded = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.upload_image);

        // Edit Text
        Name = (EditText) findViewById(R.id.imageName);
        //Type = (EditText) findViewById(R.id.image_type);
        theImage = (ImageView) findViewById(R.id.theImage);

        // Buttons
        Button back = (Button) findViewById(R.id.back_upload);
        Button submit = (Button) findViewById(R.id.submitImage);

        requestStoragePermission();

        /**
         * Press back to go back to the main menu
         */
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(i);

            }
        });

        /**
         * Pressing submit uploads the image to the database
         */
        submit.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                uploadMultiPart();

            }

        });

        /**
         * Clicking on the blank image allows user to choose an image from their gallery
         */
        theImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_REQUEST_CODE);

            }
        });
    }

    /**
     * After user selected an image from their gallery
     * @param requestCode
     * @param resultCode
     * @param data data of the file selected, in this case the image selected
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                theImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }

    }

    /**
     * Function to upload the file in parts in the background
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void uploadMultiPart(){

        path = getPath(filePath);

        //Name.setText(path);

        if(Name.getText().toString() == "")
            Name.setText(path);

        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("uploading started.....");
                    }
                });

                new uploadFile().execute();

            }
        }).start();
    }

    /**
     * Task to upload the file
     */
    class uploadFile extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            builder = new AlertDialog.Builder(UploadFile.this);
            builder.setCancelable(true);
            builder.setMessage("Failed to upload \nPlease Try again");
        }

        protected String doInBackground(String... args) {
            String fileName = path;

            System.out.println(fileName);

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 2048 * 2048;
            File sourceFile = new File(path);

            if (!sourceFile.isFile()) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("Source File not exist :" +uploadFilePath + "" + uploadFileName);
                    }
                });

            } else {
                try {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(url_upload);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("file", fileName);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + Name.getText().toString()  +  ".jpg" + lineEnd);

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

                    //System.out.println(serverResponseMessage);

                    String json = "";

                    try {
                        is = conn.getInputStream();

                        BufferedReader in = new BufferedReader(new InputStreamReader(is));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine + '\n');
                        }
                        in.close();

                        json = response.toString();
                    } catch (Exception e) {
                        Log.e("Buffer Error", "Error converting result " + e.toString());
                    }

                    System.out.println(json);

                    // try parse the string to a JSON object
                    try {
                        jObj = new JSONObject(json);
                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }

                    //System.out.println(jObj.getString("message"));

                    int success = 0;
                    try {
                        success = jObj.getInt("success");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if ( success == 1){//serverResponseCode == 200) {

                        runOnUiThread(new Runnable() {
                            public void run() {
                                //Toast.makeText(UploadToServer.this, "File Upload Complete.",Toast.LENGTH_SHORT).show();
                                System.out.println("It works the upload");
                                uploaded = 1;
                            }
                        });
                    }

                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (MalformedURLException ex) {

                    //dialog.dismiss();
                    ex.printStackTrace();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            //messageText.setText("MalformedURLException Exception : check script url.");
                            //Toast.makeText(UploadToServer.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {

                    //dialog.dismiss();
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            //messageText.setText("Got Exception : see logcat ");
                            //Toast.makeText(UploadToServer.this, "Got Exception : see logcat ",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } // End else block

            return null;
        }

        protected void onPostExecute(String file_url) {
            if(!succeed)
                builder.show();
            else
            {
                builder.setMessage("Uploaded successfully");
                builder.show();
            }
        }

    }

    /**
     * Function to get the path of the file in the phone to upload
     * @param uri
     * @return
     */
    public String getPath(Uri uri){

        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();

        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null , MediaStore.Images.Media._ID + " = ? ", new String[] {document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

        cursor.close();

        return path;
    }

    /**
     * To request user storage permission to choose image
     */
    protected void requestStoragePermission(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            // explain why need permission here;
        }

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , STORAGE_PERMISSION_CODE);
    }

    /**
     * Reaction to user choosing an image
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == STORAGE_PERMISSION_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }

        }
    }

}
