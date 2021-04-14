/**
 * @author Gavin
 * @version 1.1
 @since 2021-04-06
 */

package com.ce2006.schoolconnect;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class GPSActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    LocationRequest request;
    GoogleApiClient client;
    LatLng latLngCurrent;

    double currentLat;
    double currentLng;

    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = new JSONObject();

    private FusedLocationProviderClient mFusedLocationClient;
    private Location mGetedLocation;
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 101;

    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * This function is called upon clicking school button from viewing all nearby schools.
     * @param v is the view object from the UI
     */
    public void findSchools(View v) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
            return;
        }

        gps = new GPSTracker(GPSActivity.this);

        // Check if GPS enabled
        if(gps.canGetLocation())
        {
            currentLat = gps.getLatitude();
            currentLng = gps.getLongitude();

            User.setLat(currentLat);
            User.setLat(currentLng);

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + currentLat + "\nLong: " + currentLng, Toast.LENGTH_LONG).show();

            System.out.println("Your Location is - \nLat: " + currentLat + "\nLong: " + currentLng);

        }
        else
        {
            gps.showSettingsAlert();
        }

        System.out.println(currentLat + " , " + currentLng);

        LatLng newlat = new LatLng(currentLat, currentLng);

        mMap.addMarker(new MarkerOptions()
                .position(newlat)
                .title("You"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newlat));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));


        new getPlaces().execute();
    }

    /**
     * When map is loaded, call this function
     * @param googleMap is a google map object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        client.connect();
    }

    /**
     * When location is detected to be changed , update location of the marker
     * @param location is a location object with lat and long members
     */
    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        if(location == null)
        {
            Toast.makeText(getApplication(), "Location not found." , Toast.LENGTH_SHORT).show();
        }
        else
        {
            latLngCurrent = new LatLng(location.getLatitude(),location.getLongitude());

            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLngCurrent,15);
            mMap.animateCamera(update);

            MarkerOptions options = new MarkerOptions();
            options.position(latLngCurrent);
            options.title("Current location");
            mMap.addMarker(options);

        }

    }

    /**
     * Upon connection, set priority and interval of the GPS.
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest().create();
        request.setInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /**
     * Task to get all nearby schools based on current location on the GPS
     */
    class getPlaces extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Getting nearby schools details in background thread
         * */
        protected String doInBackground(String... params) {

            try {

                Hashtable<String,String> paramss = new Hashtable<String,String>();

                StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                stringBuilder.append("location=" + currentLat + "," + currentLng);
                stringBuilder.append("&radius=" + 1000);
                stringBuilder.append("&keyword=" + "school");
                stringBuilder.append("&key=" + "AIzaSyAVTbpSW_XBVvjRvSXddw0kfQmRJy_4hHI");

                String url = stringBuilder.toString();

                jsonObject = jsonParser.makeHttpRequest(url,
                        "POST", paramss);

                if(jsonObject.getJSONArray("results") != null)
                {

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing getting all nearby school, update and place markers
         * **/
        protected void onPostExecute(String file_url) {
            try
            {
                JSONArray resultsArray = jsonObject.getJSONArray("results");

                for(int i = 0 ; i < resultsArray.length(); i++)
                {
                    JSONObject jsonObject = resultsArray.getJSONObject(i);
                    JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");

                    String latitude = locationObj.getString("lat");
                    String longitude = locationObj.getString("lng");

                    JSONObject nameObject = resultsArray.getJSONObject(i);

                    String name_school = nameObject.getString("name");
                    String vicinity = nameObject.getString("vicinity");

                    LatLng latLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(name_school);
                    markerOptions.position(latLng);

                    mMap.addMarker(markerOptions);

                    System.out.println(name_school + "Adding marker");

                }

            } catch (JSONException e)
            {
                e.printStackTrace();
            }



        }
    }


}
