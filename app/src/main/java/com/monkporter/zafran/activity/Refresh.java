package com.monkporter.zafran.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.monkporter.zafran.R;

public class Refresh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_id);
        setSupportActionBar(toolbar);
    }
}
