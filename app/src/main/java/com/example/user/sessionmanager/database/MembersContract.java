package com.example.user.sessionmanager.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by User on 2018/4/2.
 */

public class MembersContract {

    public static final String TABLE_NAME = "members";

    public static final String CONTENT_AUTHORITY = "com.example.user.sessionmanager";

    public static final Uri CONTENT_URI = new Uri.Builder()
            .scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();

    public static abstract class MembersEntry implements BaseColumns{
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_GENDER = "gender";
    }
}
