package com.ce2006.schoolconnect;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainMenu extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    TextView Username;
    TextView School;

    // url to create new product
    private static String url_create_product = "https://10.27.141.30/CE2006/create_product.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.add_product);

        setContentView(R.layout.mainmenu_screen);

        Button btnLogout = (Button) findViewById(R.id.logout);

        Button gpsButton = (Button) findViewById(R.id.gpsBtn);

        Username = (TextView) findViewById(R.id.username);
        Username.setText(User.getName());

        School = (TextView) findViewById(R.id.schoolName);
        School.setText(User.getSchool());

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity

                User.Logout();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }

        });

        gpsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity

                //User.Logout();

                Intent i = new Intent(getApplicationContext(), GPSActivity.class);
                startActivity(i);

            }

        });




    }
}