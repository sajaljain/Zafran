package com.monkporter.zafran.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import com.google.firebase.crash.FirebaseCrash;
import com.monkporter.zafran.Interface.ApiInterface;
import com.monkporter.zafran.R;
import com.monkporter.zafran.adapter.ProductsAdapter;
import com.monkporter.zafran.generic.ShowLoader;
import com.monkporter.zafran.helper.PrefManager;
import com.monkporter.zafran.model.Banner;
import com.monkporter.zafran.model.Banners;
import com.monkporter.zafran.model.Products;
import com.monkporter.zafran.model.Product;

import com.monkporter.zafran.model.RecyclerItemClickListener;
import com.monkporter.zafran.pushnotification.MyFirebaseMessagingService;
import com.monkporter.zafran.rest.ApiClient;
import com.monkporter.zafran.utility.CommonMethod;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
* Initially user is not logged in
* */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getName();
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private SliderLayout sliderShow;
    private TextSliderView textSliderView;
    HashMap<String, String> url_maps;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerView recyclerView;

    boolean banner = false;
    boolean products = false;
    TextView toolbarAddress;
    String address = null;
    private boolean login;
    ProductsAdapter productsAdapter;
    Banners banners;
    private ViewGroup viewGroup;
    List<Product> productsList;
    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarAddress = (TextView) findViewById(R.id.toolbar_address_id);

        PrefManager prefManager = new PrefManager(MainActivity.this);
        address = prefManager.getUserCurrentLocation();


        if (address != null) {
            toolbarAddress.setText(address);
        } else {

            startActivity(new Intent(MainActivity.this, PlacesAutoCompleteActivity.class));
            //TODO: Place Default Latitude Longitude
        }
        //   getBanner();
        initSlider();
        //  getProductsList();
        initNavigationDrawer();
        setupToolbar();


        // LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // v = inflater.inflate(R.layout.actionbar_address_layout,null);

        viewGroup = (ViewGroup) findViewById(R.id.toolbar_address_layout_id);
        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlacesAutoCompleteActivity.class);
                startActivity(intent);
            }
        });
        if(banner==false || products==false) {
            ShowLoader.getInstance(MainActivity.this, "Loading...").run(true);
        }
        getBanner();

        getProductsList();


        recyclerView = (RecyclerView) findViewById(R.id.products);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        // List<Product> productsList = getProductsList();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, SingleProduct.class);
                Product products;
                products = productsAdapter.getItem(position);
                String teaImgId = products.getImageUrl();
                String teaName = products.getProductName();
                Bundle extras = new Bundle();
                extras.putString("TEA_IMAGE_ID", teaImgId);
                extras.putString("TEA_NAME", teaName);
                intent.putExtras(extras);
                startActivity(intent);
            }
        }));
    }

    private void initSlider() {

        sliderShow = (SliderLayout) findViewById(R.id.slider);
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();

        file_maps.put("The Social Entrepreneur", R.drawable.slider2);
        file_maps.put("The Big Boss", R.drawable.slider3);
        file_maps.put("The Innovator", R.drawable.slider4);
        for (String name : file_maps.keySet()) {
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                } else if (isShow) {
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

        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
        login = sharedPreferences.getBoolean(getString(R.string.LOGIN), false);
        if (!login) {

            menu.findItem(R.id.nav_account).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_address).setEnabled(false);
            menu.findItem(R.id.nav_pre_order).setEnabled(false);

        } else {
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

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (doubleBackToExitPressedOnce) {

            // Clean temporary order data after order complete
            CommonMethod.cleanDeviceData();

            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
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
            editor.putBoolean(getString(R.string.LOGIN), login);
            editor.commit();
        } else if (id == R.id.nav_logout) {
            menu.findItem(R.id.nav_account).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_address).setEnabled(false);
            menu.findItem(R.id.nav_pre_order).setEnabled(false);
            login = false;
            editor.putBoolean(getString(R.string.LOGIN), login);
            editor.commit();
        }
        if (id == R.id.nav_share) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Download ZAFRAN app at http://play.google.com/store/apps/details?id=com.grofers.customerapp ");
            startActivity(shareIntent);
        }

        if (id == R.id.nav_pre_order) {
            Intent intent = new Intent(this, OrderHistory.class);
            startActivity(intent);
        }

        if (id == R.id.nav_address) {
            Intent intent = new Intent(this, AddressDetail.class);
            startActivity(intent);
        }
        if (id == R.id.nav_about) {
            Intent intent = new Intent(this, SmsActivity.class);
            startActivity(intent);
        }            /*if (navigationView != null) {
                setupDrawerContent(nainitvigationView);
            }

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

  /*  @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }*/


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
            new MyFirebaseMessagingService().sendNotification("hello");
            Toast.makeText(this, "No notifications ", Toast.LENGTH_SHORT).show();
        }

        switch (id) {
            case android.R.id.home:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startRefreshActivity() {
        Intent refreshIntent = new Intent(MainActivity.this, Refresh.class);
        refreshIntent.putExtra("previousScreen", "splash");
        startActivityForResult(refreshIntent, 1001);
        overridePendingTransition(R.anim.start_activity_slide_in_left, R.anim.start_activity_slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {

            getBanner();
            getProductsList();

        }
    }

    public void getBanner() {
        //here we making asynchronous calls so we need to check for both the loader messages
        /*if (ShowLoader.getInstance(MainActivity.this, "Loading Tea's...").isShowing()) {


            ShowLoader.getInstance(MainActivity.this, "Loading Tea's...").dismis(true);

        }
        ShowLoader.getInstance(MainActivity.this, "Loading Banners...").run(true);
        */
        final HashMap<String, String> sliderBanner = new HashMap<>();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Banners> call = apiService.getBanners();
        FirebaseCrash.logcat(Log.INFO, TAG, "Request for Banner request");
        call.enqueue(new Callback<Banners>() {
            @Override
            public void onResponse(Call<Banners> call, Response<Banners> response) {
                try {
                    banner = true;
                    if(banner==true && products==true)
                    {
                        ShowLoader.getInstance(MainActivity.this, "Loading...").dismis(true);
                    }
                    FirebaseCrash.logcat(Log.INFO, TAG, "Response for Banner ");
                    banners = response.body();
                    if (banners != null) {

                        boolean error = banners.isError();
                        String message = banners.getMessage();

                        FirebaseCrash.logcat(Log.INFO, TAG, "" + error);
                        FirebaseCrash.logcat(Log.INFO, TAG, message);
                        if (error == false) {
                            List<Banner> listOfBanner = banners.getBanners();
                            for (Banner banner : listOfBanner) {
                                StringBuilder bannerHead = new StringBuilder();
                                bannerHead.append(banner.getBannerHead());
                                StringBuilder bannerURL = new StringBuilder();
                                bannerURL.append(banner.getBannerUrl());

                                sliderBanner.put(bannerHead.toString(), bannerURL.toString());
                                FirebaseCrash.logcat(Log.INFO, TAG, "Banner Head " + bannerHead.toString() + bannerURL.toString());
                            }
                            sliderShow.removeAllSliders();
                            for (String name : sliderBanner.keySet()) {
                                textSliderView = new TextSliderView(MainActivity.this);
                                // initialize a SliderLayout
                                textSliderView
                                        .description(name)
                                        .image(sliderBanner.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit);
                                sliderShow.addSlider(textSliderView);
                            }
                        } else {
                            //some error occurred from the database and server while fetching banners
                            FirebaseCrash.logcat(Log.INFO, TAG, "some error occurred from the database and server while fetching banners");
                            FirebaseCrash.report(new Exception("some error occurred from the database and server while fetching banners"));
                            Toast.makeText(MainActivity.this, "Oops!!! error in fetching banners", Toast.LENGTH_LONG).show();
                            startRefreshActivity();
                        }
                    }
                } catch (Exception e) {
                    //some network error occurred
                    FirebaseCrash.logcat(Log.INFO, TAG, e.getMessage());
                    FirebaseCrash.report(new Exception("Some exception occurred"));
                    Toast.makeText(MainActivity.this, "Oops!!! error in fetching banners", Toast.LENGTH_LONG).show();
                    startRefreshActivity();
                } finally {
                    //remove the show loader
                    if (ShowLoader.getInstance(MainActivity.this, "Loading ...").isShowing() ) {
                        ShowLoader.getInstance(MainActivity.this, "Loading ...").dismis(true);
                    }
                }

            }

            @Override
            public void onFailure(Call<Banners> call, Throwable t) {
                FirebaseCrash.logcat(Log.INFO, TAG, "Some n/w error in device ");
                FirebaseCrash.report(new Exception("Some n/w error in device"));
                Toast.makeText(MainActivity.this, "Oops!!! error in fetching banners", Toast.LENGTH_LONG).show();
                //here if you want you can
                startRefreshActivity();

            }
        });
    }

    public List<Product> getProductsList() {
        /*//Show Loader
        if (ShowLoader.getInstance(MainActivity.this, "Loading Banners...").isShowing()) {
            ShowLoader.getInstance(MainActivity.this, "Loading Banners...").dismis(true);

        }
        ShowLoader.getInstance(MainActivity.this, "Loading Tea's...").run(true);*/

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Products> call = apiService.getProducts();
        FirebaseCrash.logcat(Log.INFO, TAG, "Request for Products");
        try {
            FirebaseCrash.logcat(Log.INFO, TAG, "Response for Products");
            call.enqueue(new Callback<Products>() {
                @Override
                public void onResponse(Call<Products> call, Response<Products> response) {
                    banner = true;
                    if(banner==true && products==true)
                    {
                        ShowLoader.getInstance(MainActivity.this, "Loading...").dismis(true);
                    }
                    Products products = response.body();
                    if (products != null) {
                        FirebaseCrash.logcat(Log.INFO, TAG, "error = " + products.isError());
                        FirebaseCrash.logcat(Log.INFO, TAG, "message = " + products.getMessage());
                        if (products.isError() == false) {
                            FirebaseCrash.logcat(Log.INFO, TAG, "Successfully retrieved products list");
                            productsList = products.getProducts();
                            productsAdapter = new ProductsAdapter(MainActivity.this, productsList);
                            recyclerView.setAdapter(productsAdapter);
                            //ToDo if recycler view is empty.
                        } else {
                            FirebaseCrash.logcat(Log.INFO, TAG, "some error occurred from the database and server while fetching products");
                            FirebaseCrash.report(new Exception("some error occurred from the database and server while fetching products"));
                            Toast.makeText(MainActivity.this, "Oops!!! No products found in database", Toast.LENGTH_LONG).show();
                            startRefreshActivity();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Products> call, Throwable t) {
                    FirebaseCrash.logcat(Log.INFO, TAG, "Some n/w error in device "+ t.getMessage());
                    FirebaseCrash.report(new Exception("Some n/w error in device"));
                    Toast.makeText(MainActivity.this, "Oops!!! error in fetching products", Toast.LENGTH_LONG).show();
                    startRefreshActivity();
                }
            });

        } catch (Exception e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, e.getMessage());
            FirebaseCrash.report(new Exception("Some exception occurred"));
            Toast.makeText(MainActivity.this, "Oops!!! error in fetching products", Toast.LENGTH_LONG).show();
            startRefreshActivity();
        } finally {
            //remove the show loader
            if (ShowLoader.getInstance(MainActivity.this, "Loading ...").isShowing() ) {
                ShowLoader.getInstance(MainActivity.this, "Loading ...").dismis(true);
            }

        }
        return productsList;
    }

}
