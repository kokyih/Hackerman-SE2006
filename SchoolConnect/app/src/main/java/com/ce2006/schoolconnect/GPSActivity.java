package com.ce2006.schoolconnect;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
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

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class GPSActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    LocationRequest request;
    GoogleApiClient client;
    LatLng latLngCurrent;
    //EditText el;

    double currentLat;
    double currentLng;

    private FusedLocationProviderClient mFusedLocationClient;
    private Location mGetedLocation;
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 101;

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
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */

    public void findSchools(View v) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
            return;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(GPSActivity.this);
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mGetedLocation = task.getResult();
                            currentLat = mGetedLocation.getLatitude();
                            currentLng = mGetedLocation.getLongitude();
                            //updateUI();
                        } else {
                            //Log.e(TAG, "no location detected");
                            //Log.w(TAG, "getLastLocation:exception", task.getException());
                        }
                    }
                });


        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("location=" + currentLat + "," + currentLng);
        stringBuilder.append("&radius=" + 1000);
        stringBuilder.append("&keyword=" + "school");
        stringBuilder.append("&key=" + "AIzaSyAVTbpSW_XBVvjRvSXddw0kfQmRJy_4hHI");

        String url = stringBuilder.toString();

        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces(this);
        getNearbyPlaces.execute(dataTransfer);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        /*LatLng ntu = new LatLng(User.getLat(), User.getLong());
        googleMap.addMarker(new MarkerOptions()
                .position(ntu)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ntu));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(16));*/

        mMap = googleMap;

        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        client.connect();
    }

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest().create();
        request.setInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        //LocationServices.FusedLocationApi.requestLocationUpdates(client, request, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
