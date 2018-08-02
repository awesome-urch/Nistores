package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.j256.ormlite.dao.Dao;
import com.nistores.awesomeurch.nistores.Folders.Helpers.DatabaseHelper;
import com.nistores.awesomeurch.nistores.Folders.Helpers.UserTable;
import com.nistores.awesomeurch.nistores.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Intent intent;
    ConstraintLayout loader;
    String error;
    SharedPreferences preferences;
    DatabaseHelper helper;
    Dao<UserTable, Integer> dao;
    Integer const_id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        username = findViewById(R.id.input_name);
        password = findViewById(R.id.input_password);
        Button sign_in = findViewById(R.id.btn_sign_in);
        AppCompatButton sign_up = findViewById(R.id.sign_up);
        loader = findViewById(R.id.loader_layout);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (preferences.contains("user")) {
            intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void signIn(){
        boolean valid = true;
        String name = username.getText().toString();
        String pass = password.getText().toString();

        if(name.isEmpty()){
            valid = false;
            username.setError("Please enter your username");
        }
        if(pass.isEmpty()){
            valid = false;
            password.setError("Enter your password");
        }
        if(valid){

            loader.setVisibility(View.VISIBLE);
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            //this is the url where you want to send the request
            String url = "https://www.nistores.com.ng/api/src/routes/process_one.php?request=login&username="+name+"&password="+pass;
            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            loader.setVisibility(View.GONE);
                            // Display the response string.
                            //Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_SHORT).show();
                            Log.d("RTN",response.toString());
                            try {
                                Integer err = response.getInt("error");
                                JSONObject data = response.getJSONObject("data");
                                if(err==0){
                                    logUserIn(data);

                                }else{
                                    Toast.makeText(getApplicationContext(),"Invalid username or password",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Sorry an error occurred. Try again",Toast.LENGTH_SHORT).show();
                    Log.d("VOLLEY",error.toString());

                }
            });

            queue.add(jsonObjectRequest);

        }else{
            Toast.makeText(getApplicationContext(),"Fill the required fields",Toast.LENGTH_SHORT).show();
        }

    }

    public void logUserIn(JSONObject data){
        String id = null;
        try {
            id = data.getString("merchant_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String info = data.toString();
        //save user id in shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user",id).apply();

        //save user data in user table
        helper = new DatabaseHelper(getApplicationContext());
        try{
            dao = helper.getUserDao();
            UserTable userTable = new UserTable();
            userTable.setId(const_id);
            userTable.setMerchant_id(id);
            userTable.setAll(info);
            dao.create(userTable);

        }catch(Exception e){
            e.printStackTrace();
            Log.d("ERR",e.toString());
        }

        intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void signUp(){
        intent = new Intent(this,CreateAccountActivity.class);
        startActivity(intent);
    }
}
