package com.example.robin.androidlabs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageFragment extends Fragment{
    private View view;
    private TextView messageTV;
    private TextView idTV;
    private Button deleteButton;
    private Bundle bundle;
    protected ChatWindow chatWindow  ;

    public MessageFragment(){
        super();
    }

    @SuppressLint("ValidFragment")
    public MessageFragment(ChatWindow window){
        chatWindow =window;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        bundle = getArguments();
        view = inflater.inflate(R.layout.activity_message_fragment,container,false);
        messageTV = (TextView) view.findViewById(R.id.messageDetails);
        messageTV.setText(bundle.getString("Message"));
        idTV = (TextView) view.findViewById(R.id.messageID);
        idTV.setText("ID: "+ bundle.getLong("MessageId"));
        deleteButton = (Button) view.findViewById(R.id.deleteMessage);


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatWindow==null){
                    Intent intent = new Intent(getActivity(),MessageFragment.class);
                    intent.putExtra("MessageId",bundle.getLong("MessageId"));
                    getActivity().setResult(100,intent);
                    getActivity().finish();
                }else{

                    getFragmentManager().beginTransaction().remove(MessageFragment.this).commit();
                  chatWindow.deleteMessage(bundle.getLong("MessageId"));

                }
            }
        });



        return view;
    }


}
