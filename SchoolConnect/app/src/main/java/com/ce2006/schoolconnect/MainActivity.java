package com.ce2006.schoolconnect;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

public class MainActivity extends Activity {

    //private ProgressDialog pDialog;
    private AlertDialog.Builder builder;

    JSONParser jsonParser = new JSONParser();
    EditText EmailAddr;
    EditText password;

    boolean succeed = true;


    private static String url_login = Config.login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.main_screen);

        setContentView(R.layout.login_screen);

        // Edit Text
        EmailAddr = (EditText) findViewById(R.id.EmailAddr);
        password = (EditText) findViewById(R.id.password);

        // Buttons
        Button loginAcc = (Button) findViewById(R.id.loginBtn);
        Button signup = (Button) findViewById(R.id.signupBtn);

        // view products click event
        loginAcc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /*
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
                //Intent i = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(i);*/

                // Do login in background thread
                new loginActivity().execute();

            }

        });

        // view products click event
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), MainActivity.RegistrationActivity.class);
                startActivity(i);

            }
        });

    }

    class loginActivity extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(true);
            builder.setMessage("Invalid email/password \nPlease Try again");
            //builder.show();
        }

        /**
         * Login
         * */
        protected String doInBackground(String... args) {
            String email = EmailAddr.getText().toString();
            String pw = password.getText().toString();

            Hashtable<String,String> params = new Hashtable<String,String>();
            params.put("email", email);
            params.put("password", pw);

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_login,
                    "POST", params);

            // check log cat fro response
            //Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");

                System.out.println( json.getString("message"));

                if (success == 1) {
                    // successfully created product

                    User.setName(json.getString("name"));
                    User.setSchool(json.getString("school"));
                    User.setID(json.getString("userid"));
                    User.setRole(json.getString("role"));
                    User.setClassID(json.getString("class"));

                    User.save("id", User.getID(),MainActivity.this);

                    if(User.getRole().compareTo("bus") != 0) {
                        Intent i = new Intent(getApplicationContext(), MainMenu.class);
                        startActivity(i);
                    }
                    else if (User.getRole().compareTo("bus") == 0)
                    {
                        Intent i = new Intent(getApplicationContext(), Busdriver.class);
                        startActivity(i);
                    }
                    // closing this screen
                    finish();

                } else {

                    succeed = false;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            //pDialog.dismiss();
            if(!succeed)
                builder.show();

            startService(new Intent(MainActivity.this, NotificationService.class));
        }

    }
    public static class RegistrationActivity extends Activity {

        private AlertDialog.Builder builder;

        JSONParser jsonParser = new JSONParser();
        EditText r_email;
        EditText r_pw;
        EditText r_name;
        EditText r_school;

        boolean succeed = true;

        private static String url_register = Config.register;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.registration);

            // Edit Text
            r_email = (EditText) findViewById(R.id.r_emailAddr);
            r_pw = (EditText) findViewById(R.id.r_password);
            r_name = (EditText) findViewById(R.id.r_name);
            r_school = (EditText) findViewById(R.id.r_school);

            // Buttons
            Button signup = (Button) findViewById(R.id.r_signup);
            Button cancel = (Button) findViewById(R.id.r_cancel);

            // view products click event
            signup.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    // Do login in background thread
                    new Register().execute();

                }

            });

            // view products click event
            cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // Launching create new product activity
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);

                }
            });

        }

        class Register extends AsyncTask<String, String, String> {

            /**
             * Before starting background thread Show Progress Dialog
             * */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                builder = new AlertDialog.Builder(RegistrationActivity.this);
                builder.setCancelable(true);
                builder.setMessage("Account creation failed");
                //builder.show();
            }

            /**
             * Login
             * */
            protected String doInBackground(String... args) {
                String email = r_email.getText().toString();
                String pw = r_pw.getText().toString();
                String name = r_name.getText().toString();
                String school = r_school.getText().toString();

                if(pw.compareTo("") == 0 || name.compareTo("") == 0 || email.compareTo("") == 0 || school.compareTo("") == 0) {
                    succeed = false;
                    return null;
                }

                Hashtable<String,String> params = new Hashtable<String,String>();
                params.put("email", email);
                params.put("password", pw);
                params.put("name", name);
                params.put("school", school);

                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_register,
                        "POST", params);

                // check log cat fro response
                //Log.d("Create Response", json.toString());

                // check for success tag
                try {
                    int success = json.getInt("success");

                    System.out.println( json.getString("message"));

                    if (success == 1) {
                        // successfully created product

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);

                        // closing this screen
                        finish();
                    } else {

                        succeed = false;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            /**
             * After completing background task Dismiss the progress dialog
             * **/
            protected void onPostExecute(String file_url) {
                // dismiss the dialog once done
                //pDialog.dismiss();
                if(!succeed)
                    builder.show();
            }

        }

    }

}