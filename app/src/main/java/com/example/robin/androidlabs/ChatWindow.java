package com.example.robin.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;


import java.util.ArrayList;



public class ChatWindow extends Activity {
    protected static final String ACTIVITY_NAME = "ChatWindow";


    ArrayList<String> list = new ArrayList<>();
    ListView listView;
    EditText editText;
    Button button;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        ChatDatabaseHelper helper = new ChatDatabaseHelper(this);
        ChatAdapter messageAdapter =new ChatAdapter( this );

        db = helper.getWritableDatabase();
        Cursor cursor = db.query(true, ChatDatabaseHelper.TABLE_NAME,
                new String[] { ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                ChatDatabaseHelper.KEY_MESSAGE + " Not Null" , null, null, null, null, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast() ) {
            list.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            messageAdapter.notifyDataSetChanged();;
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }

        Log.i(ACTIVITY_NAME, "Cursor's  column count =" + cursor.getColumnCount() );


        listView = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.sendButton);

        listView.setAdapter (messageAdapter);
        button.setOnClickListener(e->{
            list.add(editText.getText().toString());
            ContentValues contentValues = new ContentValues();
            contentValues.put(ChatDatabaseHelper.KEY_MESSAGE,editText.getText().toString());
            db.insert(ChatDatabaseHelper.TABLE_NAME, null, contentValues);
            messageAdapter.notifyDataSetChanged();
            editText.setText("");
        });


    }

    private class ChatAdapter extends ArrayAdapter<String>{
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return list.size();
        }
        public String getItem(int position){
            return list.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(   getItem(position)  ); // get the string at position
            return result;
        }

        public long getId(int position){
            return position;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
