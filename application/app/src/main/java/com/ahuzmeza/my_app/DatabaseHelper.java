package com.ahuzmeza.my_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users "    +
                "("                         +
                "id INTEGER PRIMARY KEY,"   +
                " username text NOT NULL"   +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //  class definition of the 'users' model class.
    public class User {
        private long id;
        private String username;
    }
}
