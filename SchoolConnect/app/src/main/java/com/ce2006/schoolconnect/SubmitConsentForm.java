/**
 * @author Gavin
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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Hashtable;

/**
 * Page to input consent form details and update the database
 */
public class SubmitConsentForm extends Activity implements AdapterView.OnItemSelectedListener{
    String currentTarget = "";
    String id;
    private AlertDialog.Builder builder;
    boolean succeed = true;
    JSONParser jsonParser = new JSONParser();


    private static final String url_submit_consentform = Config.submitConsentForm;

    Spinner target;
    Button back;
    TextView title;
    TextView sender;
    TextView message;
    ToggleButton Approval;
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
        target = (Spinner) findViewById(R.id.listOfClass); // <- need a function for this to get list of classes' id

        Approval.setVisibility(View.GONE);
        target.setVisibility(View.GONE);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        id = i.getStringExtra("id");

        /**
         * Back will go back to the consent form list
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), ConsentForm.class);
                startActivity(i);
            }
        });

        /**
         * Calls the submit consent form task
         */
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

    /**
     * Submit consent form task to call the php script to pass and update the database for consent forms
     */
    class submitConsentForm extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            builder = new AlertDialog.Builder(SubmitConsentForm.this);
            builder.setCancelable(true);
            builder.setMessage("Failed to submit consent form");
        }

        /**
         * Inserts a new consent form in the database in the background
         */
        protected String doInBackground(String... args) {
            String titles = title.getText().toString();
            String messages = message.getText().toString();

            Hashtable<String,String> params = new Hashtable<String,String>();
            params.put("title", titles);
            params.put("message", messages);
            params.put("senderid", User.getName());
            params.put("studentid",currentTarget);

            JSONObject json = jsonParser.makeHttpRequest(url_submit_consentform,
                    "POST", params);

            try {
                int success = json.getInt("success");

                System.out.println( json.getString("message"));

                if (success == 1) {
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
            if(!succeed)
                builder.show();
        }

    }


}
