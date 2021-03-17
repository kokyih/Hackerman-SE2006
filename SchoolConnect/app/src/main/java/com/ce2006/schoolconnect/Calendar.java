package com.ce2006.schoolconnect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Calendar extends AppCompatActivity {

    private AlertDialog.Builder builder;
    JSONParser jsonParser = new JSONParser();
    JSONArray jnames = new JSONArray();
    boolean succeed = true;
    private static String url_updatecalendar = Config.updatecalendar;
    private static String url_getdatedetails = Config.getdatedetails;

    CalendarView calendarView;
    Button back;
    Button update;
    TextView date;
    EditText eventdetails;

    String details = "";

    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView((R.layout.calendarui));

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        update = (Button) findViewById(R.id.updateEvent_btn);
        back = (Button) findViewById(R.id.calendarbck_btn);
        date = (TextView) findViewById(R.id.date);
        eventdetails = (EditText) findViewById(R.id.eventdetails);

        if(User.getRole().compareTo("teacher") != 0) {
            update.setVisibility(View.GONE); //remove update button
            eventdetails.setEnabled(false); //disable user to edit text
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth+ "_" + month + "_" + year;
                date.setText(selectedDate);
                //need to pull details from database here
                //eventdetails.setText("send help from database");
                new getDateDetails().execute();
            }
        });

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
                new updateCalendar().execute();
            }
        });
    }

    class updateCalendar extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            builder = new AlertDialog.Builder(Calendar.this);
            builder.setCancelable(true);
            builder.setMessage("Failed to update calendar");
        }

        protected String doInBackground(String... args) {
            //String targetname = currentTarget;//target.getText().toString();
            //String titles = title.getText().toString();
            //String messages = message.getText().toString();

            Hashtable<String,String> params = new Hashtable<String,String>();
            params.put("date", date.getText().toString());
            params.put("title", eventdetails.getText().toString());

            JSONObject json = jsonParser.makeHttpRequest(Config.updatecalendar,
                    "POST", params);

            // check for success tag
            try {
                int success = json.getInt("success");

                System.out.println( json.getString("message"));

                if (success == 1) {
                    // successfully created product
                    //Intent i = new Intent(getApplicationContext(), SubmitFeedback.class);
                    //startActivity(i);

                    // closing this screen
                    //finish();
                } else {

                    //succeed = false;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            //pDialog.dismiss();
            if(!succeed)
                builder.show();
        }

    }

    class getDateDetails extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            Hashtable<String, String> params = new Hashtable<String, String>();

            params.put("date", date.getText().toString());

            JSONObject json = jsonParser.makeHttpRequest(Config.getdatedetails,
                    "POST", params);

            // check for success tag
            try {
                int success = json.getInt("success");

                System.out.println(json.getString("message") + " Success: " + success);

                if (success == 1) {
                    //succeed = true;
                    //System.out.println(json.getJSONObject("details"));

                    eventdetails.setText(json.getString("details"));

                } else {

                    eventdetails.setText("No events");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            //pDialog.dismiss();
            //if (!succeed)
                //builder.show();

        }
    }

}
