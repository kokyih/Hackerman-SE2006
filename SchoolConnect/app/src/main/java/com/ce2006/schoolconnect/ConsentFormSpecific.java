package com.ce2006.schoolconnect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

public class ConsentFormSpecific extends Activity {
    String id;

    JSONParser jsonParser = new JSONParser();

    private static final String url_get_consentform = Config.view1ConsentForm;

    Button back;
    TextView title;
    TextView sender;
    TextView message;
    ToggleButton approval;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consentform_specific);

        // save button
        back = (Button) findViewById(R.id.consentform_spec_back);

        title = (TextView) findViewById(R.id.speccf_title);
        sender = (TextView) findViewById(R.id.speccf_sender);
        message = (TextView) findViewById(R.id.speccf_msg);
        approval = (ToggleButton) findViewById(R.id.cfApproveBtn);

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
                            .getJSONArray("ConsentForm"); // JSON Array

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
}
