/**
 * @author Nicholas
 * @version 1.1
 @since 2021-04-06
 */

package com.ce2006.schoolconnect;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class to start the notification service and update every 15 seconds
 */
public class NotificationService extends Service {

    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 15;
    final Handler handler = new Handler();

    JSONParser jsonParser = new JSONParser();
    String classEnd = "";

    /**
     * On start creates the timer
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // START YOUR TASKS
        super.onStartCommand(intent, flags, startId);

        startTimer();

        return START_STICKY;
    }

    /**
     * destroys the task
     */
    @Override
    public void onDestroy() {
        // STOP YOUR TASKS
        stoptimertask();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    /**
     * Start to count and calls a task every x seconds
     */
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        timer.schedule(timerTask, 5000, Your_X_SECS * 1000);
    }

    /**
     * stops the timer task
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Init the task
     */
    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {

                        //TODO CALL NOTIFICATION FUNC
                        new GetEndingClass().execute();

                    }
                });
            }
        };
    }

    /**
     * Creates the channel for notifications to work
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "School Connect";
            String description = "Notification alert";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Task that gets called every x seconds to check whether the user has ended class or not
     */
    class GetEndingClass extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            Hashtable<String,String> params = new Hashtable<String,String>();
            String endclassvalue = User.read("id", "false", NotificationService.this);


            params.put("id", endclassvalue);

            JSONObject json = jsonParser.makeHttpRequest(Config.getClassEnd,
                    "POST", params);

            try {
                int success = json.getInt("success");

                if (success == 1) {
                    classEnd = json.getString("endClass");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task, check if child is dismissed or not. If yes, display a notification
         * **/
        protected void onPostExecute(String file_url) {
            if(classEnd.compareTo("true") == 0)
            {
                new endingClass().execute();
                classEnd = "false";

                createNotificationChannel();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this, "1")
                        .setSmallIcon(R.drawable.schlcont)
                        .setContentTitle("School Connect")
                        .setContentText("Your child has been dismissed from school")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationService.this);

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(1, builder.build());

            }
        }

    }

    /**
     * Sets the class of the teacher to end the class.
     */
    class endingClass extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            Hashtable<String,String> params = new Hashtable<String,String>();
            params.put("classID", User.getClassID());
            params.put("setEndClass" , "false");

            JSONObject json = jsonParser.makeHttpRequest(Config.endClass,
                    "POST", params);

            try {

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


}
