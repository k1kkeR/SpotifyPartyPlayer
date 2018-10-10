package com.example.tormod.spotifypartyplayer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



public class UserSession extends AppCompatActivity{
    Sessions connectedSession;
    private Adapter mAdapter;
    ArrayList<String> songQueue = new ArrayList<>();
    String partyName;
    String currentSong;
    String currentArtist;
    NsdHelper mNsdHelper;
    private Handler mUpdateHandler;
    public static final String TAG = "SQSession";
    ChatConnection mConnection;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session);



        TextView user = findViewById(R.id.userName);
        user.setText(MainActivity.theUser.getNickName());


        TextView genre = findViewById(R.id.partyGenre);
        genre.setText("?");

        //listview adapter

        songQueue.add("random sang");
        songQueue.add("random sang 2");
        updateQueue();

        //NSD
        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String[] sessionInfo = msg.getData().getString("msg").split("¤");
                partyName = sessionInfo[0];
                currentSong = sessionInfo[1];
                currentArtist = sessionInfo[2];
                ArrayList<String> queueList = new ArrayList<>();
                for(int i = 3; i< sessionInfo.length; i++){
                    queueList.add(sessionInfo[i]);
                }
                songQueue = queueList;
                setSession();
            }
        };
        mConnection = new ChatConnection(mUpdateHandler);

        mNsdHelper = new NsdHelper(this);
        mNsdHelper.initializeNsd();
        getListFromSession();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void updateQueue() {
        ListView lv = findViewById(R.id.queueList);
        mAdapter = new ArrayAdapter<>(this, R.layout.activity_list_element, songQueue);
        lv.setAdapter((ListAdapter) mAdapter);
    }
    public void getQueueList(){

    }
    public void setSession(){
        connectedSession = new Sessions(0, partyName, "?", "", "", "bestPasswordEvvah", currentSong
                , currentArtist, null, songQueue, null, null, false, false);
        updateQueue();
        updateSessionInfo();
    }
    public void updateSessionInfo(){
        TextView songTitle = findViewById(R.id.songTitle);
        TextView artistName = findViewById(R.id.artist);
        artistName.setText(currentArtist);
        songTitle.setText(currentSong);
    }

    public void queueSong(View view) {

        // alertdialog bulder example from page: https://stackoverflow.com/questions/10903754/input-text-dialog-android
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a spotify uri");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!input.getText().toString().equals("")) {
                    addSongToQueue(input.getText().toString());
                } else {
                    dialog.cancel();
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void addSongToQueue(String uri){
        NsdServiceInfo service = availableSessions.sessionToConnectTo;
        if (service != null) {
            Log.d(TAG, "Connecting.");
            Log.d("ASDASDSAD", service.getHost() + " : " + service.getPort());

            mConnection.connectToServer(service.getHost(),
                    service.getPort());
        } else {
            Log.d(TAG, "No service to connect to!");
        }
        mConnection.sendMessage("queue¤" + uri);
    }
    public void getListFromSession(){
        NsdServiceInfo service = availableSessions.sessionToConnectTo;
        if (service != null) {
            Log.d(TAG, "Connecting.");
            mConnection.connectToServer(service.getHost(),
                    service.getPort());
        } else {
            Log.d(TAG, "No service to connect to!");
        }
        mConnection.sendMessage("update" + "nothing");
    }

    public void nextSong(View view) {
    }

    public void pauseResume(View view) {
    }
}
