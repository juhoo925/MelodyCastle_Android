package com.melody.castle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;

public class KaraokeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karaoke);

        ImageButton btnTicktock = (ImageButton) findViewById(R.id.btnTicktock);
        btnTicktock.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gotoPlayVideo(1);
            }
        });

        ImageButton btnGoingFor = (ImageButton) findViewById(R.id.btnGoingFor);
        btnGoingFor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gotoPlayVideo(2);
            }
        });

        ImageButton btnAntibullying = (ImageButton) findViewById(R.id.btnAntibullying);
        btnAntibullying.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gotoPlayVideo(3);
            }
        });

        ImageButton btnGifts = (ImageButton) findViewById(R.id.btnGifts);
        btnGifts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gotoPlayVideo(4);
            }
        });

        ImageButton btnFriendship = (ImageButton) findViewById(R.id.btnFriendship);
        btnFriendship.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gotoPlayVideo(5);
            }
        });

        ImageButton btnDouble = (ImageButton) findViewById(R.id.btnDouble);
        btnDouble.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gotoPlayVideo(6);
            }
        });
    }

    private void gotoPlayVideo(int id) {

        Intent i = new Intent( KaraokeActivity.this, PlayKaraokeActivity.class);
        i.putExtra("k_id", id);
        startActivity(i);
    }

}