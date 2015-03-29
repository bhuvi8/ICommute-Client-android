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

    public static abstract class Route_txt implements BaseColumns {
        public static final String TABLE_NAME = "route_no";
        public static final String COLUMN_NAME_ROUTE_NAME = "routeno";
    }
    public static abstract class Area_txt implements BaseColumns {
        public static final String TABLE_NAME = "area_txt";
        public static final String COLUMN_NAME_AREA_NAME = "area_name";
    }
    public static abstract class Landmark_txt implements BaseColumns {
        public static final String TABLE_NAME = "landmark_txt";
        public static final String COLUMN_NAME_LANDMARK_NAME = "landmark_name";
    }

    public static abstract class Route_map implements BaseColumns {
        public static final String TABLE_NAME = "route_map";
        public static final String COLUMN_NAME_ROUTE_ID = "route_id";
        public static final String COLUMN_NAME_AREA_ID = "area_id";
        public static final String COLUMN_NAME_LANDMARK_ID = "landmark_id";
        public static final String COLUMN_NAME_ETA = "eta";
    }

    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String COMMA_SEP = ",";

    public static final String CREATE_TABLE_ROUTE_TXT =
            "CREATE TABLE " + Route_txt.TABLE_NAME + " (" +
                    Route_txt._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Route_txt.COLUMN_NAME_ROUTE_NAME + TEXT_TYPE +
            " );";

    public static final String CREATE_TABLE_AREA_TXT =
            "CREATE TABLE " + Area_txt.TABLE_NAME + " (" +
                    Area_txt._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Area_txt.COLUMN_NAME_AREA_NAME + TEXT_TYPE +
                    " );";

    public static final String CREATE_TABLE_LANDMARK_TXT =
            "CREATE TABLE " + Landmark_txt.TABLE_NAME + " (" +
                    Landmark_txt._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Landmark_txt.COLUMN_NAME_LANDMARK_NAME + TEXT_TYPE +
                    " );";

    public static final String CREATE_TABLE_ROUTE_MAP =
            "CREATE TABLE " + Route_map.TABLE_NAME + " (" +
                    Route_map._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Route_map.COLUMN_NAME_ROUTE_ID + INTEGER_TYPE + COMMA_SEP +
                    Route_map.COLUMN_NAME_AREA_ID + INTEGER_TYPE + COMMA_SEP +
                    Route_map.COLUMN_NAME_LANDMARK_ID + INTEGER_TYPE + COMMA_SEP +
                    Route_map.COLUMN_NAME_ETA + TEXT_TYPE +
                    " );";

    public static final String DELETE_TABLE_ROUTE_TXT =
            "DROP TABLE IF EXISTS " + Route_txt.TABLE_NAME;

    public static final String DELETE_TABLE_AREA_TXT =
            "DROP TABLE IF EXISTS " + Area_txt.TABLE_NAME;

    public static final String DELETE_TABLE_LANDMARK_TXT =
            "DROP TABLE IF EXISTS " + Landmark_txt.TABLE_NAME;

    public static final String DELETE_TABLE_ROUTE_MAP =
            "DROP TABLE IF EXISTS " + Route_map.TABLE_NAME;

}
