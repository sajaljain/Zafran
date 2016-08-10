package com.monkporter.zafran.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.monkporter.zafran.Interfece.AddressSendRequest;
import com.monkporter.zafran.R;
import com.monkporter.zafran.adapter.PlacesAutoCompleteAdapter;
import com.monkporter.zafran.adapter.SelectedPlacesAdapter;
import com.monkporter.zafran.helper.PrefManager;
import com.monkporter.zafran.model.Constants;
import com.monkporter.zafran.model.RecyclerItemClickListener;
import com.monkporter.zafran.model.UserLocation;
import com.monkporter.zafran.model.UserLocationResponse;
import com.monkporter.zafran.rest.UserLocationApiClient;
import com.monkporter.zafran.utility.CommonMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesAutoCompleteActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final String TAG = "PLaces";
    AddressSendRequest addressSendRequest;
    protected GoogleApiClient mGoogleApiClient;
    private String[] locationPermission = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION};
    private final int LOCATION_PERMISSIONS_REQUEST = 101;
    private List<Address> addressList;
    private Location mLastLocation;
    TextView  toolbarAddress;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(19.5937, 77.9629), new LatLng(21.5937, 79.9629));

    private EditText mAutocompleteView;
    private RecyclerView mRecyclerView, recyclerView;
    private LinearLayoutManager mLinearLayoutManager, linearLayoutManager;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    ImageView delete;
    private SelectedPlacesAdapter selectedPlaceAdapter;
    private CardView cardView;
    private LinearLayout notOperatableLayout;
    private String mArea = "", mCity = "", mCompleteAddress = "",mLatitude = "",mLongitude = "",mPlaceId = "";
    UserLocation userLocation = null;
    PrefManager prefManager;
    ArrayList<String> mResultList;
    private int cityId;
    private int areaId;

    ProgressDialog progressDialog;
    Button selectAnotherLocation;
    public PlacesAutoCompleteActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        buildGoogleApiClient();
        cardView = (CardView) findViewById(R.id.current_location_id);
        cardView.setOnClickListener(this);
        mAutocompleteView = (EditText) findViewById(R.id.autocomplete_places);
        delete = (ImageView) findViewById(R.id.cross);
        notOperatableLayout = (LinearLayout) findViewById(R.id.layout_non_operatable_city);
        selectAnotherLocation = (Button) findViewById(R.id.btn_select_another_location);
        selectAnotherLocation.setOnClickListener(this);
        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, R.layout.searchview_adapter,
                mGoogleApiClient, BOUNDS_INDIA, null);
