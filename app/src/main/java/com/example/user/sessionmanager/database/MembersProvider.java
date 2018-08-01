package com.example.user.sessionmanager.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by User on 2018/4/2.
 */

public class MembersProvider extends ContentProvider {

    public static final int MENBERS = 100;
    public static final int MEMBER_WITH_ID = 101;

    private MembersDatabaseHelper mMembersDatabaseHelper;

    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MembersContract.CONTENT_AUTHORITY, MembersContract.TABLE_NAME, MENBERS);
        uriMatcher.addURI(MembersContract.CONTENT_AUTHORITY,MembersContract.TABLE_NAME + "/#" ,MEMBER_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMembersDatabaseHelper = new MembersDatabaseHelper(context);
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase readableDatabase = mMembersDatabaseHelper.getReadableDatabase();
        int matchDirectory = URI_MATCHER.match(uri);
        Cursor returnCursor;

        if(matchDirectory == MENBERS){
            returnCursor = readableDatabase.query(
                MembersContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        }else {
            returnCursor = null;
        }

        return returnCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase writableDatabase = mMembersDatabaseHelper.getWritableDatabase();
        int matchDirectory = URI_MATCHER.match(uri);
        Uri returnUri;

        if(matchDirectory == MENBERS){
            long _id = writableDatabase.insert(MembersContract.TABLE_NAME, null, values);
            if(_id > 0){
                returnUri = ContentUris.withAppendedId(MembersContract.CONTENT_URI, _id);
            }else {
                returnUri = null;
            }
        }else {
            returnUri = null;
        }

        ContentResolver contentResolver = getContext().getContentResolver();

        if(contentResolver != null){
            contentResolver.notifyChange(uri, null);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase writableDatabase = mMembersDatabaseHelper.getWritableDatabase();
        int matchItem = URI_MATCHER.match(uri);
        int membersDeletedCounts;

        if(matchItem == MEMBER_WITH_ID){
            long _id = ContentUris.parseId(uri);
            selection = String.format("%s = ?", MembersContract.MembersEntry._ID);
            selectionArgs = new String[]{String.valueOf(_id)};
            membersDeletedCounts = writableDatabase.delete(MembersContract.TABLE_NAME, selection, selectionArgs);
        }else {
            membersDeletedCounts = 0;
        }

        ContentResolver contentResolver = getContext().getContentResolver();

        if(contentResolver != null){
            contentResolver.notifyChange(uri, null);
        }

        return membersDeletedCounts;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
