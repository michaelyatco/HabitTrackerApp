package com.example.android.habittrackerapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.habittrackerapp.data.HabitContract.HabitEntry;
/**
 * Created by mjyatco on 8/25/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "habits.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link DbHelper}.
     *
     * @param context of the app
     */
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the habits table
        String SQL_CREATE_HABITS_TABLE = "CREATE TABLE " + HabitEntry.TABLE_NAME + " ("
                + HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HabitEntry.COLUMN_DATE + " INTEGER NOT NULL, "
                + HabitEntry.COLUMN_HABIT + " TEXT, "
                + HabitEntry.COLUMN_TYPE + " INTEGER NOT NULL, "
                + HabitEntry.COLUMN_TIME + " INTEGER NOT NULL, "
                + HabitEntry.COLUMN_COMMENT + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_HABITS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }

    public Cursor getRecord(int recordId) {
        Cursor record;
        String table = HabitContract.HabitEntry.TABLE_NAME;
        String selection = HabitContract.HabitEntry._ID + " = ? ";
        String[] selectionArgs = new String[]{Integer.toString(recordId)};
        db = getReadableDatabase();
        try {
            record = db.query(true, table, null, selection, selectionArgs, null, null, null, null);
            record.moveToFirst();
            record.close();
            db.close();
            return record;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return null;
        }
    }
}
