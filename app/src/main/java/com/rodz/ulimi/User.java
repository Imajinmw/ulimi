package com.rodz.ulimi;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class User {
    SQLiteDatabase db;
    public boolean status = false;
    public String link = "http://169.254.65.21/songs/";
    public String id = "0";
    public String name  = "";
    public String dob = "";
    public String gender = "";
    public String district = "";
    public String phone = "";

    @SuppressLint("Range")
    public User(SQLiteDatabase db){
        this.db = db;

        Cursor cursor = db.rawQuery("SELECT * FROM user", null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            status = true;

            id = cursor.getString(cursor.getColumnIndex("webid"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            dob = cursor.getString(cursor.getColumnIndex("dob"));
            gender = cursor.getString(cursor.getColumnIndex("gender"));
            district = cursor.getString(cursor.getColumnIndex("gender"));
            phone = cursor.getString(cursor.getColumnIndex("phone"));
        }
    }

    public void setName(String name){
        ContentValues cv = new ContentValues();
        cv.put("name", name);

        db.update("user", cv, "webid = ?", new String[]{id});
        this.name = name;
    }
/*
    public void setEmail(String email){
        ContentValues cv = new ContentValues();
        cv.put("email", email);

        db.update("user", cv, "webid = ?", new String[]{id});
        this.email = email;
    }

    public void setPhone(String phone){
        ContentValues cv = new ContentValues();
        cv.put("phone", phone);

        db.update("user", cv, "webid = ?", new String[]{id});
        this.phone = phone;
    }*/
}
