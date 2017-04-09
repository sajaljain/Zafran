package com.monkporter.zafran.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.monkporter.zafran.Interface.ApiInterface;
import com.monkporter.zafran.R;
import com.monkporter.zafran.Zafran;
import com.monkporter.zafran.helper.PrefManager;
import com.monkporter.zafran.model.Constants;
import com.monkporter.zafran.model.OtpUserObjResponse;
import com.monkporter.zafran.model.UserDetail;
import com.monkporter.zafran.model.UserDetailResponse;
import com.monkporter.zafran.model.VerifyOtp;
import com.monkporter.zafran.model.VerifyOtpResponse;
import com.monkporter.zafran.rest.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "SmsActivity";
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Button btnRequestSms, btnVerifyOtp;
    private EditText inputName, inputEmail, inputMobile, inputOtp;
    private ProgressBar progressBar, progressBarBottom;
    private PrefManager pref;
    private ImageView btnEditMobile;
    private TextView txtEditMobile;
    private LinearLayout layoutEditMobile;
    ApiInterface requestSms, otpPostRequest;
    IntentFilter OTPMessageFilter;
    private CountDownTimer countDownTimer;

    //RECEIVER
    BroadcastReceiver OTPMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Constants.SMS_RECEIVED_ACTION)) {
                final Bundle bundle = intent.getExtras();
                try {
                    if (bundle != null) {
                        Object[] pdusObj = (Object[]) bundle.get("pdus");
                        for (Object aPdusObj : pdusObj) {
                            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                            String senderAddress = currentMessage.getDisplayOriginatingAddress();
                            String message = currentMessage.getDisplayMessageBody();

                            Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);

                            // if the SMS is not from our gateway, ignore the message
                            if (!senderAddress.toLowerCase().contains("IM-WAYSMS".toLowerCase())) {
                                return;
                            }

                            // verification code from sms
                            String verificationCode = getVerificationCode(message);

                            Log.e(TAG, "OTP received: " + verificationCode);
                            verifyOtp(verificationCode);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }
        }
    };

    /**
     * Getting the OTP from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message
     * @return
     */
    private String getVerificationCode(String message) {
        String code = null;
        int index = message.indexOf("!");

        if (index != -1) {
            int start = index + 2;
            int length = 4;
            code = message.substring(start, start + length);
            return code;
        }

        return code;
    }


    private void verifyOtp(final String otp) {
        VerifyOtp vOtp = new VerifyOtp();
        vOtp.setUserId(PrefManager.getInstance(this).getUserId());
        vOtp.setOtp(otp);
        Log.d(TAG, "verifyOtp: " + vOtp.toString());

        otpPostRequest = ApiClient.getClient().create(ApiInterface.class);
        Call<VerifyOtpResponse> call = otpPostRequest.getResponse_OTP(vOtp);


        call.enqueue(new Callback<VerifyOtpResponse>() {
            @Override
            public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                if (null != response && response.isSuccessful()) {
                    VerifyOtpResponse responseObj = response.body();
                    if (null != responseObj) {
                        Log.d(TAG, "response from verify_otp" + responseObj.toString());
                        String message = responseObj.getMessage();

                        if (!responseObj.isError()) {

                            OtpUserObjResponse profileObj = responseObj.getProfile();
                            Log.d(TAG, "OtpResponse: " + profileObj.toString());

                            String name = profileObj.getName();
                            String email = profileObj.getEmail();
                            String mobile = profileObj.getMobile();
                            PrefManager pref = PrefManager.getInstance(SmsActivity.this);
                            pref.setUserId(profileObj.getUserId());
                            pref.createLogin(name, email, mobile);

                            finish();
                            startAddressActivity(SmsActivity.this);


                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        viewPager = (ViewPager) findViewById(R.id.viewPagerVertical);
        inputName = (EditText) findViewById(R.id.inputName);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputMobile = (EditText) findViewById(R.id.inputMobile);
        inputOtp = (EditText) findViewById(R.id.inputOtp);
        btnRequestSms = (Button) findViewById(R.id.btn_request_sms);
        btnVerifyOtp = (Button) findViewById(R.id.btn_verify_otp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnEditMobile = (ImageView) findViewById(R.id.btn_edit_mobile);
        txtEditMobile = (TextView) findViewById(R.id.txt_edit_mobile);
        layoutEditMobile = (LinearLayout) findViewById(R.id.layout_edit_mobile);
        progressBarBottom = (ProgressBar) findViewById(R.id.timer_progressbar);
        progressBarBottom.setMax(11600);

        // view click listeners
        btnEditMobile.setOnClickListener(this);
        btnRequestSms.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);

        // hiding the edit mobile number
        layoutEditMobile.setVisibility(View.GONE);

        pref = PrefManager.getInstance(SmsActivity.this);

        // Checking for user session
        // if user is already logged in, take him to main activity
        if (pref.isLoggedIn()) {
            Intent intent = new Intent(SmsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
        }

        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        /**
         * Checking if the device is waiting for sms
         * showing the user OTP screen
         */
        if (pref.isWaitingForSms()) {
            viewPager.setCurrentItem(1);
            layoutEditMobile.setVisibility(View.VISIBLE);
        }

        //REGISTER
        OTPMessageFilter = new IntentFilter();
        OTPMessageFilter.addAction(Constants.SMS_RECEIVED_ACTION);
        registerReceiver(OTPMessageReceiver,
                OTPMessageFilter);
        Log.d(TAG, "broadcast registered");


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_request_sms:
                validateForm();
                break;

            case R.id.btn_verify_otp:
                verifyOtp();
                break;

            case R.id.btn_edit_mobile:
                viewPager.setCurrentItem(0);
                layoutEditMobile.setVisibility(View.GONE);
                pref.setIsWaitingForSms(false);
                break;
        }
    }

    /**
     * Validating user details form
     */
    private void validateForm() {
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String mobile = inputMobile.getText().toString().trim();

        // validating empty name and email
        if (name.length() == 0 || email.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your details", Toast.LENGTH_SHORT).show();
            return;
        }

        // validating mobile number
        // it should be of 10 digits length
        if (isValidPhoneNumber(mobile)) {

            // request for sms
            progressBar.setVisibility(View.VISIBLE);

            // saving the mobile number in shared preferences
            pref.setMobileNumber(mobile);

            // requesting for sms
            requestForSMS(name, email, mobile);

        } else {
            Toast.makeText(getApplicationContext(), "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Method initiates the SMS request on the server
     *
     * @param name   user name
     * @param email  user email address
     * @param mobile user valid mobile number
     */

    private void requestForSMS(final String name, final String email, final String mobile) {
        final UserDetail userDetail = new UserDetail();

        userDetail.setName(name);
        userDetail.setEmailId(email);
        userDetail.setCell(mobile);
        userDetail.setUserId(PrefManager.getInstance(Zafran.getInstance()).getUserId());
        userDetail.setDeviceRegistrationID(PrefManager.getInstance(Zafran.getInstance()).getFireBaseId());

        Log.d(TAG, "requestForSMS: " + userDetail.toString());
        requestSms = ApiClient.getClient().create(ApiInterface.class);
        Call<UserDetailResponse> call = requestSms.getResponse_RequestSms(userDetail);
        call.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {

                if (response != null && response.isSuccessful()) {
                    UserDetailResponse userDetailResponse = response.body();

                    if (null != userDetailResponse) {
                        Log.d(TAG, "onResponse: requestForSMS" + userDetailResponse.toString());
                        boolean error = Boolean.parseBoolean(userDetailResponse.getError().toLowerCase());
                        String message = userDetailResponse.getMessage();

                        // checking for error, if not error SMS is initiated
                        // device should receive it shortly

                        Boolean smsSendStatus = Boolean.parseBoolean(userDetailResponse.getSmsSend().toLowerCase());

                        if (!error && smsSendStatus) {

                            startTimer();
                            // boolean flag saying device is waiting for sms

                            // TODO: sajal 10-04-2017 remove this shared pref from here
                            pref.setIsWaitingForSms(true);
                            pref.setUserId(userDetailResponse.getUserId());
                            // moving the screen to next pager item i.e otp screen
                            viewPager.setCurrentItem(1);
                            txtEditMobile.setText(pref.getMobileNumber());
                            layoutEditMobile.setVisibility(View.VISIBLE);

                        } else if (!error && !smsSendStatus) {
                            //No SMS is send, user with this number is already verified
                            // TODO: sajal 10-04-2017 check if condn for starting checkout screen or Delivery address screen
                            finish();
                            startAddressActivity(SmsActivity.this);

                        } else {


                            //some error occurred
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + message,
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                // hiding the progress bar
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                t.printStackTrace();
                startActivity(new Intent(SmsActivity.this, Refresh.class));
            }
        });
    }

    /**
     * sending the OTP to server and activating the user
     */
    private void verifyOtp() {
        String otp = inputOtp.getText().toString().trim();

        if (!otp.isEmpty()) {
            /*Intent grapprIntent = new Intent(getApplicationContext(), HttpService.class);
            grapprIntent.putExtra("otp", otp);
            startService(grapprIntent);*/
            verifyOtp(otp);


        } else {
            Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Regex to validate the mobile number
     * mobile number should be of 10 digits length
     *
     * @param mobile
     * @return
     */
    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }


    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        public Object instantiateItem(View collection, int position) {

            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.layout_sms;
                    break;
                case 1:
                    resId = R.id.layout_otp;
                    break;
            }
            return findViewById(resId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //UNREGISTER
        unregisterReceiver(OTPMessageReceiver);
        Log.d(TAG, "broadcast unregistered");
    }

    public static void startAddressActivity(Context context) {

        Intent starter = new Intent(context, DeliveryAddress.class);
        context.startActivity(starter);
    }

    private void startTimer() {
        progressBarBottom.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(20000, 10) {

            int prevProgress = 0;

            public void onTick(long millisUntilFinished) {

                prevProgress += 10;
                progressBarBottom.setProgress(prevProgress);
            }

            public void onFinish() {


                Log.d(TAG, "onFinish: " + prevProgress);
//                finish();
//                Intent intentResult = new Intent(getApplicationContext() , JunkCleanResultActivity.class);
//                intentResult.putExtra(EXTRA_NO_OF_IMAGES , mImagePath.size());
//                startActivity(intentResult);

                // TODO: sajal 09-04-2017 goto next activity
            }
        };
        countDownTimer.start();

    }
}
