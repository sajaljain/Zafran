package com.monkporter.zafran.activity;

import android.app.ProgressDialog;
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
import com.monkporter.zafran.Interface.BannerRequest;
import com.monkporter.zafran.Interface.GetProductRequest;
import com.monkporter.zafran.R;
import com.monkporter.zafran.adapter.ProductsAdapter;
import com.monkporter.zafran.helper.PrefManager;
import com.monkporter.zafran.model.Banner;
import com.monkporter.zafran.model.GetBanner;
import com.monkporter.zafran.model.GetProducts;
import com.monkporter.zafran.model.Product;
import com.monkporter.zafran.model.Products;
import com.monkporter.zafran.model.RecyclerItemClickListener;
import com.monkporter.zafran.rest.BannerApiClient;
import com.monkporter.zafran.rest.ProductApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
* Initially user is not logged in
*
*
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
    HashMap<String,String> url_maps ;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerView recyclerView;
    View v;
    ProgressDialog progressDialog;
    TextView toolbarAddress;
    String address = null;
    private boolean login;
    ProductsAdapter productsAdapter;
    GetBanner getBanner;
    private ViewGroup viewGroup;
    List<Product> productsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        toolbarAddress = (TextView) findViewById(R.id.toolbar_address_id);
        PrefManager prefManager = new PrefManager(MainActivity.this);
        address = prefManager.getUserCurrentLocation();

        if(address != null){
            toolbarAddress.setText(address);
        }
        else{

            startActivity(new Intent(MainActivity.this,PlacesAutoCompleteActivity.class));
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
                Intent intent = new Intent(MainActivity.this,PlacesAutoCompleteActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.products);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
       // List<Product> productsList = getProductsList();

        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this,OrderItemListMainActivity.class);
                Product products;
                products = productsAdapter.getItem(position);
                String teaImgId = products.getImageUrl();
                String teaName = products.getProductName();
                Bundle extras = new Bundle();
                extras.putString("TEA_IMAGE_ID",teaImgId);
                extras.putString("TEA_NAME",teaName);
                intent.putExtras(extras);
                startActivity(intent);
            }
        }));
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

    public HashMap getBanner() {

        final HashMap<String, String> sliderBanner = new HashMap<>();
        BannerRequest bannerRequest = BannerApiClient.getClient().create(BannerRequest.class);
        Call<GetBanner> call = bannerRequest.getResponse();
        call.enqueue(new Callback<GetBanner>() {
            @Override
            public void onResponse(Call<GetBanner> call, Response<GetBanner> response) {
                int status = response.code();
                getBanner = response.body();
                if(getBanner != null) {
                    boolean error = getBanner.isError();
                    String message = getBanner.getMessage();
                    Log.d("Get Banner", "message =" + message);
                    Log.d("Get Banner", "error =" + error);

                    int size = getBanner.getBanners().size();
                 //   Toast.makeText(MainActivity.this, "" + size, Toast.LENGTH_SHORT).show();
                    for (Banner banner : getBanner.getBanners()) {

                        sliderBanner.put(banner.getBannerHead(), banner.getBannerUrl());
                        Log.d("Baner url", "url =" + banner.getBannerHead());
                    }
                    sliderShow.removeAllSliders();
                    for (String name : sliderBanner.keySet()) {
                        textSliderView = new TextSliderView(MainActivity.this);
                        // initialize a SliderLayout
                        textSliderView
                                .description(name)
                                .image(sliderBanner.get(name))
                                .setScaleType(BaseSliderView.ScaleType.Fit);
                        // Toast.makeText(MainActivity.this, sliderBanner.get(name), Toast.LENGTH_SHORT).show();
                        sliderShow.addSlider(textSliderView);
                    }

                }

            }

            @Override
            public void onFailure(Call<GetBanner> call, Throwable t) {
                Log.d("Get Banner", "onFailure =" + t.getMessage());
                startActivity(new Intent(MainActivity.this,Refresh.class));
            }
        });

        return sliderBanner;
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
    @Override
    public void onResume(){
        super.onResume();
        if(!progressDialog.isShowing()) {
            progressDialog.setMessage("Fetching Products...");
            progressDialog.show();
        }
        Log.d(TAG,"Resume");
        sliderShow.startAutoCycle();
        getBanner();
        getProductsList();
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
        if(id == R.id.nav_about) {
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

    public List<Product> getProductsList() {
        GetProductRequest productRequest = ProductApiClient.getClient().create(GetProductRequest.class);
        Call<GetProducts> call = productRequest.getResponse();
        call.enqueue(new Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts> response) {
                int status = response.code();
                GetProducts getProducts = response.body();
                if(getProducts != null){
                    Log.d("Product Response","message ="+getProducts.getMessage());
                    Log.d("Product Response","error ="+getProducts.isError());
                    productsList = getProducts.getProducts();
                }
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
                productsAdapter = new ProductsAdapter(MainActivity.this, productsList);
                recyclerView.setAdapter(productsAdapter);

            }

            @Override
            public void onFailure(Call<GetProducts> call, Throwable t) {
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
                startActivity(new Intent(MainActivity.this,Refresh.class));
            }
        });

        return productsList;
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
