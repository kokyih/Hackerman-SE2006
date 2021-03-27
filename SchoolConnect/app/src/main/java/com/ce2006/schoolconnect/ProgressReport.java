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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ProgressReport extends Activity implements AdapterView.OnItemSelectedListener{

    private AlertDialog.Builder builder;
    JSONParser jsonParser = new JSONParser();
    JSONArray jnames = new JSONArray();
    boolean succeed = true;
    private static String url_updateProgressReport = Config.updateProgressReport;
    private static String url_getProgressReport = Config.getProgressReport;
    private static String url_getStudentIdList = Config.getStudentIdList;

    //SearchView listofstudent;
    EditText maths;
    EditText science;
    EditText mothertongue;
    EditText english;
    Button update;
    Button back;

    Spinner target;

    String currentTarget = "";

    List<String> names = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressreport);



        update = (Button) findViewById(R.id.updategrades);
        back = (Button) findViewById(R.id.backbtn);
        maths = (EditText) findViewById(R.id.Maths);
        science = (EditText) findViewById (R.id.Science);
        mothertongue = (EditText) findViewById (R.id.MotherTongue);
        english = (EditText) findViewById (R.id.English);
        //listofstudent = (SearchView) findViewById(R.id.listofstudent);

        target = (Spinner) findViewById(R.id.listofstudent);


        //new getProgressReport().execute();
        //new getStudentIdList().execute();??? idk man

        new getProgressReport().execute();

        if(User.getRole().compareTo("teacher") != 0)
        {
            maths.setEnabled(false);
            science.setEnabled(false);
            mothertongue.setEnabled(false);
            english.setEnabled(false);
            //listofstudent.setVisibility(View.GONE);
            target.setVisibility(View.GONE);
            update.setVisibility(View.GONE);
        }
        else
        {
            new getNames().execute();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        currentTarget = names.get(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                paramsss.put("studentid",currentTarget);
                System.out.println(currentTarget);
                paramsss.put("maths",maths.getText().toString());
                paramsss.put("english",english.getText().toString());
                paramsss.put("science",science.getText().toString());
                paramsss.put("mothertongue",mothertongue.getText().toString());


                JSONObject json = jsonParser.makeHttpRequest(url_updateProgressReport,
                        "POST", paramsss);


                // json success tag
                success = json.getInt("success");
                System.out.println( json.getString("message"));

                if (success == 1) {


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
                paramsss.put("studentid",User.getName());




                // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(url_getProgressReport,
                        "POST", paramsss);
                System.out.println( json.getString("message"));
                // check your log for json response
                //Log.d("Single Product Details", json.toString());

                // json success tag
                success = json.getInt("success");
                if (success == 1) {
                    // successfully received product details




                    // display product data in EditText
                    english.setText(json.getString("english"));
                    maths.setText(json.getString("maths"));
                    science.setText(json.getString("science"));
                    mothertongue.setText(json.getString("mothertongue"));

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

    class getNames extends AsyncTask<String, String, String> {

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


                JSONObject json = jsonParser.makeHttpRequest(Config.getStudentIdList,
                        "POST", paramsss);


                // json success tag
                success = json.getInt("success");
                System.out.println( json.getString("message"));

                if (success == 1) {
                    // successfully received product details
                    succeed = true;
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

                }else{
                    // product with pid not found
                    succeed = false;
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
            if(succeed) {
                ArrayAdapter adapter = new ArrayAdapter<String>(ProgressReport.this, android.R.layout.simple_spinner_dropdown_item, names);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                target.setAdapter(adapter);
                target.setOnItemSelectedListener(ProgressReport.this);
            }

        }
    }

    }
