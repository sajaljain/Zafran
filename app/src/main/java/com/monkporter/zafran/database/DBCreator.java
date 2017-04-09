package com.monkporter.zafran.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBCreator extends SQLiteOpenHelper {

    private static final String TAG = "DbUpdate";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "monkporter";


    // Contacts table name
    private static final String TABLE_APP_DETAILS = "Address";
    private static final String ADDRESS_ID = "AddressId";
    private static final String USER_ID = "UserId";
    private static final String AREA_NAME = "AreaName";
    private static final String CITY_NAME = "CityName";
    private static final String COMPANY_NAME = "CompanyName";
    private static final String ADDRESS_STREET = "AddressStreet";
    private static final String LATITUDE = "Latitude";
    private static final String LONGITUDE = "Longitude";

    private static String DB_NAME = "monkporter.sqlite";

    private static DBCreator dbCreator;
    private SQLiteDatabase db;
    private Context mContext;


    // constructor
    public DBCreator(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        mContext = context;

    }

    public static synchronized DBCreator getInstance(Context context) {

        if (dbCreator == null) {
            dbCreator = new DBCreator(context.getApplicationContext());
        }
        return dbCreator;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "onCreate: of database ");

        //SELECT * FROM `address``CreationTime``AddressId`, `UserId`, `AreaName`, `CityName`, `CompanyName`, `AddressStreet`, `Latitude`, `Longitude`


        String CREATE_APP_TABLE = "CREATE TABLE " + TABLE_APP_DETAILS + "("
                + ADDRESS_ID + " TEXT PRIMARY KEY," +
                USER_ID + " TEXT," +
                AREA_NAME + " TEXT," +
                CITY_NAME + " TEXT," +
                COMPANY_NAME + " TEXT," +
                ADDRESS_STREET + " TEXT," +
                LATITUDE + " TEXT," +
                LONGITUDE + " TEXT," +
                ")";
        db.execSQL(CREATE_APP_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Seq", "I am here from upgrade");
        Log.d(TAG, "onUpgrade: " + oldVersion);
        Log.d(TAG, "onUpgrade: " + newVersion);

        //updateDatabase(oldVersion, db);


    }

    private void updateDatabase(int oldVersion, SQLiteDatabase db) {
        switch (oldVersion) {
            case 1:
                updateDatabaseVersion1(db);

            case 2:
                //   updateDatabaseVersion2(db);
            case 3:
                // updateDatabaseVersion3(db);
            default:
                Log.d(TAG, "updateDatabase: default " + oldVersion);
                break;
        }
    }


    private void updateDatabaseVersion1(SQLiteDatabase db) {

    }


}
