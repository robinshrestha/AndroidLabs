package com.example.robin.androidlabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends Activity {
    protected static final String ACTIVITY_NAME = "StartActivity";
    //private CheckBox cb;
    static final int REQUEST_IMAGE_CAPTURE = 50;
   // private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME, "in on Create():");

        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        Switch switch2 = (Switch) findViewById(R.id.switch1);
        final CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox);

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CharSequence text = "Switch is On";// "Switch is Off"
                    int duration = Toast.LENGTH_SHORT; //= Toast.LENGTH_LONG if Off

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration); //this is the ListActivity
                    toast.show(); //display your message box


                } else {
                    CharSequence text = "Switch is OFF";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();

                }
            }

        });


        CheckBox cb = (CheckBox) findViewById(R.id.checkBox);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
// 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.dialog_message) //Add a dialog message to strings.xml
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        Intent resultIntent = new Intent();
                        CharSequence text = getResources().getString(R.string.response);
                        //int duration = Toast.LENGTH_SHORT; //= Toast.LENGTH_LONG if Off
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                        resultIntent.putExtra("Response", "Here is my response");
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog

                        CharSequence text = getResources().getString(R.string.response);
                        //int duration = Toast.LENGTH_SHORT; //= Toast.LENGTH_LONG if Off
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                        checkbox.setChecked(false);
                    }
                });
                builder.show();

            }

        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
            imageButton.setImageBitmap(imageBitmap);
        }
    }
    @Override
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
}
