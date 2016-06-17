package com.monkporter.zafran.activity;

/**
 * Created by Vaibhav on 6/15/2016.
 */

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.monkporter.zafran.R;
import com.monkporter.zafran.adapter.ItemListAdapter;
import com.monkporter.zafran.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemListMainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ItemListAdapter adapter;
    int product_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        SharedPreferences sharedPreferences = OrderItemListMainActivity.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        product_set = sharedPreferences.getInt(getString(R.string.PRODUCT_SET),0);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(" ");
        ImageSpan i = new ImageSpan(OrderItemListMainActivity.this, R.drawable.ic_add_shopping_cart_black_18dp);
        builder.setSpan(i,0,builder.length(), 0);
        builder.append(" Rs "+product_set*10);
        Snackbar snackbar = Snackbar.make(recyclerView, builder, Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.RED);
        snackbar.show();

        recyclerView.setNestedScrollingEnabled(false);
        adapter = new ItemListAdapter(OrderItemListMainActivity.this,getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderItemListMainActivity.this));


    }
    public List<OrderItem> getData() {
        List<OrderItem> data = new ArrayList<>();
        int[] icons = {R.drawable.n1064,R.drawable.meeting64,R.drawable.n264,R.drawable.n164};
        int[] titles = {100,50,20,10};
        int[] details = {10,5,2,1};
        for( int i = 0;i < titles.length && i< icons.length;i++){
            OrderItem current = new OrderItem();
            current.photo = icons[i];
            current.price = titles[i];
            current.setOFCups = details[i];

            data.add(current);
        }
        return data;    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

