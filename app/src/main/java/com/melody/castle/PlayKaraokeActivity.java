package com.melody.castle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class PlayKaraokeActivity extends AppCompatActivity {

    VideoView mVideoView;
    MediaController mMediaController;
    Uri mUriPath;
    TextView mTxtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_karaoke);

        mTxtTitle = (TextView) findViewById(R.id.txtTitle);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        mMediaController= new MediaController(this);
        mMediaController.setAnchorView(mVideoView);
        //specify the location of media file
//        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/media/1.mp4");
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.chapter01);
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
                finish();
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // display a toast when an error is occured while playing an video
                Toast.makeText(getApplicationContext(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show();
                finish();
                return false;
            }
        });

    }


    private void getVideoInfo() {

        Intent i = getIntent();
        int cid = i.getIntExtra("k_id", 1);
        switch (cid) {
            case 1:
                mTxtTitle.setText("Tick Tock");
                mUriPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.karaoke_ticktock);
                break;
            case 2:
                mTxtTitle.setText("Going For Goals");
                mUriPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.karaoke_goingforgoals);
                break;
            case 3:
                mTxtTitle.setText("Anti Bullying");
                mUriPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.karaoke_antibullying);
                break;
            case 4:
                mTxtTitle.setText("Gifts And Talents");
                mUriPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.karaoke_giftsandtalents);
                break;
            case 5:
                mTxtTitle.setText("Friendship");
                mUriPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.karaoke_friendship);
                break;
            case 6:
                mTxtTitle.setText("Double Spell");
                mUriPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.karaoke_doublingspell);
                break;
        }

    }

}