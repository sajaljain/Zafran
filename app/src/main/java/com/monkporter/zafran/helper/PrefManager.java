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

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsWaitingForSms(boolean isWaiting) {
        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();
    }

    public boolean isWaitingForSms() {
        return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
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

    public void saveLocations(ArrayList<PlacesAutoCompleteAdapter.PlaceAutocomplete> arrayList){

      Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PlacesAutoCompleteAdapter.PlaceAutocomplete>>() {}.getType();
        String json = gson.toJson(arrayList,type);
        editor.putString(SELECTED_LOCATION,json);
        editor.commit();


    }
    public ArrayList<PlacesAutoCompleteAdapter.PlaceAutocomplete> getSaveLocations(){
        Gson gson = new Gson();
        String json = pref.getString(SELECTED_LOCATION, null);
        Type type = new TypeToken<ArrayList<PlacesAutoCompleteAdapter.PlaceAutocomplete>>() {}.getType();
        ArrayList<PlacesAutoCompleteAdapter.PlaceAutocomplete> arrayList = gson.fromJson(json, type);
        return arrayList;
    }

    public void setUserCurrentLocation(String address){
        editor.putString(USER_CURRENT_LOCATION,address);
        editor.commit();
    }

    public String getUserCurrentLocation(){
        return pref.getString(USER_CURRENT_LOCATION,null);
    }

    public void setUserId(int userId){
        editor.putInt(USER_ID,userId);
        editor.commit();
    }
    public int getUserId(){
        return pref.getInt(USER_ID,-1);
    }
    public void setUsername(String username){
        editor.putString(USERNAME,username);
        editor.commit();
    }
    public String getUsername(){
        return pref.getString(USERNAME,null);
    }
}
