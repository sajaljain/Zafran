package com.monkporter.zafran.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.monkporter.zafran.R;
import com.monkporter.zafran.adapter.ProductsAdapter;
import com.monkporter.zafran.model.Products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

/*
* Initially user is not logged in
*
*
* */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private SliderLayout sliderShow;
    private TextSliderView textSliderView;
    HashMap<String,String> url_maps ;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerView recyclerView;
    View v;
    private boolean login;
    private ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigationDrawer();
        setupToolbar();
        initSlider();

       // LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // v = inflater.inflate(R.layout.actionbar_address_layout,null);

        viewGroup = (ViewGroup) findViewById(R.id.toolbar_address_layout_id);
        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,PlacesAutoCompleteActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.products);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        List<Products> productsList = getListItemData();
        ProductsAdapter productsAdapter = new ProductsAdapter(MainActivity.this, productsList);
        recyclerView.setAdapter(productsAdapter);
    }
    private void initSlider() {
        sliderShow = (SliderLayout) findViewById(R.id.slider);
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();

        file_maps.put("The Social Entrepreneur",R.drawable.slider2);
        file_maps.put("The Big Boss",R.drawable.slider3);
        file_maps.put("The Innovator", R.drawable.slider4);
        for(String name : file_maps.keySet()){
            textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            sliderShow.addSlider(textSliderView);
        }
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();

        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayShowCustomEnabled(true);
       // ab.setCustomView(v);
        ab.setTitle("");

        ab.setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("");
                    viewGroup.setVisibility(View.VISIBLE);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle("");
                    viewGroup.setVisibility(View.GONE);
                    isShow = false;
                }
            }
        });


    }

    private void initNavigationDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }




    public void setupDrawerContent(NavigationView upDrawerContent) {
        Menu menu = navigationView.getMenu();
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        login = sharedPreferences.getBoolean(getString(R.string.LOGIN),false);
        if(!login) {

            menu.findItem(R.id.nav_account).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_address).setEnabled(false);
            menu.findItem(R.id.nav_pre_order).setEnabled(false);

        }
        else
        {
        menu.findItem(R.id.nav_account).setVisible(false);
        menu.findItem(R.id.nav_logout).setVisible(true);
        menu.findItem(R.id.nav_address).setEnabled(true);
        menu.findItem(R.id.nav_pre_order).setEnabled(true);
        }

       /* SpannableStringBuilder text = new SpannableStringBuilder();
        text.append("address");

        text.setSpan(new ForegroundColorSpan(Color.LTGRAY),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        menu.findItem(R.id.nav_address).setTitle(text);*/

        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Menu menu = navigationView.getMenu();
        int id = item.getItemId();
        if (id == R.id.nav_account) {
            item.setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(true);
            menu.findItem(R.id.nav_address).setEnabled(true);
            menu.findItem(R.id.nav_pre_order).setEnabled(true);
            login = true;
            editor.putBoolean(getString(R.string.LOGIN),login);
            editor.commit();
        } else if (id == R.id.nav_logout) {
            menu.findItem(R.id.nav_account).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_address).setEnabled(false);
            menu.findItem(R.id.nav_pre_order).setEnabled(false);
            login = false;
            editor.putBoolean(getString(R.string.LOGIN),login);
            editor.commit();
        }
        if(id == R.id.nav_share){


            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Download ZAFRAN app at http://play.google.com/store/apps/details?id=com.grofers.customerapp ");
            startActivity(shareIntent);
        }

        if(id == R.id.nav_pre_order){
            Intent intent = new Intent(this,OrderHistory.class);
            startActivity(intent);
        }

        if(id == R.id.nav_address){
            Intent intent = new Intent(this,AddressDetail.class);
            startActivity(intent);
        }
            /*if (navigationView != null) {
                setupDrawerContent(nainitvigationView);
            }

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }
    private List<Products> getListItemData(){
        List<Products> listViewItems = new ArrayList<Products>();
        listViewItems.add(new Products("Assam Tea","Some Description", R.drawable.assam));
        listViewItems.add(new Products("Cardamom Tea","Some Description", R.drawable.cardamom_tea));

        listViewItems.add(new Products("Masala Chai","Some Description", R.drawable.masala));
        listViewItems.add(new Products("Ginger Tea","Some Description", R.drawable.ginger));
        listViewItems.add(new Products("Assam Tea","Some Description", R.drawable.assam));
        listViewItems.add(new Products("Cardamom Tea","Some Description", R.drawable.cardamom_tea));

        return listViewItems;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.add_button_id);
        menuItem.setVisible(false);
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
            Toast.makeText(this,"No notifications ",Toast.LENGTH_SHORT).show();
        }

        switch (id){
            case android.R.
id.home:
                if (drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
