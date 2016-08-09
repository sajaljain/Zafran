package com.monkporter.zafran.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.monkporter.zafran.R;
import com.monkporter.zafran.model.AddressDetailAttributes;

public class ThankYouScreen extends AppCompatActivity {

    private Dialog rankDialog;
    private RatingBar ratingBar;
    public float rating;
    float f = (float)1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_id);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        menu.findItem(R.id.action_notification).setVisible(false);
        menu.findItem(R.id.add_button_id).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void rateTea(View v){
        rankDialog = new Dialog(ThankYouScreen.this, R.style.FullHeightDialog);
       rankDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        rankDialog.setContentView(R.layout.rate_tea_layout);
        rankDialog.setCancelable(false);
        ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(ratingBar.getRating());

        final TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
       // text.setText(""+ratingBar.getNumStars());

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                if(ratingBar.getRating() < 1)
                    ratingBar.setRating(f);
                if(ratingBar.getRating() == 1)
                    text.setText("Fine");
                else if(ratingBar.getRating() == 2)
                    text.setText("Nice");
                else if(ratingBar.getRating() == 3)
                    text.setText("Good");
                else if(ratingBar.getRating() == 4)
                    text.setText("Excellent");
                else
                    text.setText("Awesome");
            }
        });
        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankDialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it
        rankDialog.show();
    }
    public void sendRating(View v){

    }

}
