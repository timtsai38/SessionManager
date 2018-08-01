package com.example.user.sessionmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 2018/4/2.
 */

public class MembersDatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "members.db";
    private static final int DATABASE_VERSION = 1;

    public MembersDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private final String SQL_CREATE_TABLE = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s TEXT)",
            MembersContract.TABLE_NAME,
            MembersContract.MembersEntry._ID,
            MembersContract.MembersEntry.COLUMN_NAME,
            MembersContract.MembersEntry.COLUMN_AGE,
            MembersContract.MembersEntry.COLUMN_GENDER);

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MembersContract.TABLE_NAME);
        onCreate(db);
    }
}
