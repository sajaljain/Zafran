package com.monkporter.zafran.activity;

/**
 * Created by Sajal Jain.
 */

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;


import com.monkporter.zafran.R;
import com.monkporter.zafran.adapter.PlaceAutocompleteAdapter;
import com.monkporter.zafran.model.UserAddress;
import com.monkporter.zafran.utility.CommonMethod;


import java.util.List;
import java.util.Locale;

public class FetchLocationActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener{

    protected GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Toolbar toolbar;
    private PlaceAutocompleteAdapter mAdapter;
    private LinearLayout myLocationLayout, layoutNonOpCity, layoutEnterArea;
    private Button btnSelAnotherLoc;
    private TextView txtSearchUnderline;
    private ImageButton clearButton;
    private AutoCompleteTextView mAutocompleteView;
    private List<Address> addressList;
    private String previousActivity;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(19.5937, 77.9629), new LatLng(21.5937, 79.9629));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();



        setContentView(R.layout.activity_fetch_location);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete_places);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        mAutocompleteView.setAdapter(mAdapter);

        // Set up the 'clear text' button that clears the text in the autocomplete view
        clearButton = (ImageButton) findViewById(R.id.button_clear);
        clearButton.setOnClickListener(this);

        btnSelAnotherLoc = (Button) findViewById(R.id.btn_select_another_location);
        btnSelAnotherLoc.setOnClickListener(this);

        myLocationLayout = (LinearLayout) findViewById(R.id.my_location_lin);
       // layoutNonOpCity = (LinearLayout) findViewById(R.id.layout_non_operatable_city);
        layoutEnterArea = (LinearLayout) findViewById(R.id.layout_enter_area);
        myLocationLayout.setOnClickListener(this);

        txtSearchUnderline = (TextView) findViewById(R.id.txt_search_underline);
        String htmlString = "<u>Tips to select Area</u>";
        txtSearchUnderline.setText(Html.fromHtml(htmlString));
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            if (mAdapter != null && mAdapter.getCount() > position) {
                final AutocompletePrediction item = mAdapter.getItem(position);
                if (item != null && !item.toString().equalsIgnoreCase("")) {
                    final String placeId = item.getPlaceId();
                    final CharSequence primaryText = item.getPrimaryText(null);

                    /*
                     Issue a request to the Places Geo Data API to retrieve a Place object with additional
                     details about the place.
                      */
                    PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                            .getPlaceById(mGoogleApiClient, placeId);
                    placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
                }
            }
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            try {
                if (!places.getStatus().isSuccess()) {
                    // Request did not complete successfully
                    places.release();
                    return;
                }
                // Get the Place object from the buffer.
                final Place place = places.get(0);
                // Format details of the place for display and show it in a TextView.
                String address = place.getAddress().toString();
                if (address != null && address.length() > 0) {
                    String area = getAreaFromAddress(address);
                    String city = getCityFromAddress(address);
                    Log.i("FetchLocation", "Area=" + area + " City=" + city);
                    if (area != null && !area.equalsIgnoreCase("")) {
                        getAddress(address);
                    } else {
                        CommonMethod.showAlert("Please search [Area, City] eg. \n" +
                                "'Sector 62, Noida',\n'Laxmi Nagar, New Delhi'", FetchLocationActivity.this);
                    }

                }
                places.release();
            } catch (Exception e) {
                e.printStackTrace();

            }


        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_location_lin:
                onCurrentLocationClick();
                break;
            case R.id.button_clear:
                mAutocompleteView.setText("");
                break;
            case R.id.btn_select_another_location:
                layoutNonOpCity.setVisibility(View.GONE);
                myLocationLayout.setVisibility(View.VISIBLE);
                layoutEnterArea.setVisibility(View.VISIBLE);
        }
    }

    private void onCurrentLocationClick() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            if (addressList != null && addressList.size() > 0) {
                String currentAddress = "";
                Address address = addressList.get(0);
                if (address != null && address.getMaxAddressLineIndex() > 0) {
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        if (i == address.getMaxAddressLineIndex()) {
                            currentAddress = currentAddress + addressList.get(0).getAddressLine(i);
                        } else {
                            currentAddress = currentAddress + addressList.get(0).getAddressLine(i) + ",";
                        }
                    }
                }
                Log.i("FetchLocation", "addressList=" + address);
                getAddress(currentAddress);
            } else {
                CommonMethod.showAlert("Unable to fetch your current location.Please type your location and select.", FetchLocationActivity.this);
            }
        }
    }

    private void getAddress(String address) {
//        Getting address from

        /*int userId = -1;
        if (WelNus.getInstance() != null && WelNus.getInstance().getMyAccount() != null) {
            userId = WelNus.getInstance().getMyAccount().getUserID();
        } else {
            MyAccountDBHelper myAccountDBHelper = new MyAccountDBHelper(this);
            myAccountDBHelper.open();
            MyAccount myAccount = myAccountDBHelper.getMyAccountDetail();
            myAccountDBHelper.close();

            if (myAccount != null) {
                userId = myAccount.getUserID();
            }*/
    }


    @Override
    public void onBackPressed() {


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
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.i("Latitude", "Latitude=" + mLastLocation.getLatitude());
            Log.i("Longitude", "Longitude=" + mLastLocation.getLongitude());

            Geocoder gcd = new Geocoder(FetchLocationActivity.this, Locale.getDefault());

            try {
                addressList = gcd.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                Log.e("GeoCoder", "" + addressList.get(0).getLocality());
                if (addressList.size() > 0)
                    System.out.println(addressList.get(0).getLocality());

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


 /*   @Override
    public void setAPIResponse(Object obj, byte type) {
        switch (type) {
            case Constants.GET_ADDRESS_INFO:
                if (obj != null) {

                    UserAddress userAddress = (UserAddress) obj;
                    if (userAddress != null) {
                        if (userAddress.getCityId() != -1) {

                            int userId = -1;
                            if (WelNus.getInstance() != null && WelNus.getInstance().getMyAccount() != null) {
                                userId = WelNus.getInstance().getMyAccount().getUserID();
                            }
                            userAddress.setUserId(userId);
                            MyAddressDBHelper myAddressDBHelper = new MyAddressDBHelper(FetchLocationActivity.this);
                            myAddressDBHelper.open();
                            myAddressDBHelper.insertMyAddress(userAddress);
                            myAddressDBHelper.close();


                            if (userAddress.getAreaId() != -1) {
                                Intent data = new Intent();
                                data.putExtra("UserAddress", userAddress);
                                setResult(RESULT_OK, data);
                                finish();
                                overridePendingTransition(R.anim.start_activity_slide_in_right, R.anim.start_activity_slide_out_left);
                            } else {
                                buildAlertMessageNotOperationArea(userAddress);
                            }

                        } else {
                            // CommonMethod.showAlert("we are not operational in this city.Please select another", FetchLocationActivity.this);
                            invalidLoc();
                        }

                    } else {
                        //CommonMethod.showAlert("we are not operational in this city.Please select another", FetchLocationActivity.this);
                        invalidLoc();
                    }

                } else {
                    //CommonMethod.showAlert("we are not operational in this city.Please select another", FetchLocationActivity.this);
                    invalidLoc();
                }
                break;
        }
    }*/

    private void invalidLoc() {
        layoutNonOpCity.setVisibility(View.VISIBLE);
        myLocationLayout.setVisibility(View.GONE);
        layoutEnterArea.setVisibility(View.GONE);
    }


    private void buildAlertMessageNotOperationArea(final UserAddress userAddress) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We could not find any active partner in your selected area.\n" +
                "\nYour order will take more than 2 hours to be delivered. Do you want to continue?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent data = new Intent();
                        data.putExtra("UserAddress", userAddress);
                        setResult(RESULT_OK, data);
                        finish();
                        overridePendingTransition(R.anim.start_activity_slide_in_right, R.anim.start_activity_slide_out_left);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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