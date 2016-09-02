package com.monkporter.zafran.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monkporter.zafran.activity.PlacesAutoCompleteActivity;
import com.monkporter.zafran.adapter.PlacesAutoCompleteAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Retrofit;

/**
 * Created by Vaibhav on 7/14/2016.
 */
public class PrefManager {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "ZAFRAN_PREF";

    // All Shared Preferences Keys
    private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
    private static final String KEY_MOBILE_NUMBER = "mobile_number";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE = "mobile";
    private static final String SELECTED_LOCATION = "myLocation";
    private static final String USER_CURRENT_LOCATION = "currentLocation";
    private static final String USER_ID = "userId";
    private static final String USERNAME = "userName";
    private static final String FIRST_TIME = "firstTimeUser";
    private static final String IS_TEMP_USER = "isTempUser";
    private static final String DEVICE_REG_ID = "deviceRegId";
    private static final String USER_LATITUDE = "mLatitude";
    private static final String USER_LONGITUDE = "mLongitude";
    private static final String USER_PLACEID = "mPlaceID";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String PLACEID = "placeid";
    private static final String REF_SCRN = "pressExit";
    private static PrefManager pm = null;

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static PrefManager getInstance(Context context){

        if(pm==null){
            pm = new PrefManager(context);
        }
        return pm;
    }

    public void setIsWaitingForSms(boolean isWaiting) {
        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();
    }

    public boolean isWaitingForSms() {
        return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
    }

    public void pressExit(boolean press) {
        editor.putBoolean(REF_SCRN, press);
        editor.commit();
    }

    public boolean isExit() {
        return pref.getBoolean(REF_SCRN, false);
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString(KEY_MOBILE_NUMBER, mobileNumber);
        editor.commit();
    }


    public String getMobileNumber() {
        return pref.getString(KEY_MOBILE_NUMBER, null);
    }

    public void createLogin(String name, String email, String mobile) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("name", pref.getString(KEY_NAME, null));
        profile.put("email", pref.getString(KEY_EMAIL, null));
        profile.put("mobile", pref.getString(KEY_MOBILE, null));
        return profile;
    }

    public void saveLocations(ArrayList<String> arrayList) {

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        String json = gson.toJson(arrayList, type);
        editor.putString(SELECTED_LOCATION, json);
        editor.commit();


    }

    public void saveLongitude(ArrayList<String> arrayList) {

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        String json = gson.toJson(arrayList, type);
        editor.putString(LONGITUDE, json);
        editor.commit();


    }

    public void saveLatitude(ArrayList<String> arrayList) {

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        String json = gson.toJson(arrayList, type);
        editor.putString(LATITUDE, json);
        editor.commit();


    }

    public void savePlaceId(ArrayList<String> arrayList) {

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        String json = gson.toJson(arrayList, type);
        editor.putString(PLACEID, json);
        editor.commit();


    }

    public ArrayList<String> getSaveLocations() {
        Gson gson = new Gson();
        String json = pref.getString(SELECTED_LOCATION, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        return arrayList;
    }

    public ArrayList<String> getSaveLatitude() {
        Gson gson = new Gson();
        String json = pref.getString(LATITUDE, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        return arrayList;
    }

    public ArrayList<String> getSaveLongitude() {
        Gson gson = new Gson();
        String json = pref.getString(LONGITUDE, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        return arrayList;
    }

    public ArrayList<String> getSavePlaceId() {
        Gson gson = new Gson();
        String json = pref.getString(PLACEID, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        return arrayList;
    }


    public void setUserCurrentLocation(String address) {
        editor.putString(USER_CURRENT_LOCATION, address);
        editor.commit();
    }

    public void setUserCurrentLatitude(String lat) {
        editor.putString(USER_LATITUDE, lat);
        editor.commit();
    }

    public void setUserCurrentLongitude(String lon) {
        editor.putString(USER_LONGITUDE, lon);
        editor.commit();
    }

    public void setUserCurrentPlaceId(String placeId) {
        editor.putString(USER_PLACEID, placeId);
        editor.commit();
    }

    public String getUserCurrentLocation() {
        return pref.getString(USER_CURRENT_LOCATION, null);
    }

    public String getUserCurrentLatitude() {
        return pref.getString(USER_LATITUDE, null);
    }

    public String getUserCurrentLongitude() {
        return pref.getString(USER_LONGITUDE, null);
    }

    public String getUserCurrentPlaceId() {
        return pref.getString(USER_PLACEID, null);
    }

    public void setUserId(int userId) {
        editor.putInt(USER_ID, userId);
        editor.commit();
    }

    public int getUserId() {
        //here default user id is kept zero, compliance with backend
        return pref.getInt(USER_ID, 0);
    }

    public void setUsername(String username) {
        editor.putString(USERNAME, username);
        editor.commit();
    }

    public String getUsername() {
        return pref.getString(USERNAME, null);
    }

    public void setIsTempUser(boolean userType) {
        editor.putBoolean(IS_TEMP_USER, userType);
        editor.commit();
    }

    public boolean getIsTempUser() {
        return pref.getBoolean(IS_TEMP_USER, false);
    }

    public void setDeviceRegId(String deviceRegId) {
        editor.putString(DEVICE_REG_ID, deviceRegId);
        editor.commit();
    }

    public String getDeviceRegId() {
        return pref.getString(DEVICE_REG_ID, null);
    }
}
