package com.monkporter.zafran.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.monkporter.zafran.Interface.GetConnection;
import com.monkporter.zafran.R;
import com.monkporter.zafran.helper.PrefManager;
import com.monkporter.zafran.model.CheckConnection;
import com.monkporter.zafran.rest.CheckConnectionApiClient;
import com.monkporter.zafran.utility.CommonMethod;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Refresh extends AppCompatActivity implements View.OnClickListener {
    Button btn;
    private String previousScreen = null;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection_screen);
        context = this;
        previousScreen = getIntent().getStringExtra("previousScreen");
        btn = (Button) findViewById(R.id.refresh_btn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_id);
        setSupportActionBar(toolbar);
        btn.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        PrefManager pref = PrefManager.getInstance(Refresh.this);
        boolean pressExit = pref.isExit();
        if (!pressExit) {
            //TODO: sajal This code is to be removed
            Toast.makeText(Refresh.this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            pref.pressExit(true);

            Intent launchNextActivity;
            launchNextActivity = new Intent(this, Refresh.class);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(launchNextActivity);
        } else {
            pref.pressExit(false);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh_btn:
                if (CommonMethod.isNetworkAvailable(Refresh.this)) {
                    if (previousScreen.equalsIgnoreCase("splash")) {
                        setResult(RESULT_OK);
                        finish();
                    }
                }
                break;

        }

    }
}
