package com.monkporter.zafran.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


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


    /**
     * This method adds ImageModel img to database table Images
     *
     * @param img ImageModel to enter into database

    public synchronized void addImage(ImageModel img) {

        ContentValues values = new ContentValues();
        values.put(DBCreator.PATH, img.getPath());
        values.put(DBCreator.RESPONSE_STATUS, img.getResponseStatus());
        values.put(DBCreator.ACTION_STATUS, img.getActionStatus());
        values.put(DBCreator.TRASH_PATH, img.getTrashPath());

        // Inserting Row
        try {
            open();
            db.insert(DBCreator.IMAGES_TB, null, values);
            Log.d("ImageDB", "new Image added " + img.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

    }
    */

}

