package com.example.android.habittrackerapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.habittrackerapp.data.DbHelper;
import com.example.android.habittrackerapp.data.HabitContract.HabitEntry;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

        private DbHelper mDbHelper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Setup FAB to open EditorActivity
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    startActivity(intent);
                }
            });

            // To access our database, we instantiate our subclass of SQLiteOpenHelper
            // and pass the context, which is the current activity
            mDbHelper = new DbHelper(this);

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String dateString = formatter.format(date);
            int dateInt = Integer.parseInt(dateString);
        }

        @Override
        protected void onStart() {
            super.onStart();
            displayDatabaseInfo();
        }

        /**
         * Temporary helper method to display information in the onscreen TextView about the state of
         * the habits database.
         */
        private void displayDatabaseInfo() {
            // Create and/or open a database to read from it
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            String[] projection = {
                    HabitEntry._ID,
                    HabitEntry.COLUMN_DATE,
                    HabitEntry.COLUMN_HABIT,
                    HabitEntry.COLUMN_TYPE,
                    HabitEntry.COLUMN_TIME,
                    HabitEntry.COLUMN_COMMENT,
            };

            Cursor cursor = db.query(
                    HabitEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);

            TextView displayView = (TextView) findViewById(R.id.text_view_habit);

            try {
                // Create a header in the Text View that looks like this:
                //
                //
                // In the while loop below, iterate through the rows of the cursor and display
                // the information from each column in this order.
                displayView.setText("The habits tracker table contains " + cursor.getCount() + " habits.\n\n");
                displayView.append(HabitEntry._ID + " - " +
                        HabitEntry.COLUMN_DATE + " - " +
                        HabitEntry.COLUMN_HABIT + " - " +
                        HabitEntry.COLUMN_TYPE + " - " +
                        HabitEntry.COLUMN_TIME + " - " +
                        HabitEntry.COLUMN_COMMENT + "\n");

                // Figure out the index of each column
                int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
                int dateColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_DATE);
                int habitColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT);
                int typeColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_TYPE);
                int timeColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_TIME);
                int commentColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_COMMENT);

                // Iterate through all the returned rows in the cursor
                while (cursor.moveToNext()) {
                    // Use that index to extract the String or Int value of the word
                    // at the current row the cursor is on.
                    int currentID = cursor.getInt(idColumnIndex);
                    String currentDate = cursor.getString(dateColumnIndex);
                    String currentHabit = cursor.getString(habitColumnIndex);
                    int currentType = cursor.getInt(typeColumnIndex);
                    int currentTime = cursor.getInt(timeColumnIndex);
                    String currentComment = cursor.getString(commentColumnIndex);
                    // Display the values from each column of the current row in the cursor in the TextView
                    displayView.append(("\n" + currentID + " - " +
                            currentDate + " - " +
                            currentHabit + " - " +
                            currentType + " - " +
                            currentTime + " - " +
                            currentComment));
                }
            } finally {
                // Always close the cursor when you're done reading from it. This releases all its
                // resources and makes it invalid.
                cursor.close();
            }
            Cursor habitRecord = mDbHelper.getRecord(1);
            Log.i("getRecord ", habitRecord.toString());
        }

        /**
         * Helper method to insert hardcoded habit data into the database. For debugging purposes only.
         */
        private void insertHabit() {
            // Gets the database in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            // Create a ContentValues object where column names are the keys,
            // and habit attributes are the values.
            ContentValues values = new ContentValues();
            values.put(HabitEntry.COLUMN_DATE, "2017, 04, 01");
            values.put(HabitEntry.COLUMN_HABIT, "Coding");
            values.put(HabitEntry.COLUMN_TYPE, HabitEntry.TYPE_ENRICHMENT);
            values.put(HabitEntry.COLUMN_TIME, 1);
            values.put(HabitEntry.COLUMN_COMMENT, "Groovy");

            // Insert a new row for Toto in the database, returning the ID of that new row.
            // The first argument for db.insert() is the habits table name.
            // The second argument provides the name of a column in which the framework
            // can insert NULL in the event that the ContentValues is empty (if
            // this is set to "null", then the framework will not insert a row when
            // there are no values).
            // The third argument is the ContentValues object containing the info for Toto.
            long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu options from the res/menu/menu_catalog.xml file.
            // This adds menu items to the app bar.
            getMenuInflater().inflate(R.menu.menu_catalog, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // User clicked on a menu option in the app bar overflow menu
            switch (item.getItemId()) {
                // Respond to a click on the "Insert dummy data" menu option
                case R.id.action_insert_dummy_data:
                    insertHabit();
                    displayDatabaseInfo();
                    return true;
                // Respond to a click on the "Delete all entries" menu option
                case R.id.action_delete_all_entries:
                    // Do nothing for now
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }