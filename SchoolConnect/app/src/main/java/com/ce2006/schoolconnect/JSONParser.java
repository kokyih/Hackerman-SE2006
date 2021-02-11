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
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


import android.util.Log;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method,
                                      Hashtable<String,String> params)
    {
        //return null;
        String s_params = "";

        for(String key : params.keySet())
        {
            s_params += key + "=" + params.get(key) + "&";
        }

        // Making HTTP request
        try
        {
            // check for request method

            if(method == "POST")
            {
                // request method is POST
                // defaultHttpClient

                //DefaultHttpClient httpClient = new DefaultHttpClient();
                //HttpPost httpPost = new HttpPost(url);
                //httpPost.setEntity(new UrlEncodedFormEntity(params));

                //HttpResponse httpResponse = httpClient.execute(httpPost);
                //HttpEntity httpEntity = httpResponse.getEntity();
                //is = httpEntity.getContent();

                // NEW CODE
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                //con.setRequestProperty("User-Agent", USER_AGENT);

                // For POST only - START
                con.setDoOutput(true);

                System.out.println("ENTERED HERE IN POST");

                OutputStream os = con.getOutputStream();
                os.write(s_params.getBytes());
                os.flush();
                os.close();

                //is = con.getInputStream();

                int responseCode = con.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    is = con.getInputStream();
                } else {
                    System.out.println("POST request not worked");
                }

                System.out.println("ALL GOOD HERE IN POST");

            }
            else if(method == "GET")
            {
                // request method is GET
                /*
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();*/

                //URL urlobj = new URL(url);
                //HttpURLConnection urlc = (HttpURLConnection) urlobj.openConnection();

                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");

                //is = con.getInputStream();

                System.out.println("ENTERED HERE IN GET");

                int responseCode = con.getResponseCode();

                System.out.println("Enter here 5");

                //System.out.println("GET Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    is = con.getInputStream();
                } else {
                    System.out.println("GET request not worked");
                }

                System.out.println("ALL GOOD IN GET");

            }

        }

        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        /*catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }*/
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            /*BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();*/

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + '\n');
            }
            in.close();

            //json = " {\"products\":[{\"pid\":\"3\",\"name\":\"nicholas\",\"price\":\"234.00\",\"created_at\":\"2021-01-31 01:25:32\",\"updated_at\":null},{\"pid\":\"2\",\"name\":\"wow\",\"price\":\"523.00\",\"created_at\":\"2021-01-30 20:04:46\",\"updated_at\":null},{\"pid\":\"4\",\"name\":\"nicholas\",\"price\":\"234.00\",\"created_at\":\"2021-01-31 01:25:46\",\"updated_at\":null},{\"pid\":\"5\",\"name\":\"nicholas\",\"price\":\"234.00\",\"created_at\":\"2021-01-31 01:26:27\",\"updated_at\":null},{\"pid\":\"6\",\"name\":\"nicholas\",\"price\":\"234.00\",\"created_at\":\"2021-01-31 01:32:03\",\"updated_at\":null},{\"pid\":\"7\",\"name\":\"nicholas\",\"price\":\"234.00\",\"created_at\":\"2021-01-31 01:32:11\",\"updated_at\":null},{\"pid\":\"8\",\"name\":\"nicholas\",\"price\":\"234.00\",\"created_at\":\"2021-02-07 17:16:00\",\"updated_at\":null},{\"pid\":\"9\",\"name\":\"fuck\",\"price\":\"234.00\",\"created_at\":\"2021-02-07 17:28:52\",\"updated_at\":null},{\"pid\":\"10\",\"name\":\"fuck\",\"price\":\"234.00\",\"created_at\":\"2021-02-07 17:39:26\",\"updated_at\":null},{\"pid\":\"11\",\"name\":\"fuck\",\"price\":\"234.00\",\"created_at\":\"2021-02-07 18:08:10\",\"updated_at\":null},{\"pid\":\"12\",\"name\":\"fuck\",\"price\":\"234.00\",\"created_at\":\"2021-02-07 18:37:10\",\"updated_at\":null},{\"pid\":\"13\",\"name\":\"WAH HELp\",\"price\":\"234.00\",\"created_at\":\"2021-02-07 18:38:04\",\"updated_at\":null}],\"success\":1} " ;

            //System.out.println(json);

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