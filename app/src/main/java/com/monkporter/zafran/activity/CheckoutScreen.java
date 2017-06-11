package com.monkporter.zafran.activity;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.monkporter.zafran.R;
import com.monkporter.zafran.helper.TimePickerFragment;

import java.util.Calendar;

public class CheckoutScreen extends AppCompatActivity {
    private RelativeLayout timePicker;
    TextView timePick;
    private String time;
    static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.CheckOut_Toolbar);
        setSupportActionBar(toolbar);

        timePick = (TextView)findViewById(R.id.pick_time_id);
      /*  final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        if(hour > 12) {
            hour -= 12;
            time = hour + ":" + minute+" PM";
        }
        else{
            time = hour + ":" + minute+" AM";
        }
        timePick.setText(time);*/
        showTimePickerDialog();
    }

    private void showTimePickerDialog() {
        timePicker = (RelativeLayout) findViewById(R.id.time_picker_layout_id);
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePickerDialog = new TimePickerFragment();
                timePickerDialog.show(getFragmentManager(), "timePicker");
            }
        });
    }
    public void onResume(){
        super.onResume();

    }
}
