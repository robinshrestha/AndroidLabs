package com.example.robin.androidlabs;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    Button sendButton;
    EditText editText;
    ListView listView;
    ArrayList<String> chat = new ArrayList<>();
    ChatAdapter messageAdapter;
    ChatDatabaseHelper chatDatabaseHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    Boolean frameExist;
    Cursor cursor;
    MessageFragment messageFragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        sendButton = (Button) findViewById(R.id.sendButton);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter((ListAdapter)messageAdapter);

        // Create a temporary ChatDatavaseHelper object
        chatDatabaseHelper = new ChatDatabaseHelper(this);
        database = chatDatabaseHelper.getWritableDatabase();
        cursor = database.query(ChatDatabaseHelper.TABLE_NAME,new String[]{chatDatabaseHelper.KEY_ID,ChatDatabaseHelper.KEY_MESSAGE},null,null,null,null,null);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
                Log.i("ChatWindow","SQL MESSAGE: "+message);
                Log.i("ChatWindow","Cursor's column count = "+cursor.getColumnCount());
                chat.add(message);
                cursor.moveToNext();
            }
        }
        for(int i = 0; i < cursor.getColumnCount(); i++){
            Log.i("ChatWindow","The "+i+" column is "+cursor.getColumnName(i));
        }


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMessage = editText.getText().toString();
                chat.add(newMessage);
                contentValues = new ContentValues();
                contentValues.put(ChatDatabaseHelper.KEY_MESSAGE,newMessage);
                database.insert(ChatDatabaseHelper.TABLE_NAME,null,contentValues);

                messageAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

        listView.setOnItemClickListener((AdapterView<?> adapterView, View view, int position, long id)->{
            frameExist = findViewById(R.id.frameLayout)!=null;
            long messageId = messageAdapter.getItemId(position);
            String message = messageAdapter.getItem(position);
            Bundle bundle = new Bundle();
            bundle.putString("Message",message);
            bundle.putLong("MessageId",messageId);

            // Running on a tablet, use a Bundle to pass message and database id
            if(frameExist){
                messageFragment = new MessageFragment(ChatWindow.this);
                messageFragment.setArguments(bundle);
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout,messageFragment);
                fragmentTransaction.commit();
                Log.i("ChatWindow", "Running on a Tablet");
            }else{
                Intent intent = new Intent(ChatWindow.this,MessageDetails.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,50);
                Log.i("ChatWindow","Running on a phone");
            }
        });

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(chatDatabaseHelper != null ){
            chatDatabaseHelper.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == 100)
        {
            Long ID = data.getLongExtra("MessageId",-1);
            deleteMessage(ID);
        }
    }

    public void deleteMessage(Long id){

        database.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID + "="+ id,null);

        chat.clear();
        cursor = database.query(ChatDatabaseHelper.TABLE_NAME,new String[]{chatDatabaseHelper.KEY_ID,ChatDatabaseHelper.KEY_MESSAGE},null,null,null,null,null);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
                chat.add(message);
                cursor.moveToNext();
            }
        }
        messageAdapter.notifyDataSetChanged();
    }

    private class ChatAdapter extends ArrayAdapter<String>{
        public ChatAdapter(Context ctx){
            super(ctx,0);
        }
        public int getCount(){
            return chat.size();
        }

        public String getItem(int position){
            return chat.get(position);
        }

        public View getView(int position,View convertView,ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(   getItem(position)  ); // get the string at position
            //position++;
            return result;
        }

        public long getId(int position){
            return position;
        }

        public long getItemId(int position){
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(chatDatabaseHelper.KEY_ID));
        }


    }

}
