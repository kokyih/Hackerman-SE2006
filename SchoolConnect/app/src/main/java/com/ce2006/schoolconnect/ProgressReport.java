package com.ce2006.schoolconnect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ProgressReport extends Activity implements AdapterView.OnItemSelectedListener{

    private AlertDialog.Builder builder;
    JSONParser jsonParser = new JSONParser();
    JSONArray jnames = new JSONArray();
    boolean succeed = true;
    private static String url_submitfeedback = Config.submitfeedback;


    List<String> names = new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button update = (Button) findViewById(R.id.updategrades);
        Button back = (Button) findViewById(R.id.backbtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(i);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ProgressReport().execute();
            }
        });



        new ProgressReport().getNameList().execute();

    }

    }
