package com.ce2006.schoolconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class SubmitConsentForm extends Activity implements AdapterView.OnItemSelectedListener{
    String currentTarget = "";
    List<String> names = new ArrayList<String>();
    String id;
    private AlertDialog.Builder builder;
    JSONArray jnames = new JSONArray();
    boolean succeed = true;
    JSONParser jsonParser = new JSONParser();


    private static final String url_submit_consentform = Config.submitConsentForm;
    // need to pull from student's class id
    //private static final String url_get_listofclassid = Config.getClassIdList;

    Spinner target;
    Button back;
    TextView title;
    TextView sender;
    TextView message;
    ToggleButton Approval;
    //TextView noOfApproval;
    Button submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.consentform_specific);

        back = (Button) findViewById(R.id.consentform_back);
        title = (EditText) findViewById(R.id.speccf_title);
        sender = (TextView) findViewById(R.id.speccf_sender);
        message = (EditText) findViewById(R.id.speccf_msg);
        submit = (Button) findViewById(R.id.cfSubmitBtn);
        Approval = (ToggleButton) findViewById(R.id.cfApproveBtn);
        //noOfApproval = (TextView) findViewById(R.id.noOfApproval); // <- need a function for this to get total number of approved form for this consentform id
        target = (Spinner) findViewById(R.id.listOfClass); // <- need a function for this to get list of classes' id

        Approval.setVisibility(View.GONE);
        target.setVisibility(View.GONE);



        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        id = i.getStringExtra("id");



        // save button click event
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), ConsentForm.class);
                startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SubmitConsentForm.submitConsentForm().execute();
            }
        });




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    class submitConsentForm extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            builder = new AlertDialog.Builder(SubmitConsentForm.this);
            builder.setCancelable(true);
            builder.setMessage("Failed to submit consent form");
        }

        protected String doInBackground(String... args) {
            //String targetname = currentTarget;//target.getText().toString();
            String titles = title.getText().toString();
            String messages = message.getText().toString();

            Hashtable<String,String> params = new Hashtable<String,String>();
            //params.put("target", targetname);
            params.put("title", titles);
            params.put("message", messages);
            params.put("senderid", User.getName());
            params.put("studentid",currentTarget);

            JSONObject json = jsonParser.makeHttpRequest(url_submit_consentform,
                    "POST", params);

            // check for success tag
            try {
                int success = json.getInt("success");

                System.out.println( json.getString("message"));

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), ConsentForm.class);
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
