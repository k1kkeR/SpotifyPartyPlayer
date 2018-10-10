package com.example.tormod.spotifypartyplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class MainActivity extends AppCompatActivity{
    public static Users theUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        theUser = new Users();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //onclick functions
    public void connectToSession(View view) {
        Intent intent = new Intent(this, availableSessions.class);
        startActivity(intent);

    }

    public void startSession(View view) {
        Intent intent = new Intent(this, startSessionActivity.class);
        startActivity(intent);
    }
}