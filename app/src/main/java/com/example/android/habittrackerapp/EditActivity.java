package com.example.android.habittrackerapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.habittrackerapp.data.DbHelper;
import com.example.android.habittrackerapp.data.HabitContract.HabitEntry;

/**
 * Created by mjyatco on 8/25/17.
 */

public class EditActivity extends AppCompatActivity {

    /**
     * EditText field to enter the habit's date
     */
    private EditText mDateEditText;

    /**
     * EditText field to enter the habit's name
     */
    private EditText mHabitEditText;

    /**
     * EditText field to enter the habit's type
     */
    private Spinner mTypeSpinner;

    /**
     * EditText field to enter the habit's time
     */
    private Spinner mTimeSpinner;

    /**
     * EditText field to enter the habit's commentary
     */
    private EditText mCommentEditText;

    private int mType = HabitEntry.TYPE_ENRICHMENT;

    private int mTime = HabitEntry.TIME_EVENING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Find all relevant views that we will need to read user input from
        mDateEditText = (EditText) findViewById(R.id.edit_habit_date);
        mHabitEditText = (EditText) findViewById(R.id.edit_habit_habit);
        mTypeSpinner = (Spinner) findViewById(R.id.spinner_type);
        mTimeSpinner = (Spinner) findViewById(R.id.spinner_time);
        mCommentEditText = (EditText) findViewById(R.id.edit_habit_comment);

        setupTypeSpinner();
        setupTimeSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the type of habit
     */
    private void setupTypeSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter typeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_type_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mTypeSpinner.setAdapter(typeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.type_fitness))) {
                        mType = HabitEntry.TYPE_FITNESS;
                    } else if (selection.equals(getString(R.string.type_leisure))) {
                        mType = HabitEntry.TYPE_LEISURE;
                    } else{
                        mType = HabitEntry.TYPE_ENRICHMENT;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mType = HabitEntry.TYPE_ENRICHMENT;
            }
        });
    }

    private void setupTimeSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter timeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_time_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        timeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mTimeSpinner.setAdapter(timeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.time_morning))) {
                        mType = HabitEntry.TIME_MORNING;
                    } else if (selection.equals(getString(R.string.time_afternoon))) {
                        mType = HabitEntry.TIME_AFTERNOON;
                    } else{
                        mType = HabitEntry.TIME_EVENING;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mType = HabitEntry.TYPE_ENRICHMENT;
            }
        });
    }


    /**
     * Get user input from editor and save new habit into database.
     */
    private void insertHabit() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String dateString = mDateEditText.getText().toString().trim();
        String habitString = mHabitEditText.getText().toString().trim();
        String commentString = mCommentEditText.getText().toString().trim();

        // Create database helper
        DbHelper mDbHelper = new DbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and habit attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_DATE, dateString);
        values.put(HabitEntry.COLUMN_HABIT, habitString);
        values.put(HabitEntry.COLUMN_TYPE, mType);
        values.put(HabitEntry.COLUMN_TIME, mTime);
        values.put(HabitEntry.COLUMN_COMMENT, commentString);

        // Insert a new row for habit in the database, returning the ID of that new row.
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving habit", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save habit to database
                insertHabit();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

