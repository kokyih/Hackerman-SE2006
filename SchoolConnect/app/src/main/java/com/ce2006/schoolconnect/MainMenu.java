/**
 * @author Zi Jian
 * @version 1.1
 @since 2021-04-06
 */

package com.ce2006.schoolconnect;

import java.util.Hashtable;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Class for the main menu
 */
public class MainMenu extends Activity {

    JSONParser jsonParser = new JSONParser();
    TextView Username;
    TextView School;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.add_product);

        setContentView(R.layout.mainmenu_screen);

        Button btnLogout = (Button) findViewById(R.id.logout);
        ImageButton gpsButton = (ImageButton) findViewById(R.id.gpsBtn);
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

            new startClass().execute();

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

    /**
     * Task to change status of all students in this class to dismissed
     */
    class endingClass extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            Hashtable<String,String> params = new Hashtable<String,String>();
            params.put("classID", User.getClassID());
            params.put("setEndClass" , "true");

            JSONObject json = jsonParser.makeHttpRequest(Config.endClass,
                    "POST", params);

            try {
                int success = json.getInt("success");

                System.out.println( json.getString("message"));

                if (success == 1) {

                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
        }

    }

    /**
     * Task to start class upon teacher's login
     */
    class startClass extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            Hashtable<String,String> params = new Hashtable<String,String>();
            params.put("classID", User.getClassID());
            params.put("setEndClass" , "false");

            JSONObject json = jsonParser.makeHttpRequest(Config.endClass,
                    "POST", params);

            try {
                int success = json.getInt("success");

                System.out.println( json.getString("message"));

                if (success == 1) {

                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
        }

    }

}