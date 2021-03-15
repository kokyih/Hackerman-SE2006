package com.ce2006.schoolconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

public class ConsentFormSpecific extends Activity {
    String id;
    private AlertDialog.Builder builder;
    JSONArray jnames = new JSONArray();
    boolean succeed = true;
    JSONParser jsonParser = new JSONParser();

    private static final String url_get_consentform = Config.view1ConsentForm;
    private static final String url_update_consentform = Config.updateConsentForm;
    private static final String url_submit_consentform = Config.submitConsentForm;
    // need to pull from student's class id
    private static final String url_get_listofclassid = Config.getClassIdList;

    Spinner classIdList;
    Button back;
    TextView title;
    TextView sender;
    TextView message;
    TextView noOfApproval;
    ToggleButton approval;
    Button submit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consentform_specific);

        back = (Button) findViewById(R.id.consentform_spec_back);
        title = (EditText) findViewById(R.id.speccf_title);
        sender = (TextView) findViewById(R.id.speccf_sender);
        message = (EditText) findViewById(R.id.speccf_msg);
        approval = (ToggleButton) findViewById(R.id.cfApproveBtn);
        submit = (Button) findViewById(R.id.cfSubmitBtn);
        noOfApproval = (TextView) findViewById(R.id.noOfApproval); // <- need a function for this to get total number of approved form for this consentform id
        classIdList = (Spinner) findViewById(R.id.listOfClass); // <- need a function for this to get list of classes' id


        if(User.getRole().compareTo("teacher") != 0)
        {
            submit.setVisibility(View.GONE);
            message.setEnabled(false);
            title.setEnabled(false);
            noOfApproval.setVisibility(View.GONE);
            classIdList.setVisibility(View.GONE);

        }
        else if(User.getRole().compareTo("teacher")==0)
        {
            approval.setVisibility(View.GONE);

        }


        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        id = i.getStringExtra("id");

        // Getting complete product details in background thread
        new GetConsentFormDetails().execute();

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
                new submitConsentForm().execute();
            }
        });

        approval.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new updateConsentForm().execute();
            }
        });



    }

    class GetConsentFormDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDialog = new ProgressDialog(EditProductActivity.this);
            pDialog.setMessage("Loading product details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            int success;
            try {
                // Building Parameters
                //List<NameValuePair> params = new ArrayList<NameValuePair>();
                //params.add(new BasicNameValuePair("pid", pid));

                Hashtable<String,String> paramsss = new Hashtable<String,String>();
                paramsss.put("id", id);

                // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(url_get_consentform,
                        "POST", paramsss);

                // check your log for json response
                //Log.d("Single Product Details", json.toString());

                // json success tag
                success = json.getInt("success");
                if (success == 1) {
                    // successfully received product details
                    JSONArray productObj = json
                            .getJSONArray("consentform"); // JSON Array

                    // get first product object from JSON Array
                    JSONObject consentform = productObj.getJSONObject(0);

                    // display product data in EditText
                    title.setText(consentform.getString("title"));
                    sender.setText(consentform.getString("senderid"));
                    message.setText(consentform.getString("message"));
                    approval.setChecked(consentform.getBoolean("status"));

                }else{
                    // product with pid not found
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
            // dismiss the dialog once got all details
            //pDialog.dismiss();
        }
    }

    class updateConsentForm extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDialog = new ProgressDialog(EditProductActivity.this);
            pDialog.setMessage("Loading product details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            int success;
            try {
                // Building Parameters
                //List<NameValuePair> params = new ArrayList<NameValuePair>();
                //params.add(new BasicNameValuePair("pid", pid));

                Hashtable<String,String> paramsss = new Hashtable<String,String>();
                paramsss.put("id", id);

                // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(url_update_consentform,
                        "POST", paramsss);

                // check your log for json response
                //Log.d("Single Product Details", json.toString());

                // json success tag
                success = json.getInt("success");
                if (success == 1) {
                    // successfully received product details
                    JSONArray productObj = json
                            .getJSONArray("consentform"); // JSON Array

                    // get first product object from JSON Array
                    JSONObject consentform = productObj.getJSONObject(0);

                    // display product data in EditText
                    title.setText(consentform.getString("title"));
                    sender.setText(consentform.getString("senderid"));
                    message.setText(consentform.getString("message"));
                    approval.setChecked(consentform.getBoolean("status"));

                }else{
                    // product with pid not found
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
            // dismiss the dialog once got all details
            //pDialog.dismiss();
        }
    }

    class submitConsentForm extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            builder = new AlertDialog.Builder(ConsentFormSpecific.this);
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
            params.put("submitid", User.getName());

            JSONObject json = jsonParser.makeHttpRequest(url_submit_consentform,
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
