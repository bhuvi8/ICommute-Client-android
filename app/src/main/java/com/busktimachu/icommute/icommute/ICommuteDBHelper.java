package com.busktimachu.icommute.icommute;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bhuvi on 29/3/15.
 */
public class ICommuteDBHelper extends SQLiteOpenHelper {

    public ICommuteDBHelper(Context context) {
        super(context, ICommuteDB.DATABASE_NAME, null, ICommuteDB.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ICommuteDB.CREATE_TABLE_ROUTE_TXT);
        db.execSQL(ICommuteDB.CREATE_TABLE_AREA_TXT);
        db.execSQL(ICommuteDB.CREATE_TABLE_LANDMARK_TXT);
        db.execSQL(ICommuteDB.CREATE_TABLE_ROUTE_MAP);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(ICommuteDB.DELETE_TABLE_ROUTE_TXT);
        db.execSQL(ICommuteDB.DELETE_TABLE_AREA_TXT);
        db.execSQL(ICommuteDB.DELETE_TABLE_LANDMARK_TXT);
        db.execSQL(ICommuteDB.DELETE_TABLE_ROUTE_MAP);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



}
