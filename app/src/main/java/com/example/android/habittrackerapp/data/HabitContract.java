package com.example.android.habittrackerapp.data;

import android.provider.BaseColumns;

/**
 * Created by mjyatco on 8/25/17.
 */

public final class HabitContract {

    public HabitContract() {}

    public class HabitEntry implements BaseColumns {

        public final static String TABLE_NAME = "habits";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_HABIT = "activity";
        public final static String COLUMN_TYPE = "type";
        public final static String COLUMN_TIME = "time";
        public final static String COLUMN_COMMENT = "commentary";

        public final static int TIME_MORNING = 0;
        public final static int TIME_AFTERNOON = 1;
        public final static int TIME_EVENING = 2;

        public final static int TYPE_ENRICHMENT = 0;
        public final static int TYPE_LEISURE= 1;
        public final static int TYPE_FITNESS = 2;
    }

}

