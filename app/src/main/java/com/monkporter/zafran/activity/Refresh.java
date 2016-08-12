package com.monkporter.zafran.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.monkporter.zafran.Interfece.GetConnection;
import com.monkporter.zafran.R;
import com.monkporter.zafran.helper.PrefManager;
import com.monkporter.zafran.model.CheckConnection;
import com.monkporter.zafran.rest.CheckConnectionApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Refresh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_id);
        setSupportActionBar(toolbar);
    }

    public void checkConnection(View v) {
        GetConnection getConnection = CheckConnectionApiClient.getClient().create(GetConnection.class);
        Call<CheckConnection> call = getConnection.getResponse();
        call.enqueue(new Callback<CheckConnection>() {
            @Override
            public void onResponse(Call<CheckConnection> call, Response<CheckConnection> response) {
                Log.d("Connection ","established");
                finish();
            }

            @Override
            public void onFailure(Call<CheckConnection> call, Throwable t) {
                Log.d("Connection", "onFailure =" + t.getMessage());
            }
        });
    }
    @Override
    public void onBackPressed(){
        PrefManager pref = new PrefManager(this);
        boolean pressExit = pref.isExit();
        if(!pressExit){
            Toast.makeText(Refresh.this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            pref.pressExit(true);
            Intent launchNextActivity;
            launchNextActivity = new Intent(this,Refresh.class);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(launchNextActivity);
        }
        else{
            pref.pressExit(false);
            finish();
        }
    }
}
