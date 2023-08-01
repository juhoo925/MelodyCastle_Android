package com.melody.castle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class PlayVideoActivity extends AppCompatActivity {

    VideoView mVideoView;
    LinearLayout mViewControl;
    ImageButton mBtnTopNext;
    ImageButton mBtnNext;
    ImageButton mBtnReplay;

    MediaController mMediaController;

    TextView mTxtTitle;

    Uri mUriPath;

    Uri mUri71Path;
    Uri mUri72Path;
    Uri mUriAddPath = null;

    int selCh7Id = 1;

    int[] pauseTime;
    int[] pauseTime1 = {93, 109, 131, 140};
    int[] pauseTime2 = {165, 293, 390};
    int[] pauseTime3 = {97, 250, 326};
    int[] pauseTime4 = {16, 255};
    int[] pauseTime5 = {49, 144, 217};
    int[] pauseTime6 = {};
    int[] pauseTime7 = {27};

    int[] replayTime;
    int[] replayTime1 = {416};
    int[] replayTime2 = {160, 555};
    int[] replayTime3 = {212};
    int[] replayTime4 = {107};
    int[] replayTime5 = {344};
    int[] replayTime6 = {};
    int[] replayTime7 = {159};

    int[] replayStartTime;
    int[] replayStartTime1 = {342};
    int[] replayStartTime2 = {71, 485};
    int[] replayStartTime3 = {114};
    int[] replayStartTime4 = {87};
    int[] replayStartTime5 = {265};
    int[] replayStartTime6 = {};
    int[] replayStartTime7 = {115};

    int nReplayIndex = 0;

    int nCount = 0;
    int nChapterId = 1;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        sp = getApplicationContext().getSharedPreferences("MelodyCastle", Context.MODE_PRIVATE);
        String path1 = sp.getString("chapter7_1", "");
        String path2 = sp.getString("chapter7_2", "");
        mUri71Path = Uri.fromFile(new File(path1));
        mUri72Path = Uri.fromFile(new File(path2));
        String added_path = sp.getString("add_video", "");
        if ( added_path.equals("") == false ) {
            mUriAddPath = Uri.parse(added_path);
        }

        mTxtTitle = (TextView) findViewById(R.id.txtTitle);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        mMediaController= new MediaController(this);
        mMediaController.setAnchorView(mVideoView);
        mMediaController.setVisibility(View.INVISIBLE);
        getVideoInfo();

        //Setting MediaController and URI, then starting the videoView
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoURI(mUriPath);
        mVideoView.requestFocus();
        mVideoView.start();

        // perform set on completion listener event on video view
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // do something when the end of the video is reached
                if ( nChapterId != 7 ) {
                    finish();
                }
                else {
                    if (mUriAddPath == null) {
                        finish();
                    }
                    else {
                        if ( selCh7Id == 1 ) {
                            mUriPath = mUriAddPath;
                            selCh7Id = 2;
                        }
                        else if ( selCh7Id == 2 ) {
                            mUriPath = mUri72Path;
                            selCh7Id = 3;
                        }
                        else {
                            finish();
                            return;
                        }
                        mVideoView.setVideoURI(mUriPath);
                        mVideoView.requestFocus();
                        mVideoView.start();
                    }
                }
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // display a toast when an error is occured while playing an video
                Toast.makeText(getApplicationContext(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show();
                mHandler.sendEmptyMessageDelayed(1, 2);
                return false;
            }
        });

        mViewControl = (LinearLayout) findViewById(R.id.viewControl);
        mBtnTopNext = (ImageButton) findViewById(R.id.btnNextTop);
        mBtnTopNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if ( mVideoView.isPlaying() == false ) {
                    mVideoView.start();
                }
                mBtnTopNext.setVisibility(View.GONE);
            }
        });
        mBtnNext = (ImageButton) findViewById(R.id.btnNext);
        mBtnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mViewControl.setVisibility(View.GONE);
                mVideoView.start();
            }
        });
        mBtnReplay = (ImageButton) findViewById(R.id.btnReplay);
        mBtnReplay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if ( mVideoView.isPlaying() == false ) {
                    nCount = replayStartTime[nReplayIndex];
                    mVideoView.seekTo(replayStartTime[nReplayIndex] * 1000 );
                    mVideoView.start();
                }
                mViewControl.setVisibility(View.GONE);
            }
        });

        isActivityRunning = true;
        run();
    }

    private void getVideoInfo() {

        Intent i = getIntent();
        int cid = i.getIntExtra("c_id", 1);
        nChapterId = cid;
        switch (cid) {
            case 1:
                mTxtTitle.setText("Chapter1");
                pauseTime = pauseTime1;
                replayTime = replayTime1;
                replayStartTime = replayStartTime1;
                mUriPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.chapter01);
                break;
            case 2:
                mTxtTitle.setText("Chapter2");
                pauseTime = pauseTime2;
                replayTime = replayTime2;
                replayStartTime = replayStartTime2;
                mUriPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.chapter02);
                break;
            case 3:
                mTxtTitle.setText("Chapter3");
                pauseTime = pauseTime3;
                replayTime = replayTime3;
                replayStartTime = replayStartTime3;
                mUriPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.chapter03);
                break;
            case 4:
                mTxtTitle.setText("Chapter4");
                pauseTime = pauseTime4;
                replayTime = replayTime4;
                replayStartTime = replayStartTime4;
                mUriPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.chapter04);
                break;
            case 5:
                mTxtTitle.setText("Chapter5");
                pauseTime = pauseTime5;
                replayTime = replayTime5;
                replayStartTime = replayStartTime5;
                mUriPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.chapter05);
                break;
            case 6:
                mTxtTitle.setText("Chapter6");
                pauseTime = pauseTime6;
                replayTime = replayTime6;
                replayStartTime = replayStartTime6;
                mUriPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.chapter06);
                break;
            case 7:
                mTxtTitle.setText("Chapter7");
                pauseTime = pauseTime7;
                replayTime = replayTime7;
                replayStartTime = replayStartTime7;
                if ( mUriAddPath == null ) {
                    mUriPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.chapter07);
                }
                else {
                    selCh7Id = 1;
                    mUriPath = mUri71Path;
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        isActivityRunning = false;
        super.onDestroy();
    }

    private boolean isActivityRunning = false;
    private void run() {

        Thread aThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while(isActivityRunning) {

                    if ( mVideoView.isPlaying() == false ) {

                    }
                    else {
                        for ( int i = 0; i< pauseTime.length; i++ ) {
                            if ( nCount == pauseTime[i] ) {
                                mHandler.sendEmptyMessage(0);
                                break;
                            }
                        }
                        for ( int i = 0; i< replayTime.length; i++ ) {
                            if ( nCount == replayTime[i] ) {
                                nReplayIndex = i;
                                mHandler.sendEmptyMessage(2);
                                break;
                            }
                        }
                        nCount++;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        });
        aThread.start();
    }

    private final Handler mHandler = new Handler(new Callback() {

        @SuppressLint("ResourceAsColor")
        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if ( msg.what == 0 ) {
                mVideoView.pause();
                mBtnTopNext.setVisibility(View.VISIBLE);
            }
            else if ( msg.what == 2 ) {
                mVideoView.pause();
                mViewControl.setVisibility(View.VISIBLE);
            }
            else if ( msg.what == 1 ) {
                finish();
            }
            return false;
        }
    });

}