progressDialog = new ProgressDialog(this);
       prefManager = new PrefManager(PlacesAutoCompleteActivity.this);
        mResultList = prefManager.getSaveLocations();
        if(mResultList == null){
            mResultList = new ArrayList<>();
        }

        selectedPlaceAdapter = new SelectedPlacesAdapter(this,mResultList);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewID);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(selectedPlaceAdapter);



        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                mResultList.remove(viewHolder.getAdapterPosition());
                prefManager.saveLocations(mResultList);
                selectedPlaceAdapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);
        delete.setOnClickListener(this);
        mAutocompleteView.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                    delete.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                } else if (s.toString().equals("")) {
                    mAutoCompleteAdapter.clear();
                    delete.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else if (!mGoogleApiClient.isConnected()) {
                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e(Constants.PlacesTag, Constants.API_NOT_CONNECTED);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        Log.i("TAG", "Autocomplete item selected: " + item.description);
                        selectedPlaceAdapter.insertItem((String) item.description);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getCount() == 1) {
                                    //Do the things here on Click.....
                                    Toast.makeText(getApplicationContext(), String.valueOf(places.get(0).getLatLng()), Toast.LENGTH_SHORT).show();
                                   if(mLatitude.equals("") && mLongitude.equals("")){
                                        String loc= String.valueOf(places.get(0).getLatLng());
                                       getLAtLong(loc);
                                }
                                } else {
                                    Toast.makeText(getApplicationContext(), Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Clicked: " + item.area);
                        Log.i("TAG", "Clicked: " + item.city);

                        if(mCity.equals("") && mArea.equals("") && mCompleteAddress.equals("") && mPlaceId.equals("")){
                            mCity = getCityFromAddress((String) item.description);
                            mArea = getAreaFromAddress((String) item.description);
                            mCompleteAddress = (String) item.description;
                            mPlaceId = (String)item.placeId;

                            Log.i("UserLocation", "City: " + mCity);
                            Log.i("UserLocation", "Area: " + mArea);
                            Log.i("UserLocation", "des: " + mCompleteAddress);



                                userLocation = new UserLocation();
                                userLocation.setArea(mArea);
                                userLocation.setCity(mCity);
                            sendUserLocationRequest(userLocation);

                        }
                        else{

                            Log.i("TAG", String.format("Area =  '%s' & City = '%s' & Address = '%s' ",
                                    mArea,mCity,mCompleteAddress));
                        }

                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);

                    }
                })
        );

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final String address = selectedPlaceAdapter.getItem(position);
                if(mCity.equals("") && mArea.equals("") && mCompleteAddress.equals("")){
                    mCity = getCityFromAddress(address);
                    mArea = getAreaFromAddress(address);
                    mCompleteAddress = address;


                    Log.i("UserLocation", "City: " + mCity);
                    Log.i("UserLocation", "Area: " + mArea);
                    Log.i("UserLocation", "des: " + mCompleteAddress);



                    userLocation = new UserLocation();
                    userLocation.setArea(mArea);
                    userLocation.setCity(mCity);
                    sendUserLocationRequest(userLocation);
                }
                else{

                    Log.i("TAG", String.format("Area =  '%s' & City = '%s' & Address = '%s' ",
                            mArea,mCity,mCompleteAddress));
                }

                //Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);

            }
        }));


    }

    private void getLAtLong(String loc) {
        String s[] = loc.split(",",0);

        for(String s1:s){
            if(mLatitude == "")
                mLatitude = s1.replaceAll("[^ 0-9 .]","");
            else
                mLongitude = s1.replaceAll("[^ 0-9 .]","");
            // Log.d("lat",s1);
        }
        Log.d("lat ="+mLatitude,"long ="+mLongitude);

    }

    private void sendUserLocationRequest(final UserLocation userLocation) {
        if(!progressDialog.isShowing()) {
            progressDialog.setMessage("fetching location...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        addressSendRequest = UserLocationApiClient.getClient().create(AddressSendRequest.class);
        Call<UserLocationResponse> call = addressSendRequest.getResponseMessage(userLocation);
        call.enqueue(new Callback<UserLocationResponse>() {
            @Override
            public void onResponse(Call<UserLocationResponse> call, Response<UserLocationResponse> response) {
                int statuscode = response.code();
                UserLocationResponse userLocationResponse = response.body();
                Log.d("UserLocation","response status ="+userLocationResponse.getAreaId());
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                 cityId = userLocationResponse.getCityId();
                 areaId = userLocationResponse.getAreaId();
                Log.d("AreaID ="+areaId,"CityID ="+cityId);


                if(areaId == 1 && cityId == 1) {
                    String add = mArea + "," + mCity;
                    PrefManager prefManager = new PrefManager(PlacesAutoCompleteActivity.this);
                    prefManager.setUserCurrentLocation(add);
                    Intent intent = new Intent(PlacesAutoCompleteActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else if(areaId == -1 && cityId == 1){
                    mRecyclerView.setVisibility(View.GONE);
                    CommonMethod.showAlert("We are not operational in this Area. Please select another Area...",PlacesAutoCompleteActivity.this);
                    mArea = mCity = mCompleteAddress = mLatitude = mLongitude = mPlaceId = "";
                }
                else{
                    mRecyclerView.setVisibility(View.GONE);
                    notOperatableLayout.setVisibility(View.VISIBLE);
                    mArea = mCity = mCompleteAddress = mLatitude = mLongitude = mPlaceId = "";
                }
            }

            @Override
            public void onFailure(Call<UserLocationResponse> call, Throwable t) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.d("UserLocation","onFailure ="+t.getMessage());
                startActivity(new Intent(PlacesAutoCompleteActivity.this,Refresh.class));

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    protected synchronized void buildGoogleApiClient() {
        Log.v("Google API", "api connected");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("Google API Callback", "Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        Toast.makeText(this, Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v == delete) {
            mAutocompleteView.setText("");
        }
        if (v == cardView) {
            onCurrentLocationClick();
        }
        if (v == selectAnotherLocation){
            changeLocation();
        }
    }

    private void changeLocation() {
        notOperatableLayout.setVisibility(View.GONE);
    }


    private void onCurrentLocationClick() {
        if(Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(PlacesAutoCompleteActivity.this, "Requesting Permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(PlacesAutoCompleteActivity.this, locationPermission, LOCATION_PERMISSIONS_REQUEST);
            } else {
                Toast.makeText(PlacesAutoCompleteActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
                showMyLocation();
            }
        }
        else{
            showMyLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == LOCATION_PERMISSIONS_REQUEST) {
            Log.i(TAG, "Received response for permissions request.");
            Toast.makeText(PlacesAutoCompleteActivity.this, "Received response for permissions request.", Toast.LENGTH_SHORT).show();

            // We have requested multiple permissions for contacts, so all of them need to be
            // checked.
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // All required permissions have been granted, display contacts fragment.
                Toast.makeText(PlacesAutoCompleteActivity.this, "Permissions were granted.", Toast.LENGTH_SHORT).show();
                showMyLocation();
            } else {
                Log.i(TAG, "Permissions were NOT granted.");
                Toast.makeText(PlacesAutoCompleteActivity.this, "Permissions were NOT granted.", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }





    private void showMyLocation() {
        if(!progressDialog.isShowing()) {
            progressDialog.setMessage("fetching location...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(PlacesAutoCompleteActivity.this, "No Permission", Toast.LENGTH_SHORT).show();
                return;
            }

            if (CommonMethod.isNetworkAvailable(PlacesAutoCompleteActivity.this)) {
                PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                        .getCurrentPlace(mGoogleApiClient, null);


                result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                    @Override
                    public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                        if (likelyPlaces.getCount() > 0) {
                            if (mCity.equals("") && mArea.equals("") && mCompleteAddress.equals("")) {
                                mCity = getCityFromAddress((String) likelyPlaces.get(0).getPlace().getAddress());
                                mArea = getAreaFromAddress((String) likelyPlaces.get(0).getPlace().getAddress());
                                mCompleteAddress = (String) likelyPlaces.get(0).getPlace().getAddress();
                                mPlaceId =  likelyPlaces.get(0).getPlace().getId();
                                String loc = String.valueOf(likelyPlaces.get(0).getPlace().getLatLng());
                                getLAtLong(loc);
                                selectedPlaceAdapter.insertItem(mCompleteAddress);

                                userLocation = new UserLocation();
                                userLocation.setArea(mArea);
                                userLocation.setCity(mCity);
                                userLocation.setLatitude(mLatitude);
                                userLocation.setLongitude(mLongitude);
                                userLocation.setPlaceId(mPlaceId);
                                userLocation.setSearchString(mCompleteAddress);
                                sendUserLocationRequest(userLocation);
                                Log.i("TAG", String.format("Area =  '%s' & City = '%s' & Address = '%s' ",
                                        mArea, mCity, mCompleteAddress));
                            } else {

                                Log.i("TAG", String.format("Area =  '%s' & City = '%s' & Address = '%s' ",
                                        mArea, mCity, mCompleteAddress));
                            }

                            Log.i("TAG", String.format("Place '%s' is",
                                    likelyPlaces.get(0).getPlace().getAddress()));

                            likelyPlaces.release();
                        }
                        else{
                           // CommonMethod.showAlert("It seems that you are not connected to Internet.Please check your Internet Connection and then continue.",PlacesAutoCompleteActivity.this);
                            startActivity(new Intent(PlacesAutoCompleteActivity.this,Refresh.class));
                        }
                    }
               });
            } else {
                CommonMethod.showAlert("It seems that you are not connected to Internet.Please check your Internet Connection and then continue.",PlacesAutoCompleteActivity.this);
            }
        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            Log.v("Google API", "Connecting");
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            Log.v("Google API", "Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void onStart() {
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            Log.v("Google API", "api connected");
            mGoogleApiClient.connect();
            super.onStart();
        }
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    private String getCityFromAddress(String address) {
        String city = "";
        String[] separated = address.split("\\,");
        for (int i = 0; i < separated.length; i++) {
            Log.i("FetchLocation", "separated=" + separated[i]);
        }
        if (separated != null && separated.length > 2) {
            city = separated[separated.length - 3];
        }
        return city;
    }

    private String getAreaFromAddress(String address) {
        String area = "";
        String[] separated = address.split("\\,");
        if (separated != null && separated.length > 4) {
            area = separated[separated.length - 5];
            area = area + "," + separated[separated.length - 4];
        } else if (separated != null && separated.length > 3) {
            area = separated[separated.length - 4];
        }

        return area;
    }


}

