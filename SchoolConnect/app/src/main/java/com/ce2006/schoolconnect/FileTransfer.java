/**
 * @author Ong Jun Sen
 * @version 1.1
 @since 2021-04-06
 */

package com.ce2006.schoolconnect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class to handle the downloading of images
 */
public class FileTransfer {

    /**
     *
     * @param link url to download the file from
     * @param filename name of the file to pull from database
     * @return returns the bitmap of the image to set on the UI
     */
    public Bitmap downloadImage(String link, String filename)
    {
        Bitmap yourpic = null;

        try {

            URL url = new URL(link + filename + ".jpg");
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
