package com.example.tormod.spotifypartyplayer;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class sessionActivity extends AppCompatActivity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback, AdapterView.OnItemClickListener {
    //spotify
    private static final String CLIENT_ID = "499356cac5894f899639e9150ce6ced1";
    private static final String REDIRECT_URI = "hihihehehaha://callback";
    private Player mPlayer;
    private static final int REQUEST_CODE = 420;
    private boolean updateOrNotToUpdate = false;
    ListView lv;
    private ArrayAdapter mAdapter;
    //nsd
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

        TextView party = findViewById(R.id.partyName);
        party.setText(startSessionActivity.theParty.getPartyName());

        TextView genre = findViewById(R.id.partyGenre);
        genre.setText(startSessionActivity.theParty.getGenre());

        updateQueue();


        //spotify
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        //NSD
        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mNsdHelper.discoverServices();
                String[] handleMe = msg.getData().getString("msg").split("¤");
                if(Objects.equals(handleMe[0], "update")){
                    returnList();
                }
                else if(Objects.equals(handleMe[0], "queue")){
                    addSongToQueue(handleMe[1]);
                    returnList();
                }
            }
        };
        mConnection = new ChatConnection(mUpdateHandler);

        mNsdHelper = new NsdHelper(this);
        mNsdHelper.initializeNsd();
        if(mConnection.getLocalPort() > -1) {
            mNsdHelper.registerService(mConnection.getLocalPort());
        } else {
            Log.d(TAG, "ServerSocket isn't bound.");
        }
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    //NSD functions
    public void returnList(){
        NsdServiceInfo service = mNsdHelper.getChosenServiceInfo();
        if (service != null) {
            Log.d(TAG, "Connecting.");
            mConnection.connectToServer(service.getHost(),
                    service.getPort());
        } else {
            Log.d(TAG, "No service to connect to!");
        }
        mConnection.sendSessionMsg();

    }
    //Spotify and UI functions
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

    public void updateCurrentSong(String songName, String songArtist, String uri) {
        //textveiw update
        startSessionActivity.theParty.setCurrentSong(songName);
        startSessionActivity.theParty.setCurrentArtist(songArtist);
        TextView songTitle = findViewById(R.id.songTitle);
        TextView artistName = findViewById(R.id.artist);
        artistName.setText(songArtist);
        songTitle.setText(songName);
        Log.d("songplayed 2", uri);
        mPlayer.playUri(null, uri, 0 ,0);
    }

    public void addSongToQueue(String uri) {
        Songs info;
        if(mPlayer.getMetadata().currentTrack != null){
            String songPaused = mPlayer.getMetadata().currentTrack.uri;
            Integer wherePaused = (int) mPlayer.getPlaybackState().positionMs;
            info = getSongInfo(uri);
            mPlayer.playUri(null, songPaused, 0, wherePaused);
            startSessionActivity.theParty.addToQueueList(info);
            TextView artistName = findViewById(R.id.artist);
            if(Objects.equals(artistName.getText(), "4chan")){
                onNextSong();
            }
            else{
                updateQueue();
            }
        }
        else {
            info = getSongInfo(uri);
            startSessionActivity.theParty.addToQueueList(info);
            onNextSong();
        }
    }

    public void nextSong(View view) {
        onNextSong();
    }
    public void pauseResume(View view){
        TextView pR = (TextView)findViewById(R. id. pauseResume);

        if(mPlayer.getMetadata().currentTrack != null){
            if(mPlayer.getPlaybackState().isPlaying){
                mPlayer.pause(null);
                pR.setText(getString(R.string.page4_Resume));
            }
            else {
                mPlayer.resume(null);
                pR.setText(getString(R.string.page4_pauseResume));
            }
        }
    }

    //updates the ListVeiw
    public void updateQueue() {
        lv = findViewById(R.id.queueList);
        mAdapter = new ArrayAdapter<>(this, R.layout.activity_list_element, getNameQueue());
        lv.setAdapter( mAdapter);
        lv.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView itemClicked = (TextView)view;
        ArrayList<Songs> temp = new ArrayList<>();
        for(int j = 0; j < startSessionActivity.theParty.getQueueList().size(); j++){
            if(j == 0){
                temp.add(startSessionActivity.theParty.findIndexOfQueue(i));
            }
            else if(j <= i){
                temp.add(startSessionActivity.theParty.findIndexOfQueue(j-1));
            }
            else{
                temp.add(startSessionActivity.theParty.findIndexOfQueue(j));
            }
        }
        startSessionActivity.theParty.setQueueList(temp);
        Log.d("asdasd", itemClicked.getText() + " : " + i);
        onNextSong();
    }

    //takes a song array and turns it into a songTitle array
    public ArrayList<String> getNameQueue() {
        ArrayList<String> TitleQueue = new ArrayList<>();
        for (int i = 0; i < startSessionActivity.theParty.getQueueList().size(); i++) {

            String songTitle = startSessionActivity.theParty.findIndexOfQueue(i).getSongTitle();
            TitleQueue.add(i, songTitle);
        }
        return TitleQueue;
    }

    public void onNextSong() {
        if(startSessionActivity.theParty.getQueueList().size() > 0) {
            ArrayList<Songs> tempList = new ArrayList<>();
            String songTitle = null;
            String songArtist = null;
            String songUri = null;
            for (int i = 0; i < startSessionActivity.theParty.getQueueList().size(); i++) {
                if (i == 0) {
                    songUri = startSessionActivity.theParty.findIndexOfQueue(0).getUri();
                    songTitle = startSessionActivity.theParty.findIndexOfQueue(0).getSongTitle();
                    songArtist = startSessionActivity.theParty.findIndexOfQueue(0).getArtist();
                } else {
                    tempList.add(i - 1, startSessionActivity.theParty.findIndexOfQueue(i));
                }
            }
            startSessionActivity.theParty.setQueueList(tempList);
            updateQueue();
            updateCurrentSong(songTitle, songArtist, songUri);
        }
        else{
            updateCurrentSong("No song queued, initiating rickrolling sequence", "4chan", "spotify:track:4uLU6hMCjMI75M1A2tKUQC");
        }


    }
    public Songs getSongInfo(String uri) {
        Songs song;
        mPlayer.playUri(null, uri, 0, 0);
        while (!Objects.equals(mPlayer.getMetadata().contextUri, uri)) {
        }
        song = new Songs(uri, mPlayer.getMetadata().currentTrack.name, mPlayer.getMetadata().currentTrack.artistName,
                mPlayer.getMetadata().currentTrack.albumCoverWebUrl, (int) mPlayer.getMetadata().currentTrack.durationMs, 0);
        mPlayer.pause(null);

        return song;

    }
    //spotfyfunctions
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(sessionActivity.this);
                        mPlayer.addNotificationCallback(sessionActivity.this);
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }
    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        if (playerEvent == PlayerEvent.kSpPlaybackNotifyAudioDeliveryDone) {
            onNextSong();
        }

    }


    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");

        //mPlayer.playUri(null, "spotify:track:3VHJN9R1HaJAp6SbeMT0V5", 0, 220000);

    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
