package com.ce2006.schoolconnect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

public class FileTransfer {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    static String uploadurl = Config.upload;
    static String downloadUrl = Config.download;

    public JSONObject uploadImage(Hashtable<String,String> params)
    {
        return null;
    }

    public Bitmap downloadImage(String filename)
    {
        Bitmap yourpic = null;

        try {

            URL url = new URL(downloadUrl + filename);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            final InputStream input = connection.getInputStream();

            yourpic = BitmapFactory.decodeStream(input);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return yourpic;
    }

}
