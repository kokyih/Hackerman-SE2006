/**
 * @author Yi Heng
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
 * Class to show the entire list of feedbacks to view
 */
public class ViewFeedback extends ListActivity {

    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> feedbackList;

    private static String url_viewfeedback = Config.viewfeedback;

    JSONArray feedback = null;

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_feedback);

        feedbackList = new ArrayList<HashMap<String, String>>();

        back = (Button) findViewById(R.id.feedbackList_backbtn);

        // Loading products in Background Thread
        new LoadAllFeedbacks().execute();

        // Get listview
        ListView lv = getListView();

        /**
         * Back goes back to the main menu
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(i);
            }
        });

        /**
         * To handle input of clicking on of the boxes in the list
         */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.id)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        ViewSpecificFeedback.class);
                // sending pid to next activity
                in.putExtra("id", pid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });
    }

    /**
     * Task to load all the feedbacks and show it on the current page
     */
    class LoadAllFeedbacks extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {

            Hashtable<String,String> params = new Hashtable<String,String>();

            JSONObject json = jParser.makeHttpRequest(url_viewfeedback, "GET", params);

            try {
                int success = json.getInt("success");

                if (success == 1) {
                    feedback = json.getJSONArray("feedback");

                    // looping through All Products
                    for (int i = 0; i < feedback.length(); i++) {
                        JSONObject c = feedback.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString("id");
                        String title = c.getString("title");

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put("id", id);
                        map.put("title", title);

                        // adding HashList to ArrayList
                        feedbackList.add(map);
                    }
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
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            ViewFeedback.this, feedbackList,
                            R.layout.list_feedback, new String[] { "id",
                            "title"},
                            new int[] { R.id.id, R.id.title });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }

}
