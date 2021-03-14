package com.ce2006.schoolconnect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class ConsentForm extends ListActivity {

    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> consentFormList;

    private static String url_viewConsentForm = Config.viewConsentForm;

    JSONArray consentForm = null;

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.consentformui);

        consentFormList = new ArrayList<HashMap<String, String>>();

        back = (Button) findViewById(R.id.ConsentFormList_backbtn);

        // Loading products in Background Thread
        new LoadAllConsentForm().execute();

        // Get listview
        ListView lv = getListView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(i);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.id)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        ConsentFormSpecific.class);
                // sending pid to next activity
                in.putExtra("id", pid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });
    }
    class LoadAllConsentForm extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*pDialog = new ProgressDialog(AllProductsActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            //List<NameValuePair> params = new ArrayList<NameValuePair>();

            Hashtable<String,String> params = new Hashtable<String,String>();

            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_viewConsentForm, "GET", params);

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt("success");

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    consentForm = json.getJSONArray("ConsentForm");

                    // looping through All Products
                    for (int i = 0; i < consentForm.length(); i++) {
                        JSONObject c = consentForm.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString("id");
                        String title = c.getString("title");

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put("id", id);
                        map.put("title", title);

                        // adding HashList to ArrayList
                        consentFormList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    //Intent i = new Intent(getApplicationContext(),
                    //NewProductActivity.class);
                    // Closing all previous activities
                    //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(i);
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
            // dismiss the dialog after getting all products
            //pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            ConsentForm.this, consentFormList,
                            R.layout.list_consentform, new String[] { "id",
                            "title"},
                            new int[] { R.id.id, R.id.title });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}
