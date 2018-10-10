package com.example.tormod.spotifypartyplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tormod.spotifypartyplayer.NsdHelper;
import java.util.ArrayList;


public class availableSessions extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView list;
    public static ArrayList<String> availableParties = new ArrayList<>();
    NsdHelper mNsdHelper;
    public static NsdServiceInfo sessionToConnectTo;
    public static final String TAG = "SQSession";
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_to_session);
        //updateList();
        Log.d("Hvafaen", "1");
        //NSD
        mNsdHelper = new NsdHelper(this);
        Log.d("Hvafaen", "2");
        mNsdHelper.initializeNsd();
        mNsdHelper.discoverServices();
        updateList();

    }

    public void updateList(){
        Log.d("Hvafaen", "8");

        list = findViewById(R.id.availableList);
        ArrayList<String> tomt = new ArrayList<>();
        tomt.add("There is no available parties");
        ArrayAdapter<String> adapter;
        if(availableParties.size() == 0){
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tomt);
        }
        else{
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, availableParties);

        }
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView itemClicked = (TextView)view;
        updateList();
        Log.d("clickedlist", availableParties.size()+"");
        if(availableParties.size() > 0){
            connectToTheSession(null);
        }

        int index = checkID((String) itemClicked.getText());
        //startSessionActivity.theParty.setPartyName(MainActivity.availableParties.get(index).getPartyName());
        //startSessionActivity.theParty.setGenre(MainActivity.availableParties.get(index).getGenre());

    }
    private int checkID(String name){
/*
        for(int i = 0; i< MainActivity.availableParties.size(); i++){
            if(MainActivity.availableParties.get(i).getPartyName() == name){
                return i;
            }
        }*/
        return 0;
    }

    public void connectToTheSession(View view){
        sessionToConnectTo = mNsdHelper.getChosenServiceInfo();
        Intent intent = new Intent(this, UserSession.class);
        startActivity(intent);
    }
}
