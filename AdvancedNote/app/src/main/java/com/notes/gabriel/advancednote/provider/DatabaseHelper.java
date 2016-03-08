package com.notes.gabriel.advancednote.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ysuarez on 3/3/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG= "NotesContentProvider";
    private String key=null;
    public DatabaseHelper(Context context, String DATABASE_NAME,
                          SQLiteDatabase.CursorFactory factory,
                          int DATABASE_VERSION) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + AccessProvider.NOTES_TABLE + " (" + AccessProvider.Columnas.ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + AccessProvider.Columnas.DATE +
                " VARCHAR(10)," + AccessProvider.Columnas.TITLE +
                " VARCHAR(255)," + AccessProvider.Columnas.BODY +
                " VARCHAR(255)" + " );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + AccessProvider.NOTES_TABLE);
        onCreate(db);
    }
}
