/**
 * @author Yi Heng
 * @version 1.1
 @since 2021-04-06
 */

package com.ce2006.schoolconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

/**
 * Class to view the specific feedback that the user clicked into from the list of all the feedbacks
 */
public class ViewSpecificFeedback extends Activity {

    String id;

    JSONParser jsonParser = new JSONParser();

    private static final String url_get_feedback = Config.view1feedback;

    Button back;
    TextView title;
    TextView sender;
    TextView message;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_specific_feedback);

        // save button
        back = (Button) findViewById(R.id.viewspecfb_back);

        title = (TextView) findViewById(R.id.viewspecfb_title);
        sender = (TextView) findViewById(R.id.viewspecfb_sender);
        message = (TextView) findViewById(R.id.viewspecfb_fb);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        id = i.getStringExtra("id");

        // Getting complete product details in background thread
        new GetFeedbackDetails().execute();

        /**
         * Back goes back to the list of feedbacks
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), ViewFeedback.class);
                startActivity(i);
            }
        });

    }

    /**
     * Task to get details of the specific feedback
     */
    class GetFeedbackDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Getting feedback details in background thread
         * */
        protected String doInBackground(String... params) {

            int success;
            try {

                Hashtable<String,String> paramsss = new Hashtable<String,String>();
                paramsss.put("id", id);

                JSONObject json = jsonParser.makeHttpRequest(url_get_feedback,
                        "POST", paramsss);

                success = json.getInt("success");
                if (success == 1) {
                    JSONArray productObj = json
                            .getJSONArray("feedback"); // JSON Array

                    JSONObject feedback = productObj.getJSONObject(0);

                    title.setText(feedback.getString("title"));
                    sender.setText("From : " + feedback.getString("submitid"));
                    message.setText(feedback.getString("message"));

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

}
