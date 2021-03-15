package com.ce2006.schoolconnect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ProgressReport extends ListActivity{

    private AlertDialog.Builder builder;
    JSONParser jsonParser = new JSONParser();
    JSONArray jnames = new JSONArray();
    boolean succeed = true;
    private static String url_updateProgressReport = Config.updateProgressReport;
    private static String url_getProgressReport = Config.getProgressReport;
    private static String url_getStudentIdList = Config.getStudentIdList;

    SearchView listofstudent;
    EditText maths;
    EditText science;
    EditText mothertongue;
    EditText english;
    Button update;
    Button back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        update = (Button) findViewById(R.id.updategrades);
        back = (Button) findViewById(R.id.backbtn);
        maths = (EditText) findViewById(R.id.Maths);
        science = (EditText) findViewById (R.id.Science);
        mothertongue = (EditText) findViewById (R.id.MotherTongue);
        english = (EditText) findViewById (R.id.English);
        listofstudent = (SearchView) findViewById(R.id.listofstudent);

        new getProgressReport().execute();
        //new getStudentIdList().execute();??? idk man

        if(User.getRole().compareTo("teacher") != 0)
        {
            maths.setEnabled(false);
            science.setEnabled(false);
            mothertongue.setEnabled(false);
            english.setEnabled(false);
            listofstudent.setVisibility(View.GONE);
            update.setVisibility(View.GONE);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(i);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new updateProgressReport().execute();
            }
        });

    }
    class updateProgressReport extends AsyncTask<String, String, String> {

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
                //paramsss.put("id", id);

                // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(url_updateProgressReport,
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
                    //title.setText(consentform.getString("title"));
                    //sender.setText(consentform.getString("senderid"));
                    //message.setText(consentform.getString("message"));
                    //approval.setChecked(consentform.getBoolean("status"));

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

    class getProgressReport extends AsyncTask<String, String, String> {

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
                //paramsss.put("id", id);

                // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(url_getProgressReport,
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
                    //title.setText(consentform.getString("title"));
                    //sender.setText(consentform.getString("senderid"));
                    //message.setText(consentform.getString("message"));
                    //approval.setChecked(consentform.getBoolean("status"));

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
