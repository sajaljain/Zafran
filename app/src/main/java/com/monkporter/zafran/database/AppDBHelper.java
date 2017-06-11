package com.monkporter.zafran.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.monkporter.zafran.model.Address;

import java.util.ArrayList;
import java.util.List;


public class AppDBHelper {

    private static DBCreator mDBConnection;
    private static AppDBHelper helper;
    SQLiteDatabase db;

    private AppDBHelper(Context context) {

        mDBConnection = DBCreator.getInstance(context);

    }

    public static AppDBHelper getInstance(Context context) {

        if (helper == null)
            helper = new AppDBHelper(context);
        return helper;
    }

    public void open() throws SQLException {
        db = mDBConnection.getWritableDatabase();
    }

    public void close() {
        mDBConnection.close();
    }


/*    *
     * This method adds ImageModel img to database table Images
     *
     * @param img ImageModel to enter into database*/

    public synchronized void addAddress(Address address) {
        /*
        * {
         * "UserID" : 61,
         * "AreaName":"nehru nagar",  --------> COMPANY ADDRESS
         * "CityName":"ghaziabad",    --------> INSTRUCTIONS and PLACE ID
         * "CompanyName":"Hours",
         * "AddressStreet":"arjun nagar",  -----> Area and Locality
         * "Latitude":28.628937,
         * "Longitude":77.371140
         * }
         */
        ContentValues values = new ContentValues();
        values.put(DBCreator.ADDRESS_ID, "" + address.getAddressId());
        values.put(DBCreator.USER_ID, "" + address.getUserID());
        values.put(DBCreator.AREA_NAME, address.getAreaName());
        values.put(DBCreator.CITY_NAME, address.getCityName());
        values.put(DBCreator.COMPANY_NAME, address.getCompanyName());
        values.put(DBCreator.ADDRESS_STREET, address.getAddressStreet());
        values.put(DBCreator.LATITUDE, "" + address.getLatitude());
        values.put(DBCreator.LONGITUDE, "" + address.getLongitude());
        values.put(DBCreator.PLACE_ID, address.getPlaceID());

        // Inserting Row
        try {
            open();
            db.insert(DBCreator.TABLE_ADDRESS, null, values);
            Log.d("AddressID", "new Image added " + address.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

    }


    public synchronized List<Address> getAddress() {

        List<Address> addresses = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + DBCreator.TABLE_ADDRESS;

        try {
            open();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Address address = new Address();

                    address.setAddressId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBCreator.ADDRESS_ID))));
                    address.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBCreator.USER_ID))));
                    address.setAreaName(cursor.getString(cursor.getColumnIndex(DBCreator.AREA_NAME)));
                    address.setCityName(cursor.getString(cursor.getColumnIndex(DBCreator.CITY_NAME)));
                    address.setCompanyName(cursor.getString(cursor.getColumnIndex(DBCreator.COMPANY_NAME)));
                    address.setAddressStreet(cursor.getString(cursor.getColumnIndex(DBCreator.ADDRESS_STREET)));
                    address.setLatitude(Float.parseFloat(cursor.getString(cursor.getColumnIndex(DBCreator.LATITUDE))));
                    address.setLongitude(Float.parseFloat(cursor.getString(cursor.getColumnIndex(DBCreator.LONGITUDE))));
                    address.setPlaceID(cursor.getString(cursor.getColumnIndex(DBCreator.PLACE_ID)));

                        Log.d("AddressID", "printing Image added " + address.toString());
                    // Adding contact to list
                    addresses.add(address);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return addresses;


    }


    public Address addressExists(float lat, float log) {
        boolean entryExists = false;
        String selectQuery = "SELECT " + DBCreator.AREA_NAME + "," + DBCreator.COMPANY_NAME + "," + DBCreator.ADDRESS_STREET + " FROM " + DBCreator.TABLE_ADDRESS + " WHERE " + DBCreator.LATITUDE + " = '" + lat +"' AND " + DBCreator.LONGITUDE + " = '" + log + "'";
        try {
            open();
            Cursor cursor = db.rawQuery(selectQuery,null);

            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                Address address = new Address();
                address.setAreaName(cursor.getString(0));
                address.setCompanyName(cursor.getString(1));
                address.setAddressStreet(cursor.getString(2));
                return address;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return null;
    }

}


