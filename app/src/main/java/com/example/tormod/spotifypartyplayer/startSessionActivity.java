package com.example.tormod.spotifypartyplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;




public class startSessionActivity extends AppCompatActivity {

    public static Sessions theParty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_session);
    }

    public void randomizeInput(View view) {

    }
    public void createSession(View view){
        theParty = new Sessions();
        EditText partyName = findViewById(R.id.sessionName);
        EditText partygenre = findViewById(R.id.genre);
        EditText password1 = findViewById(R.id.password1);
        EditText password2 = findViewById(R.id.password2);
        EditText password3 = findViewById(R.id.password3);
        if(!partyName.getText().toString().equals("") && !partygenre.getText().toString().equals("")){
            theParty.setPartyName(partyName.getText().toString());
            theParty.setGenre(partygenre.getText().toString());
            theParty.setPassword1(password1.getText().toString());
            theParty.setPassword2(password2.getText().toString());
            theParty.setPassowrd3(password3.getText().toString());
            Intent intent = new Intent(this, sessionActivity.class);
            startActivity(intent);
        }
        else {
            password3.setText("Fill out the two first inputs");
        }
    }
}
