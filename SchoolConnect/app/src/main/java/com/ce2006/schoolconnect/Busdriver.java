/**
 * @author Ooi Kok Yih
 * @version 1.1
 @since 2021-04-06
 */

package com.ce2006.schoolconnect;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

public class Busdriver extends Activity {


    GPSTracker gps;
    double latitude = 0;
    double longitude = 0;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestLocationPermission();

        setContentView(R.layout.busloc);

        Button logout = (Button) findViewById(R.id.busloc_back);
        TextView locationTxt = (TextView) findViewById(R.id.locationText);
        Button update = (Button) findViewById(R.id.updateLoc);

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Do login in background thread
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        // Show location button click event
        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Create class object
                gps = new GPSTracker(Busdriver.this);

                // Check if GPS enabled
                if(gps.canGetLocation())
                {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                    locationTxt.setText("Your Location is - \nLat: " + latitude + "\nLong: " + longitude);

                    System.out.println("Your Location is - \nLat: " + latitude + "\nLong: " + longitude);

                    new updateLoc().execute();

                }
                else
                {
                    // Can't get location.
                    // GPS or network is not enabled.
                    // Ask user to enable GPS/network in settings.
                    gps.showSettingsAlert();
                }
            }
        });
    }

    protected void requestLocationPermission(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            return;

        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.ACCESS_FINE_LOCATION))
        {
            // explain why need permission here;
        }

        ActivityCompat.requestPermissions((Activity) this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == 1)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }

        }
    }

    class updateLoc extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            Hashtable<String,String> params = new Hashtable<String,String>();
            params.put("location", latitude + "," + longitude);
            params.put("uid", User.getID());

            JSONObject json = jsonParser.makeHttpRequest(Config.updateLocation,
                    "POST", params);

            try {
                int success = json.getInt("success");

                System.out.println( json.getString("message"));

                if (success == 1) {
                } else {
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
