package com.monkporter.zafran.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.monkporter.zafran.Interface.ApiInterface;
import com.monkporter.zafran.R;
import com.monkporter.zafran.database.AppDBHelper;
import com.monkporter.zafran.helper.PrefManager;
import com.monkporter.zafran.model.Address;
import com.monkporter.zafran.model.AddressResponse;
import com.monkporter.zafran.rest.ApiClient;
import com.monkporter.zafran.utility.CommonMethod;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryAddress extends AppCompatActivity implements View.OnClickListener {
    private ImageView back_btn, edit_btn;
    private EditText company_name, company_address, instructions;
    private TextView delivery_area;
    private Button save_and_deliver;
    private ProgressDialog networkProgress;
    private AlertDialog.Builder builder;


    private String mLocation, mLatitude, mLongitude;

    private String mCompanyName, mCompanyAddress, mInstructions;

    private static final String TAG = "DeliveryAddress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        init();


    }

    private void init() {
        back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);

        edit_btn = (ImageView) findViewById(R.id.edit_btn);
        edit_btn.setOnClickListener(this);

        company_name = (EditText) findViewById(R.id.company_name);
        company_address = (EditText) findViewById(R.id.company_address);
        instructions = (EditText) findViewById(R.id.instructions);

        delivery_area = (TextView) findViewById(R.id.delivery_area);
        mLocation = PrefManager.getInstance(DeliveryAddress.this).getUserCurrentLocation();
        if (null != mLocation) {
            delivery_area.setText(mLocation);
        }

        save_and_deliver = (Button) findViewById(R.id.save_and_deliver);
        save_and_deliver.setOnClickListener(this);
        networkProgress = new ProgressDialog(this);
        networkProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        networkProgress.setMessage("Saving Address...");
        builder = new AlertDialog.Builder(DeliveryAddress.this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.edit_btn:
                startPlacesActivity(DeliveryAddress.this);
                break;
            case R.id.save_and_deliver:

                if (!networkProgress.isShowing()) {

                    Address oldAddress = AppDBHelper.getInstance(DeliveryAddress.this).
                            addressExists(Float.parseFloat(PrefManager.getInstance(DeliveryAddress.this).getUserCurrentLatitude()),
                                    Float.parseFloat(PrefManager.getInstance(DeliveryAddress.this).getUserCurrentLongitude()));

                    if (null == oldAddress) {
                        networkProgress.show();
                        if (CommonMethod.checkInternet()) {
                            sendAddressToServer();
                        } else {
                            Toast.makeText(this, "Please turn on internet", Toast.LENGTH_SHORT).show();
                            networkProgress.cancel();
                        }
                    } else {
                        builder.setMessage("same address already exists do you still want to add this address");
                        String positiveText = getString(android.R.string.ok);
                        builder.setPositiveButton(positiveText,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        networkProgress.show();
                                        if (CommonMethod.checkInternet()) {
                                            sendAddressToServer();
                                        } else {
                                            Toast.makeText(DeliveryAddress.this, "Please turn on internet", Toast.LENGTH_SHORT).show();
                                            networkProgress.cancel();
                                        }
                                    }
                                });

                        String negativeText = getString(android.R.string.cancel);
                        builder.setNegativeButton(negativeText,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startAddressSelectionActivity(DeliveryAddress.this);
                                    }
                                });
                    }
                    AlertDialog dialog = builder.create();
                    // display dialog
                    dialog.show();
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void startPlacesActivity(Context context) {
        Intent starter = new Intent(context, PlacesAutoCompleteActivity.class);
        context.startActivity(starter);
    }

    private void startCheckOutActivity(Context context) {
        Intent starter = new Intent(context, CheckoutScreen.class);
        context.startActivity(starter);
    }

    private void startAddressSelectionActivity(Context context) {
        Intent starter = new Intent(context, AddressDetail.class);
        context.startActivity(starter);
    }

    private void sendAddressToServer() {
/*
        {
         * "UserID" : 61,
         * "AreaName":"nehru nagar",  --------> COMPANY ADDRESS
         * "CityName":"ghaziabad",    --------> INSTRUCTIONS and PLACE ID
         * "CompanyName":"Hours",
         * "AddressStreet":"arjun nagar",  -----> Area and Locality
         * "Latitude":28.628937,
         * "Longitude":77.371140
         }
 */
        PrefManager prefManager = PrefManager.getInstance(DeliveryAddress.this);
        final Address params = new Address();
        params.setUserID(prefManager.getUserId());

        //Any precise address give by user
        mCompanyAddress = company_address.getText().toString();
        if (null != mCompanyAddress)
            params.setAreaName(mCompanyAddress);

        //set the instructions and Place-Id provided by google maps api
        mInstructions = instructions.getText().toString();
        params.setPlaceID(prefManager.getUserCurrentPlaceId());

        if (null != mInstructions)
            params.setCityName(mInstructions + ":" + params.getPlaceID());

        mCompanyName = company_name.getText().toString();
        if (null != mCompanyName)
            params.setCompanyName(mCompanyName);

        if (null != mLocation)
            params.setAddressStreet(mLocation);

        mLatitude = prefManager.getUserCurrentLatitude();
        if (null != mLatitude)
            params.setLatitude(Float.parseFloat(mLatitude));

        mLongitude = prefManager.getUserCurrentLongitude();
        if (null != mLongitude)
            params.setLongitude(Float.parseFloat(mLongitude));

        Log.d(TAG, "Create address entry on server: " + params.toString());

        ApiInterface createAddressRequest = ApiClient.getClient().create(ApiInterface.class);
        Call<AddressResponse> call = createAddressRequest.getResponse_CreateAddress(params);
        call.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (null != response && response.isSuccessful()) {
                    AddressResponse responseObj = response.body();
                    if (null != responseObj) {
                        Log.d(TAG, "response from address creation " + responseObj.toString());
                        if (!responseObj.isError()) {
                            params.setAddressId(responseObj.getAddressId());
                            //reset
                            if (null != mInstructions)
                                params.setCityName(mInstructions);

                            //enter values in db for future use
                            AppDBHelper.getInstance(DeliveryAddress.this).addAddress(params);

                            startCheckOutActivity(DeliveryAddress.this);

                            AppDBHelper.getInstance(DeliveryAddress.this).getAddress();

                        } else {
                            // TODO: sajal 12-04-2017 make check for entries in edit text
                            Toast.makeText(DeliveryAddress.this, "Please recheck entries", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                networkProgress.cancel();
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                networkProgress.cancel();
                t.printStackTrace();
            }
        });
    }


}
