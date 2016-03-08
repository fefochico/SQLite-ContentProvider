package com.notes.gabriel.advancednote.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by ysuarez on 3/3/16.
 */
public class NotesContentProvider extends ContentProvider {

    //Nombre de la base de datos
    private static final String DATABASE_NAME= "notes.db";
    //Version de la base de datos
    private static final int DATABASE_VERSION= 1;

    private DatabaseHelper dbHelper;

    private String key;

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(AccessProvider.NOTES_TABLE);

        switch(AccessProvider.uriMatcher.match(uri)){
            case AccessProvider.ALLROWS:
               break;
            case AccessProvider.SINGLE_ROW:
                selection = selection + "id = " + uri.getLastPathSegment();
            default:
                throw  new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (AccessProvider.uriMatcher.match(uri)){
            case AccessProvider.ALLROWS:
                return AccessProvider.MULTIPLE_MIME;
            case AccessProvider.SINGLE_ROW:
                return AccessProvider.SINGLE_MIME;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if(AccessProvider.uriMatcher.match(uri) != AccessProvider.ALLROWS){
            throw new IllegalArgumentException("Unknown URI" + uri);
        }

        ContentValues values;
        if(initialValues!= null) {
            values = new ContentValues(initialValues);
        }else{
            values = new ContentValues();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId= db.insert(AccessProvider.NOTES_TABLE, AccessProvider.Columnas.BODY, values);
        if(rowId>0){
     /*c*/  Uri noteUri= ContentUris.withAppendedId(Uri.parse(AccessProvider.MULTIPLE_MIME), rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        try {
            throw new SQLException("Failed to insert row into " + uri);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        switch (AccessProvider.uriMatcher.match(uri)){
            case AccessProvider.ALLROWS:
                break;
            case AccessProvider.SINGLE_ROW:
                selection = "id = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        int count = db.delete(AccessProvider.NOTES_TABLE, selection, selectionArgs);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (AccessProvider.uriMatcher.match(uri)){
            case AccessProvider.ALLROWS:
                count = db.update(AccessProvider.NOTES_TABLE, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
