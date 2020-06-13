package com.example.todo;

import android.provider.BaseColumns;

public class TaskContract {
    private TaskContract() {
    }

    public static final class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "taskList";
        public static final String COLUMN_NAME = "taskname";
        public static final String COLUMN_TIMESTAMP = "timeStamp";
        public static final String DATE = "date";
        public static final String STATE = "state";

    }
}
