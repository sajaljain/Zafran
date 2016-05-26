package com.monkporter.zafran.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PointerIconCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        initNavigationDrawer();
        initSlider();

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
        file_maps.put("The Developer",R.drawable.slider1);
        file_maps.put("The Social Entrepreneur",R.drawable.slider2);
        file_maps.put("The Big Boss",R.drawable.slider3);
        file_maps.put("The Innovator", R.drawable.slider4);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            sliderShow.addSlider(textSliderView);
        }


    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

    }

    private void initNavigationDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        setupActionBarDrawerToogle();
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }
    private void setupActionBarDrawerToogle() {
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
                /**
                 * Called when a drawer has settled in a completely closed state.
                 */
                public void onDrawerClosed(View view) {
                //Snackbar.make(view, R.string.navigation_drawer_close, Snackbar.LENGTH_SHORT).show();
            }

                /**
                 * Called when a drawer has settled in a completely open state.
                 */
            public void onDrawerOpened(View drawerView) {
                //Snackbar.make(drawerView, R.string.navigation_drawer_open, Snackbar.LENGTH_SHORT).show();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    public void setupDrawerContent(NavigationView upDrawerContent) {
        Menu menu = navigationView.getMenu();
        //If user not logged in
        if(false) {

            menu.findItem(R.id.nav_account).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(false);

        }
        //else logged in
        else
        {
            menu.findItem(R.id.nav_account).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(true);
            menu.findItem(R.id.nav_address).setEnabled(true);
            menu.findItem(R.id.nav_pre_order).setEnabled(true);
        }
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


        int id = item.getItemId();
        if (id == R.id.nav_account) {
            Toast.makeText(this,"Login",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_notification) {
            /*Toast.makeText(this,"Notification",Toast.LENGTH_SHORT).show();
            TextView txt = (TextView) findViewById(R.id.counter);
            txt.setText("2");*/

            /*if (navigationView != null) {
                setupDrawerContent(nainitvigationView);
            }
*/
        }

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


        return listViewItems;
    }


}
