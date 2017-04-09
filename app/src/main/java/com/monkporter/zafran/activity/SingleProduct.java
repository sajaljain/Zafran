package com.monkporter.zafran.activity;

/**
 * Created by Vaibhav on 6/15/2016.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.monkporter.zafran.R;
import com.monkporter.zafran.adapter.ItemListAdapter;
import com.monkporter.zafran.model.OrderItem;
import com.monkporter.zafran.utility.CommonMethod;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SingleProduct extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ItemListAdapter adapter;
    int product_set;
    private TextView cartPrice,cartQuantity;
    private View cartBar;
    String teaName;
    String teaImageId;
    private ShareActionProvider mShareActionProvider;
    LinearLayout checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        teaName = extras.getString("TEA_NAME");
        teaImageId = extras.getString("TEA_IMAGE_ID");
        toolbar.setTitle(teaName);
        ImageView toolbarImage = (ImageView) findViewById(R.id.backdrop);
        Picasso.with(SingleProduct.this).load(teaImageId).into(toolbarImage);
        //toolbarImage.setImageResource(teaImageId);
       final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(teaName);

        cartPrice = (TextView) findViewById(R.id.cart_price);
        cartQuantity = (TextView) findViewById(R.id.cart_quantity);
        //if q =0
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        SharedPreferences sharedPreferences = SingleProduct.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        product_set = sharedPreferences.getInt(getString(R.string.PRODUCT_SET),0);
        cartPrice.setText("Rs."+product_set*10);
        cartQuantity.setText(""+product_set);

        cartBar = findViewById(R.id.cart_bar);
        cartBar.setVisibility(View.GONE);

        recyclerView.setNestedScrollingEnabled(false);
        adapter = new ItemListAdapter(SingleProduct.this,getData(),cartPrice,cartQuantity,cartBar);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SingleProduct.this));
        checkout = (LinearLayout) findViewById(R.id.cart_bar);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SingleProduct.this,SmsActivity.class));
            }
        });
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
            current.set = details[i];

            data.add(current);
        }
        return data;    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   getMenuInflater().inflate(R.menu.menu_scrolling, menu);
     /*   MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"texxxxxt");
        mShareActionProvider.setShareIntent(shareIntent);*/
        return true;
    }

    public void onBackPressed(View v) {
            super.onBackPressed();
            SingleProduct.this.finish();
            return;
        }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
    //    if (id == R.id.action_settings) {
      //      return true;
        //}
        return super.onOptionsItemSelected(item);
    }
}

