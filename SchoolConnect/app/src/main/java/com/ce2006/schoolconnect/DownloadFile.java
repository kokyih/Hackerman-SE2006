package com.ce2006.schoolconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Hashtable;

public class DownloadFile extends Activity {

    private static String url_download = Config.download;

    EditText Name;
    ImageView dlImage;

    FileTransfer file = new FileTransfer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_image);

        dlImage = (ImageView) findViewById(R.id.downloaddeImage);
        Name = (EditText) findViewById(R.id.dl_name);

        Button back = (Button) findViewById(R.id.dl_back);
        Button download = (Button) findViewById(R.id.download_btn);

        Name.setVisibility(View.GONE);
        download.setVisibility(View.GONE);

        if(User.picture != null)
        {
            dlImage.setImageBitmap(User.picture);
        }

        new downloadPic().execute();

        //dlImage.setImageBitmap(User.picture);

        //Intent i = new Intent(getApplicationContext(), DownloadFile.class);
        //startActivity(i);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Do login in background thread
                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(i);

            }

        });

        download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                new downloadPic().execute();

                dlImage.setImageBitmap(User.picture);

                Intent i = new Intent(getApplicationContext(), DownloadFile.class);
                startActivity(i);

            }

        });
    }

    class downloadPic extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            //String name = Name.getText().toString();
            String name = User.getClassID();
            //System.out.println(name);

            Bitmap pic = file.downloadImage(Config.downloadTB,name);

            User.setBitmap(pic);

            //Intent i = new Intent(getApplicationContext(), DownloadFile.class);
            //startActivity(i);

            return null;
        }

        protected void onPostExecute(String file_url) {
            dlImage.setImageBitmap(User.picture);
        }

    }

}
