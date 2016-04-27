package com.monkporter.zafran.zafranmain;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.monkporter.zafran.R;

public class Splash extends AppCompatActivity {
    ProgressBar pb;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        pb = (ProgressBar) findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int mProgressStatus = 0;
                while (mProgressStatus < 100) {
                    mProgressStatus +=1;

                    // Update the progress bar
                    final int finalMProgressStatus = mProgressStatus;
                    mHandler.post(new Runnable() {
                        public void run() {
                            System.out.println("Sajal"+finalMProgressStatus);
                            pb.setProgress(finalMProgressStatus);
                        }
                    });
                }
            }
        }).start();
    }
}