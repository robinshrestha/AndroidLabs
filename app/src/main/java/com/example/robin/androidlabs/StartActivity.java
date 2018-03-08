package com.example.robin.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {
    protected static final String ACTIVITY_NAME = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        Log.i(ACTIVITY_NAME,"In onCreate():");

        Button btn2= (Button)findViewById(R.id.button);
        Button btn = (Button)findViewById(R.id.startChat);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivity(intent);
               // startActivityForResult(intent,50);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new   Intent(StartActivity.this, ChatWindow.class);
                startActivity(intent);
                // startActivityForResult(intent,50);
            }
        });

    }  @Override
    protected void onStart() {
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
    public void onAcitivyResult(int requestCode,int resultCode,Intent data)
    {
        if(resultCode==50){
        Log.i(ACTIVITY_NAME,"Returned to StartActivity.onActivityResult");
    }
      else if (resultCode == Activity.RESULT_OK)
    {
        String messagePassed  = data.getStringExtra( "Response");
        Toast toast=Toast.makeText(StartActivity.this,messagePassed,Toast.LENGTH_LONG);
        toast.show();
  }
}
public void onClick(){
        Log.i(ACTIVITY_NAME, "User clicked Start Chat");
}
}