package com.ce2006.schoolconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class SubmitFeedback extends Activity implements AdapterView.OnItemSelectedListener{

    private AlertDialog.Builder builder;
    JSONParser jsonParser = new JSONParser();
    JSONArray jnames = new JSONArray();
    boolean succeed = true;
    private static String url_submitfeedback = Config.submitfeedback;

    EditText title;
    Spinner target;
    EditText message;

    String currentTarget = "";

    List<String> names = new ArrayList<String>();

    //ArrayAdapter adapter;//= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, names);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.submit_feedback);

        title = (EditText) findViewById(R.id.fb_title);
        target = (Spinner) findViewById(R.id.fb_targetname);
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

        new getNameList().execute();
    }

    class getNameList extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            Hashtable<String,String> params = new Hashtable<String,String>();

            JSONObject json = jsonParser.makeHttpRequest(Config.getfeedbackNames,
                    "POST", params);

            // check for success tag
            try {
                int success = json.getInt("success");

                System.out.println( json.getString("message"));

                jnames = json.getJSONArray("nameList");

                // looping through All Products
                for (int i = 0; i < jnames.length(); i++) {
                    JSONObject c = jnames.getJSONObject(i);

                    // Storing each json item in variable
                    String name = c.getString("name");

                    // adding HashList to ArrayList
                    names.add(name);
                    //System.out.println( name);
                }

                if (success == 1) {
                    succeed = true;
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            if (succeed)
            {
                succeed = false;
                //ArrayAdapter newadapter = new ArrayAdapter<String>(SubmitFeedback.this, android.R.layout.simple_spinner_dropdown_item, names);
                ArrayAdapter adapter = new ArrayAdapter<String>(SubmitFeedback.this, android.R.layout.simple_spinner_dropdown_item, names);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                target.setAdapter(adapter);
                target.setOnItemSelectedListener(SubmitFeedback.this);
                //System.out.println("ENTER HERE LEH");
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        currentTarget = names.get(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            String targetname = currentTarget;//target.getText().toString();
            String titles = title.getText().toString();
            String messages = message.getText().toString();

            Hashtable<String,String> params = new Hashtable<String,String>();
            params.put("target", targetname);
            params.put("title", titles);
            params.put("message", messages);
            params.put("submitid", User.getName());

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
