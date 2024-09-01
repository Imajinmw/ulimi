package com.rodz.ulimi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydb.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY AUTOINCREMENT, webid VARCHAR, name VARCHAR, dob VARCHAR, district VARCHAR, gender TEXT, phone TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS settings (name VARCHAR, value VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS notifications (id INTEGER PRIMARY KEY AUTOINCREMENT, webid VARCHAR, type VARCHAR, content VARCHAR, date VARCHAR, status TEXT, refer VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS items (id INTEGER PRIMARY KEY AUTOINCREMENT, webid VARCHAR, name VARCHAR, description VARCHAR, picture VARCHAR, type TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS environment (id INTEGER PRIMARY KEY AUTOINCREMENT, webid VARCHAR, item VARCHAR, title VARCHAR, description VARCHAR, user TEXT, time TEXT, status TEXT, picture TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS activities (id INTEGER PRIMARY KEY AUTOINCREMENT, webid VARCHAR, item VARCHAR, title VARCHAR, description VARCHAR, user TEXT, time TEXT, status TEXT, picture TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, webid VARCHAR, name VARCHAR, email VARCHAR, password VARCHAR, status TEXT, type TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS selected (id INTEGER PRIMARY KEY AUTOINCREMENT, item VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}