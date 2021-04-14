/**
 * @author Nicholas
 * @version 1.1
 @since 2021-04-06
 */

package com.ce2006.schoolconnect;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

/**
 * Class to show progress report UI
 */
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

        /**
         * Pressing back goes back to the main menu
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(i);
            }
        });

        /**
         * Pressing update will send the users input to the database to update specific student's grades
         */
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new updateProgressReport().execute();
            }
        });

    }

    /**
     * Gets the selected item in the drop down list
     * @param parent
     * @param v
     * @param position is the pos of the clicked item in the list
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        currentTarget = names.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Task to update the progress report
     */
    class updateProgressReport extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * setting progress report details in background thread
         * */
        protected String doInBackground(String... params) {

            int success;
            try {

                Hashtable<String,String> paramsss = new Hashtable<String,String>();
                paramsss.put("studentid",currentTarget);
                System.out.println(currentTarget);
                paramsss.put("maths",maths.getText().toString());
                paramsss.put("english",english.getText().toString());
                paramsss.put("science",science.getText().toString());
                paramsss.put("mothertongue",mothertongue.getText().toString());


                JSONObject json = jsonParser.makeHttpRequest(url_updateProgressReport,
                        "POST", paramsss);

                success = json.getInt("success");
                System.out.println( json.getString("message"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task
         * **/
        protected void onPostExecute(String file_url) {
        }
    }

    /**
     * Task to pull progress report data from the database
     */
    class getProgressReport extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Getting progress report details in background thread
         * */
        protected String doInBackground(String... params) {

            int success;
            try {
                Hashtable<String,String> paramsss = new Hashtable<String,String>();
                paramsss.put("studentid",User.getName());

                JSONObject json = jsonParser.makeHttpRequest(url_getProgressReport,
                        "POST", paramsss);
                System.out.println( json.getString("message"));

                success = json.getInt("success");
                if (success == 1) {
                    english.setText(json.getString("english"));
                    maths.setText(json.getString("maths"));
                    science.setText(json.getString("science"));
                    mothertongue.setText(json.getString("mothertongue"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task
         * **/
        protected void onPostExecute(String file_url) {
        }
    }

    /**
     * Get names of all the students to show on the drop down list
     */
    class getNames extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Getting student list details in background thread
         * */
        protected String doInBackground(String... params) {

            int success;
            try {
                Hashtable<String,String> paramsss = new Hashtable<String,String>();
                JSONObject json = jsonParser.makeHttpRequest(Config.getStudentIdList,
                        "POST", paramsss);

                success = json.getInt("success");
                System.out.println( json.getString("message"));

                if (success == 1) {
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
         * After completing background task
         * **/
        protected void onPostExecute(String file_url) {
            if(succeed) {
                ArrayAdapter adapter = new ArrayAdapter<String>(ProgressReport.this, android.R.layout.simple_spinner_dropdown_item, names);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                target.setAdapter(adapter);
                target.setOnItemSelectedListener(ProgressReport.this);
            }

        }
    }

    }
