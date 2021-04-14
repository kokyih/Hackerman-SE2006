/**
 * @author Yi Heng
 * @version 1.1
 @since 2021-04-06
 */

package com.ce2006.schoolconnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;


import android.util.Log;

/**
 * Class to call all the connection neccessary to connect the app to php scripts to the database
 */
public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }



    /**
     * function get json from url
     * by making HTTP POST or GET mehtod
     * @param url link of the address to make the request
     * @param method Either post or get method
     * @param params parameters to pass while going to the address
     * @return JSONObject to pull data out of .
     */
    public JSONObject makeHttpRequest(String url, String method,
                                      Hashtable<String,String> params)
    {
        String s_params = "";

        for(String key : params.keySet())
        {
            s_params += key + "=" + params.get(key) + "&";
        }

        // Making HTTP request
        try
        {
            if(method == "POST")
            {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                os.write(s_params.getBytes());
                os.flush();
                os.close();

                int responseCode = con.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    is = con.getInputStream();
                } else {
                    System.out.println("POST request not worked");
                }
            }
            else if(method == "GET")
            {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    is = con.getInputStream();
                } else {
                    System.out.println("GET request not worked");
                }
            }

        }

        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + '\n');
            }
            in.close();

            json = response.toString();
        }
        catch (Exception e)
        {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try
        {
            jObj = new JSONObject(json);
        }
        catch (JSONException e)
        {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}