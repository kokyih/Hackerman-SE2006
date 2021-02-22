package com.ce2006.schoolconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

public class SubmitFeedback extends Activity {

    private AlertDialog.Builder builder;
    JSONParser jsonParser = new JSONParser();
    boolean succeed = true;
    private static String url_submitfeedback = Config.submitfeedback;

    EditText title;
    EditText target;
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.submit_feedback);

        title = (EditText) findViewById(R.id.fb_title);
        target = (EditText) findViewById(R.id.fb_targetname);
        message = (EditText) findViewById(R.id.submitfb_fb);

        // Buttons
        Button submit = (Button) findViewById(R.id.submit_fb);
        Button back = (Button) findViewById(R.id.submitFB_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new submitFeedback().execute();
            }
        });
    }

    class submitFeedback extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            builder = new AlertDialog.Builder(SubmitFeedback.this);
            builder.setCancelable(true);
            builder.setMessage("Failed to submit feedback");
        }

        protected String doInBackground(String... args) {
            String targetname = target.getText().toString();
            String titles = title.getText().toString();
            String messages = message.getText().toString();

            Hashtable<String,String> params = new Hashtable<String,String>();
            params.put("target", targetname);
            params.put("title", titles);
            params.put("message", messages);
            params.put("submitid", User.getID());

            JSONObject json = jsonParser.makeHttpRequest(url_submitfeedback,
                    "POST", params);

            // check for success tag
            try {
                int success = json.getInt("success");

                System.out.println( json.getString("message"));

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), SubmitFeedback.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {

                    succeed = false;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            //pDialog.dismiss();
            if(!succeed)
                builder.show();
        }

    }

}
