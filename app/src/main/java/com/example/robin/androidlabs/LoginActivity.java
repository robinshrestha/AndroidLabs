package com.example.robin.androidlabs;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
    protected static final String ACTIVITY_NAME = "StartActivity";
    //public static final String file="userFile";
    //  public static final String firstTime="email?@domain.com";
    //private static final String usearName= "loginText";
    EditText id, pswd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "in onCreate() : ");

        id = findViewById(R.id.email_input);
        pswd = findViewById(R.id.password_login);

        Button btn = (Button) findViewById(R.id.btn_login);
        EditText loginName = (EditText) findViewById(R.id.email_input);

        SharedPreferences sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("id")) {
            id.setText(sharedPreferences.getString("id", ""));
        }
        if (sharedPreferences.contains("pswd")) {
            pswd.setText(sharedPreferences.getString("pswd", ""));
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getSharedPreferences("pref", 0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("id", id.getText().toString());
                editor.putString("pswd", pswd.getText().toString());
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);

            }
        });
    }
    @Override
    protected void onStart ()
    {
        Log.i(ACTIVITY_NAME, "In onStart()");
        super.onStart();
    }

    protected void onResume() {
        Log.i(ACTIVITY_NAME, "In onResume()");
        super.onResume();
    }

    protected void onPause() {
        Log.i(ACTIVITY_NAME, "In onPause()");
        super.onPause();
    }

    protected void onStop() {
        Log.i(ACTIVITY_NAME, "In onStop()");
        super.onStop();

    }

    protected void onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
    }
}