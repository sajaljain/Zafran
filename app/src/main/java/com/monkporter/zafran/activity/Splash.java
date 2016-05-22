package com.monkporter.zafran.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.monkporter.zafran.R;

public class Splash extends AppCompatActivity {
    ProgressBar pb;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        pb = (ProgressBar) findViewById(R.id.progressBar);

        pb.setProgress(1);


    }
}