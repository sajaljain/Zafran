package com.monkporter.zafran.helper;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import com.monkporter.zafran.R;

import java.util.Calendar;

/**
 * Created by Vaibhav on 7/26/2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
  private String time;
    TextView timePick;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        timePick = (TextView) getActivity().findViewById(R.id.pick_time_id);
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(),R.style.MyTimePickerDialogTheme, this, hour, minute,
                false);
    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String min = "";
        if(minute < 10)
            min = "0"+minute;
        else
        min = ""+minute;
        if (hourOfDay > 12) {
            hourOfDay -= 12;
             time = hourOfDay+":"+min+" PM";
        }
        else {
            time = hourOfDay + ":" + min+" AM";
        }
       timePick.setText(time);
    }


  /*  public String getTime() {
        return time;
    }*/

}
