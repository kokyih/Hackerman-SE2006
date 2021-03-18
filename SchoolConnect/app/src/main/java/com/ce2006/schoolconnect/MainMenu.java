package com.ce2006.schoolconnect;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainMenu extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    TextView Username;
    TextView School;

    // url to create new product
    private static String url_create_product = "https://10.27.141.30/CE2006/create_product.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.add_product);

        setContentView(R.layout.mainmenu_screen);

        Button btnLogout = (Button) findViewById(R.id.logout);
        Button gpsButton = (Button) findViewById(R.id.gpsBtn);
        ImageButton endClass = (ImageButton) findViewById(R.id.endClass);
        ImageButton calendarButton = (ImageButton) findViewById(R.id.calendarBtn);

        ImageButton progressbtn = (ImageButton) findViewById(R.id.progressreport_btn);
        ImageButton upload = (ImageButton) findViewById(R.id.uploadFile);
        ImageButton download = (ImageButton) findViewById(R.id.downloadFile);

        TextView uploadtxt = (TextView) findViewById(R.id.uploadTxt);
        TextView downloadtxt = (TextView) findViewById(R.id.downloadTxt);

        //System.out.println(User.getRole());

        if(User.getRole().compareTo("parent") == 0)
        {
            upload.setVisibility(View.GONE);
            uploadtxt.setVisibility(View.GONE);

            endClass.setVisibility(View.GONE);

        }
        else if (User.getRole().compareTo("teacher") == 0)
        {
            download.setVisibility(View.GONE);
            downloadtxt.setVisibility(View.GONE);
        }

        ImageButton viewfb = (ImageButton) findViewById(R.id.viewfeedbackBtn);
        ImageButton submitfb = (ImageButton) findViewById(R.id.submitFeedbackBtn);

        ImageButton viewconsentform = (ImageButton) findViewById(R.id.consentFormBtn) ;

        Username = (TextView) findViewById(R.id.username);
        Username.setText(User.getName());

        School = (TextView) findViewById(R.id.schoolName);
        School.setText(User.getSchool());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.Logout();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Calendar.class);
                startActivity(i);
            }
        });

        progressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ProgressReport.class);
                startActivity(i);
            }
        });

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GPSActivity.class);
                startActivity(i);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UploadFile.class);
                startActivity(i);
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DownloadFile.class);
                startActivity(i);
            }
        });

        viewfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ViewFeedback.class);
                startActivity(i);
            }
        });

        submitfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SubmitFeedback.class);
                startActivity(i);
            }
        });

        viewconsentform.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getApplicationContext(), ConsentForm.class);
                startActivity(i);
            }
        });

        endClass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Intent i = new Intent(getApplicationContext(), ConsentForm.class);
                //startActivity(i);
                new endingClass().execute();
            }
        });

    }

    class endingClass extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            Hashtable<String,String> params = new Hashtable<String,String>();
            params.put("classID", User.getClassID());
            params.put("setEndClass" , "true");

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(Config.endClass,
                    "POST", params);

            // check log cat fro response
            //Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");

                System.out.println( json.getString("message"));

                if (success == 1) {
                    // successfully created product

                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {

        }

    }

}