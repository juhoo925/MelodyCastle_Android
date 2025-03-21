package com.melody.castle;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        isActivityRunning = true;
        run();
    }

    @Override
    protected void onDestroy() {
        isActivityRunning = false;
        super.onDestroy();
    }

    private boolean isActivityRunning = false;
    public void run() {

        Thread aThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                int nCount = 0;
                while(isActivityRunning) {

                    if ( nCount > 2 ) {
                        Intent i = new Intent( SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    nCount++;
                }
            }
        });
        aThread.start();

    }
}
