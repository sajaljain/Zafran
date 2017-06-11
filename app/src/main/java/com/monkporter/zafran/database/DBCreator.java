package com.monkporter.zafran.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;


public class DBCreator extends SQLiteOpenHelper {

    private static final String TAG = "DbUpdate";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "monkporter";


    // Contacts table name
    public static final String TABLE_ADDRESS = "Address";
    public static final String ADDRESS_ID = "AddressId";
    public static final String USER_ID = "UserId";
    public static final String AREA_NAME = "AreaName";
    public static final String CITY_NAME = "CityName";
    public static final String COMPANY_NAME = "CompanyName";
    public static final String ADDRESS_STREET = "AddressStreet";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String PLACE_ID = "PlaceId";

    private static String DB_NAME = "monkporter.sqlite";

    private static DBCreator dbCreator;
    private SQLiteDatabase db;
    private Context mContext;


    // constructor
    public DBCreator(Context context) {
        super(context, Environment.getExternalStorageDirectory().getPath()+"/"+DB_NAME, null, DATABASE_VERSION);

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

        Log.d(TAG, "onCreate: of database "+Environment.getExternalStorageDirectory().getPath()+DB_NAME);

        //SELECT * FROM `address``CreationTime``AddressId`, `UserId`, `AreaName`, `CityName`, `CompanyName`, `AddressStreet`, `Latitude`, `Longitude`


        String CREATE_APP_TABLE = "CREATE TABLE " + TABLE_ADDRESS + "("
                + ADDRESS_ID + " TEXT PRIMARY KEY," +
                USER_ID + " TEXT," +
                AREA_NAME + " TEXT," +
                CITY_NAME + " TEXT," +
                COMPANY_NAME + " TEXT," +
                ADDRESS_STREET + " TEXT," +
                LATITUDE + " TEXT," +
                LONGITUDE + " TEXT," +
                PLACE_ID + " TEXT UNIQUE" +
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
