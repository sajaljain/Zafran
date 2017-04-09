package com.monkporter.zafran.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.monkporter.zafran.helper.PrefManager;
import com.monkporter.zafran.model.Address;
import com.monkporter.zafran.model.AddressResponse;
import com.monkporter.zafran.rest.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryAddress extends AppCompatActivity implements View.OnClickListener {
    private ImageView back_btn, edit_btn;
    private EditText company_name, company_address, instructions;
    private TextView delivery_area;
    private Button save_and_deliver;

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
                sendAddressToServer();
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


    private void sendAddressToServer() {
        /*{
         * "UserID" : 61,
         * "AreaName":"nehru nagar",
         * "CityName":"ghaziabad",
         * "CompanyName":"Hours",
         * "AddressStreet":"arjun nagar",
         * "Latitude":28.628937,
         * "Longitude":77.371140
         * }
 */
        PrefManager prefManager = PrefManager.getInstance(DeliveryAddress.this);
        Address params = new Address();
        params.setUserID(prefManager.getUserId());

        //Any precise address give by user
        mCompanyAddress = company_address.getText().toString();
        if (null != mCompanyAddress)
            params.setAreaName(mCompanyAddress);

        //set the instructions and Place-Id provided by google maps api
        mInstructions = instructions.getText().toString();
        if (null != mInstructions)
            params.setCityName(mInstructions + ":" + prefManager.getUserCurrentPlaceId());

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
                            responseObj.getAddressId();
                            // TODO: sajal 10-04-2017 add entry in database
                            // TODO: sajal 10-04-2017 add gotoCheckOut Activity
                        } else {
                            Toast.makeText(DeliveryAddress.this, "Please recheck entries", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
