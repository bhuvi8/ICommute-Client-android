package com.busktimachu.icommute.icommute;

import android.provider.BaseColumns;

/**
 * Created by bhuvi on 29/3/15.
 */
public final class ICommuteDB {
    public ICommuteDB() {
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ICommute.db";

    // TODO : add columns for lat and long and possibly accuracy for opening stop in a map
    public static abstract class Route_map implements BaseColumns {
        public static final String TABLE_NAME = "route_map";
        public static final String COLUMN_NAME_ROUTE_NAME = "route_name";
        public static final String COLUMN_NAME_AREA_NAME = "area_name";
        public static final String COLUMN_NAME_LANDMARK_NAME = "landmark_name";
        public static final String COLUMN_NAME_ETA = "eta";
    }

    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String COMMA_SEP = ",";

    public static final String CREATE_TABLE_ROUTE_MAP =
            "CREATE TABLE " + Route_map.TABLE_NAME + " (" +
                    Route_map._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Route_map.COLUMN_NAME_ROUTE_NAME + INTEGER_TYPE + COMMA_SEP +
                    Route_map.COLUMN_NAME_AREA_NAME + INTEGER_TYPE + COMMA_SEP +
                    Route_map.COLUMN_NAME_LANDMARK_NAME + INTEGER_TYPE + COMMA_SEP +
                    Route_map.COLUMN_NAME_ETA + TEXT_TYPE +
                    " );";

    public static final String DELETE_TABLE_ROUTE_MAP =
            "DROP TABLE IF EXISTS " + Route_map.TABLE_NAME;

}
