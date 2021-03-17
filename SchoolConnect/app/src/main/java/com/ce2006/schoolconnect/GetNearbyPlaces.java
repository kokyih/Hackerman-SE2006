package com.ce2006.schoolconnect;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetNearbyPlaces extends AsyncTask<Object,String, String>
{

    GoogleMap mMap;
    String url;
    InputStream is;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String data;
    Context context;

    public GetNearbyPlaces(Context context)
    {
        this.context = context;
    }

    @Override
    protected String doInBackground(Object... params) {

        mMap = (GoogleMap)params[0];
        url = (String) params[1];

        try
        {
            URL myurl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)myurl.openConnection();

            httpURLConnection.connect();

            is = httpURLConnection.getInputStream();

            bufferedReader = new BufferedReader(new InputStreamReader(is));

            String line = "";
            stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line);
            }

            data = stringBuilder.toString();

        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }


        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        //return null;

        System.out.println("Enter post execute for get places");

        try
        {
            JSONObject parentObject = new JSONObject(s);
            JSONArray resultsArray = parentObject.getJSONArray("results");

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
                markerOptions.title(vicinity);
                markerOptions.position(latLng);

                mMap.addMarker(markerOptions);

            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

}
