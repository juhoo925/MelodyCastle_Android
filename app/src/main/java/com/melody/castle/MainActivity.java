package com.melody.castle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    ScrollView mViewChapters;
    LinearLayout mViewOptions;

    TextView mTxtChapters;
    TextView mTxtOptions;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getApplicationContext().getSharedPreferences("MelodyCastle", Context.MODE_PRIVATE);

        mViewChapters = (ScrollView) findViewById(R.id.viewChapters);
        mViewOptions = (LinearLayout) findViewById(R.id.viewOptions);

        mTxtChapters = (TextView) findViewById(R.id.txtChapters);
        mTxtChapters.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onClickTab(1);
            }
        });
        mTxtOptions = (TextView) findViewById(R.id.txtOptions);
        mTxtOptions.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onClickTab(2);
            }
        });

        ImageButton btnChapter1 = (ImageButton) findViewById(R.id.btnChapter1);
        btnChapter1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gotoPlayVideo(1);
            }
        });

        ImageButton btnChapter2 = (ImageButton) findViewById(R.id.btnChapter2);
        btnChapter2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gotoPlayVideo(2);
            }
        });
        ImageButton btnChapter3 = (ImageButton) findViewById(R.id.btnChapter3);
        btnChapter3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gotoPlayVideo(3);
            }
        });
        ImageButton btnChapter4 = (ImageButton) findViewById(R.id.btnChapter4);
        btnChapter4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gotoPlayVideo(4);
            }
        });
        ImageButton btnChapter5 = (ImageButton) findViewById(R.id.btnChapter5);
        btnChapter5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gotoPlayVideo(5);
            }
        });
        ImageButton btnChapter6 = (ImageButton) findViewById(R.id.btnChapter6);
        btnChapter6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gotoPlayVideo(6);
            }
        });
        ImageButton btnChapter7 = (ImageButton) findViewById(R.id.btnChapter7);
        btnChapter7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gotoPlayVideo(7);
            }
        });
        ImageButton btnKaraoke = (ImageButton) findViewById(R.id.btnKaraoke);
        btnKaraoke.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent( MainActivity.this, KaraokeActivity.class);
                startActivity(i);
            }
        });
        ImageButton btnload = (ImageButton) findViewById(R.id.btnload);
        btnload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent( MainActivity.this, LoadVideoActivity.class);
                startActivity(i);
            }
        });

        String desFolderPath = Environment.getExternalStorageDirectory().getPath() + "/MelodyCastle";
        File fp1 = new File(desFolderPath);
        if( fp1.exists() == false ) {
            fp1.mkdir();
        }
        copyAssetFileToSDCard( "chapter071.mp4", desFolderPath + "/chapter071.mp4");
        copyAssetFileToSDCard( "chapter072.mp4", desFolderPath + "/chapter072.mp4");
        copyAssetFileToSDCard( "defaultvideo.mp4", desFolderPath + "/defaultvideo.mp4");

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("chapter7_1", desFolderPath + "/chapter071.mp4");
        editor.putString("chapter7_2", desFolderPath + "/chapter072.mp4");
        editor.putString("default", desFolderPath + "/defaultvideo.mp4");
        editor.commit();
    }

    private void gotoPlayVideo(int id) {
        Intent i = new Intent( MainActivity.this, PlayVideoActivity.class);
        i.putExtra("c_id", id);
        startActivity(i);
    }

    private void onClickTab(int index) {

        if ( index == 1 ) {
            mTxtChapters.setBackgroundColor(Color.parseColor("#4f1f01"));
            mTxtOptions.setBackgroundColor(Color.parseColor("#ba4803"));
            mViewChapters.setVisibility(View.VISIBLE);
            mViewOptions.setVisibility(View.GONE);
        }
        else {
            mTxtChapters.setBackgroundColor(Color.parseColor("#ba4803"));
            mTxtOptions.setBackgroundColor(Color.parseColor("#4f1f01"));
            mViewChapters.setVisibility(View.GONE);
            mViewOptions.setVisibility(View.VISIBLE);
        }
    }

    private void copyAssetFileToSDCard( String assetFilePath, String sdCardFilePath ) {

        if( new File(sdCardFilePath).exists() == true )
            return;

        AssetManager assetMng = this.getAssets();
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            is = assetMng.open(assetFilePath);

            if( is == null )
                return;
            fos = new FileOutputStream(sdCardFilePath);

            byte buf[] = new byte[2048];

            int nLen = 0;
            while( (nLen = is.read(buf)) > -1 ) {
                fos.write(buf, 0, nLen);
            }
        }
        catch( Exception e ) {
            return;
        }
        finally {
            try {
                if( is != null )
                    is.close();
                if( fos != null )
                    fos.close();
            } catch ( Exception e ) {
            }
        }
    }
}

