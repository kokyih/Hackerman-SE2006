/**
 * @author Ooi Kok Yih
 * @version 1.1
 @since 2021-04-06
 */

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

/**
 * Class to view the clicked consent form from the list
 */
public class ConsentFormSpecific extends Activity implements AdapterView.OnItemSelectedListener {
    String id;
    private AlertDialog.Builder builder;
    JSONArray jnames = new JSONArray();
    boolean succeed = true;
    JSONParser jsonParser = new JSONParser();

    private static final String url_get_consentform = Config.view1ConsentForm;
    private static final String url_update_consentform = Config.updateConsentForm;
    private static final String url_submit_consentform = Config.submitConsentForm;

    Spinner target;
    Button back;
    TextView title;
    TextView sender;
    TextView message;
    TextView noOfApproval;
    ToggleButton approval;
    Button submit;

    String statusString = "";


    String currentTarget = "";
    List<String> names = new ArrayList<String>();

    /**
     *
     * @param savedInstanceState on creation link all the UI elements to the XML.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consentform_specific);

        back = (Button) findViewById(R.id.consentform_back);
        title = (EditText) findViewById(R.id.speccf_title);
        sender = (TextView) findViewById(R.id.speccf_sender);
        message = (EditText) findViewById(R.id.speccf_msg);
        approval = (ToggleButton) findViewById(R.id.cfApproveBtn);
        submit = (Button) findViewById(R.id.cfSubmitBtn);
        noOfApproval = (TextView) findViewById(R.id.noOfApproval); // <- need a function for this to get total number of approved form for this consentform id
        target = (Spinner) findViewById(R.id.listOfClass); // <- need a function for this to get list of classes' id


        if (User.getRole().compareTo("teacher") != 0) {
            submit.setVisibility(View.GONE);
            message.setEnabled(false);
            title.setEnabled(false);
            //noOfApproval.setVisibility(View.GONE);
            target.setVisibility(View.GONE);

        } else if (User.getRole().compareTo("teacher") == 0) {
            approval.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            message.setEnabled(false);
            title.setEnabled(false);
            target.setVisibility(View.GONE);
        }

        Intent i = getIntent();

        // gets the ID from the previous class
        id = i.getStringExtra("id");

        // get details for this specific consent form to show on the UI
        new GetConsentFormDetails().execute();

        /**
         * Upon pressing back it will go back to the list
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), ConsentForm.class);
                startActivity(i);
            }
        });

        /**
         * Clicking approval marks that the current user has approved this consent form and will update the database
         */
        approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new updateConsentForm().execute();
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class GetConsentFormDetails extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Getting consent form details in background thread
         */
        protected String doInBackground(String... params) {

            int success;
            try {

                Hashtable<String, String> paramsss = new Hashtable<String, String>();
                paramsss.put("id", id);

                JSONObject json = jsonParser.makeHttpRequest(url_get_consentform,
                        "POST", paramsss);

                success = json.getInt("success");
                if (success == 1) {
                    JSONArray productObj = json
                            .getJSONArray("consentform"); // JSON Array

                    // get first product object from JSON Array
                    JSONObject consentform = productObj.getJSONObject(0);

                    // display product data in EditText
                    title.setText(consentform.getString("title"));
                    sender.setText(consentform.getString("senderid"));
                    message.setText(consentform.getString("message"));

                    statusString = consentform.getString("status");

                    String[] arrSplit = statusString.split(",");

                    approval.setChecked(false);

                    for(int i = 0 ; i < arrSplit.length ; i ++)
                    {
                        if(arrSplit[i].compareTo(User.getID().toString()) == 0)
                        {
                            approval.setChecked(true);

                            approval.setEnabled(false);

                            break;
                        }
                    }

                    System.out.print(consentform.getString("status"));

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
     * Task to update the number of approved users in the consent form database
     */
    class updateConsentForm extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * updating consent form details in background thread
         */
        protected String doInBackground(String... params) {

            int success;
            try {

                Hashtable<String, String> paramsss = new Hashtable<String, String>();
                paramsss.put("id", id);
                paramsss.put("status", statusString + ',' + User.getID().toString());
                JSONObject json = jsonParser.makeHttpRequest(url_update_consentform,
                        "POST", paramsss);

                success = json.getInt("success");
                if (success == 1) {
                    JSONArray productObj = json
                            .getJSONArray("consentform"); // JSON Array

                    JSONObject consentform = productObj.getJSONObject(0);


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

