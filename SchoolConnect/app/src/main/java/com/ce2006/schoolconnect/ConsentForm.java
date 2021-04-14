/**
 * @author Ooi Kok Yih
 * @version 1.1
 @since 2021-04-06
 */

package com.ce2006.schoolconnect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * Consentform class for the overall lists of consent form.
 */
public class ConsentForm extends ListActivity {

    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> consentFormList;

    private static String url_viewConsentForm = Config.viewConsentForm;

    JSONArray consentForm = null;

    Button back;
    Button newConsentForm;

    /**
     *
     * @param savedInstanceState on creation it will create and link a bunch of buttons from the XML.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.consentformui);

        consentFormList = new ArrayList<HashMap<String, String>>();

        back = (Button) findViewById(R.id.ConsentFormList_backbtn);
        newConsentForm = (Button) findViewById(R.id.ConsentFormList_newBtn);

        new LoadAllConsentForm().execute();

        ListView lv = getListView();

        if(User.getRole().compareTo("teacher")!=0)
        {
            newConsentForm.setVisibility(View.GONE);
        }

        /**
         * Upon clicking back button, go back to main menu
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(i);
            }
        });

        /**
         * Clicking this brings to a new page to enter details to submit a new consent form
         */
        newConsentForm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                Intent i = new Intent(getApplicationContext(), SubmitConsentForm.class);
                startActivity(i);
            }
        });

        /**
         * Sets the function to find which box is clicked and send the ID to the next UI class.
         */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String pid = ((TextView) view.findViewById(R.id.cfid)).getText()
                        .toString();

                Intent in = new Intent(getApplicationContext(),
                        ConsentFormSpecific.class);
                // sending pid to next activity
                in.putExtra("id", pid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });
    }

    /**
     * Task to call PHP script to pull all the consent form from the database
     */
    class LoadAllConsentForm extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * getting all the consent form from consentform database
         * */
        protected String doInBackground(String... args) {

            Hashtable<String,String> params = new Hashtable<String,String>();

            params.put("id",User.getID());

            JSONObject json = jParser.makeHttpRequest(url_viewConsentForm, "GET", params);

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt("success");

                if (success == 1) {
                    consentForm = json.getJSONArray("consentform");

                    // looping through All consent forms
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * after pulling data, update the listview to show the whole list of consent forms on the UI
         * **/
        protected void onPostExecute(String file_url) {
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            ConsentForm.this, consentFormList,
                            R.layout.list_consentform, new String[] { "id",
                            "title"},
                            new int[] { R.id.cfid, R.id.cftitle });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}
