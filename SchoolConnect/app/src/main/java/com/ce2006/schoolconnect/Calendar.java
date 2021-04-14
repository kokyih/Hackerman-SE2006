/**
 * @author Ooi Kok Yih
 * @version 1.1
 @since 2021-04-06
 */

package com.ce2006.schoolconnect;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This class is for the calendar page of the app.
 */
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

    /**
     *
     * @param savedInstancesState on creation it will create and assign all the buttons and UI elements
     */
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

        /**
         * Function to identify which date is clicked and then set it to date variable
         */
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth+ "_" + (month+1) + "_" + year;
                date.setText(selectedDate);
                new getDateDetails().execute();
            }
        });

        /**
         * If back button is clicked, go back to main menu
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(i);
            }
        });

        /**
         * Upon pressing update, the task updateCalender() will be started and will update the event according to the date selected
         */
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new updateCalendar().execute();
            }
        });
    }

    /**
     * The task to call the PHP script and sends the new information
     */
    class updateCalendar extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            builder = new AlertDialog.Builder(Calendar.this);
            builder.setCancelable(true);
            builder.setMessage("Failed to update calendar");
        }

        protected String doInBackground(String... args) {

            Hashtable<String,String> params = new Hashtable<String,String>();
            params.put("date", date.getText().toString());
            params.put("title", eventdetails.getText().toString());

            JSONObject json = jsonParser.makeHttpRequest(Config.updatecalendar,
                    "POST", params);

            // check for success tag
            try {
                int success = json.getInt("success");
                System.out.println( json.getString("message"));

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

    /**
     * Task to pull the selected date's event detail using PHP scripts to connect to the database
     *
     */
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

            try {
                int success = json.getInt("success");

                System.out.println(json.getString("message") + " Success: " + success);

                if (success == 1) {
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
        }
    }

}
