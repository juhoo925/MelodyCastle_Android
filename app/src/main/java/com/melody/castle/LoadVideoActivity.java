package com.melody.castle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;

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
//                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//                startActivityIfNeeded(i, REQUEST_PICK_VIDEO);
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityIfNeeded(intent, REQUEST_PICK_VIDEO);

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

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
//                Toast.makeText(getApplicationContext(), "try again to request the permission.", Toast.LENGTH_LONG).show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_WRITE_EXTERNAL_CODE);

                // PERMISSIONS_WRITE_EXTERNAL_CODE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
//                Toast.makeText(getApplicationContext(), "result of the request", Toast.LENGTH_LONG).show();
            }
        } else {
            // Permission has already been granted
//            Toast.makeText(getApplicationContext(), "Permission has already been granted!!", Toast.LENGTH_LONG).show();
        }
    }

    public final int PERMISSIONS_WRITE_EXTERNAL_CODE = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == PERMISSIONS_WRITE_EXTERNAL_CODE) {
                Toast.makeText(getApplicationContext(), "PERMISSION_GRANTED!!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data ) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case REQUEST_PICK_VIDEO:
                    Uri selectedVideo = data.getData();
                    // MEDIA GALLERY
                    String video_path = getPath(selectedVideo);
                    if ( video_path != null ) {
                        String folder = getFilesDir().getAbsolutePath() + "/MelodyCastle";
                        File fp1 = new File(folder);
                        if( fp1.exists() == false ) {
                            fp1.mkdir();
                        }
                        // Storing the data in file with name as geeksData.txt
                        File file1 = new File(folder, "added_video.mp4");
                        copyFileToSDCard(video_path, file1.getPath());
                        mUriVideo = Uri.fromFile(file1);//Uri.parse(video_path);//

                        mLoadStatus.setText("Custom Video Added");
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("add_video", video_path);
                        editor.commit();
                    }
                    break;
            }
        }

    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void copyFileToSDCard( String path1, String sdCardFilePath ) {

        FileInputStream is = null;
        FileOutputStream fos = null;

        try {
            is = new FileInputStream(path1);
            if( is == null ) {
                Toast.makeText(getApplicationContext(), "is == null", Toast.LENGTH_LONG).show();
                return;
            }
            fos = new FileOutputStream(sdCardFilePath);

            byte buf[] = new byte[2048];

            int nLen = 0;
            while( (nLen = is.read(buf)) > -1 ) {
                fos.write(buf, 0, nLen);
            }
        }
        catch( Exception e ) {
            Toast.makeText(getApplicationContext(), "Exception e", Toast.LENGTH_LONG).show();
            return;
        }
        finally {
            try {
                if( is != null )
                    is.close();
                if( fos != null )
                    fos.close();
            } catch ( Exception e ) {
                Toast.makeText(getApplicationContext(), "finally Exception e", Toast.LENGTH_LONG).show();
            }
        }
    }


}