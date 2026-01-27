package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHealper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "information.db";
    public static final String TABLE_NAME = "NameEmail";
    public static final String USER_ID = "ID";
    public static final String USER_NAME = "NAME";
    public static final String USER_EMAIL = "EMAIL";

    public DatabaseHealper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, EMAIL NEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }


    public Cursor getDataById(String id) {
        return getReadableDatabase().rawQuery(
                "SELECT * FROM "+TABLE_NAME+" WHERE id = ?",
                new String[]{id}
        );
    }

    public boolean insertData(String name, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, name);
        contentValues.put(USER_EMAIL, email);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }

    public boolean updataData(String id, String name, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, id);
        contentValues.put(USER_NAME, name);
        contentValues.put(USER_EMAIL, email);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{ id } );
        return true;
    }



    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

    public Cursor readAllData() {
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            return cursor;
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Ошибка чтения", e);
            return null;
        }
    }

}

