package com.melody.castle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class LoadVideoActivity extends AppCompatActivity {

    TextView mLoadStatus;
    VideoView mVideoView;
    MediaController mMediaController;

    LinearLayout mViewPlayer;

    Uri mUriVideo;

    SharedPreferences sp;

    static final int REQUEST_PICK_VIDEO = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_video);

        sp = getApplicationContext().getSharedPreferences("MelodyCastle", Context.MODE_PRIVATE);

        mViewPlayer = (LinearLayout) findViewById(R.id.viewPlay);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        mMediaController= new MediaController(this);
        mMediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mMediaController);

        mLoadStatus = (TextView) findViewById(R.id.txtLoadStatus);
        mLoadStatus.setText("Default Video");

        ImageButton btnBrowse = (ImageButton) findViewById(R.id.btnBrowse);
        btnBrowse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityIfNeeded(i, REQUEST_PICK_VIDEO);
            }
        });
        ImageButton btnDefault = (ImageButton) findViewById(R.id.btnDefault);
        btnDefault.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String default_path = sp.getString("default", "");
                mUriVideo = Uri.fromFile(new File(default_path));
                mLoadStatus.setText("Default Video");
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("add_video", default_path);
                editor.commit();
            }
        });
        ImageButton btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mViewPlayer.setVisibility(View.VISIBLE);
                mVideoView.setVideoURI(mUriVideo);
                mVideoView.requestFocus();
                mVideoView.start();
            }
        });

        Button btnAddVideo = (Button) findViewById(R.id.btnAddVideo);
        btnAddVideo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });

        // perform set on completion listener event on video view
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // do something when the end of the video is reached
                mViewPlayer.setVisibility(View.GONE);
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // display a toast when an error is occured while playing an video
                Toast.makeText(getApplicationContext(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data ) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case REQUEST_PICK_VIDEO:
                    Uri selectedVideo = data.getData();
                    String[] filePathColumn = { MediaStore.Video.Media.DATA };

                    Cursor cursor = getApplicationContext().getContentResolver().query(selectedVideo,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String video_path = cursor.getString(columnIndex);
                    cursor.close();

                    mUriVideo = Uri.fromFile(new File(video_path));
                    mLoadStatus.setText("Custom Video Added");
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("add_video", video_path);
                    editor.commit();
                    break;
            }
        }

    }
}