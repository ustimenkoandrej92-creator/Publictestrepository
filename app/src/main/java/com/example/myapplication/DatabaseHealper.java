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
    public static final String TABLE_NAME_2 = "Defects";
    public static final String USER_ID = "ID";
    public static final String USER_ID2 = "ID2";
    public static final String USER_NAME = "NAME";
    public static final String USER_EMAIL = "EMAIL";
    public static final String TYPE_OF_DEFECT = "TYPE";
    public static final String PLACE_OF_DEFECT = "PLACE";

    public DatabaseHealper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        android.util.Log.d("DB_CREATE", "onCreate CALLED");

        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +  USER_NAME + " TEXT, " + USER_EMAIL + " TEXT);");
        db.execSQL("create table " + TABLE_NAME_2 + " (ID2 INTEGER PRIMARY KEY AUTOINCREMENT, " +  TYPE_OF_DEFECT + " TEXT, " + PLACE_OF_DEFECT + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
    }

    public Cursor getDataById(String id) {                                          //1
        return getReadableDatabase().rawQuery(
                "SELECT * FROM "+TABLE_NAME+" WHERE id = ?",
                new String[]{id}
        );
    }

    public Cursor getDataById2(String id) {                                         //2
        return getReadableDatabase().rawQuery(
                "SELECT * FROM "+TABLE_NAME_2+" WHERE id2 = ?",
                new String[]{id}
        );
    }

    public boolean insertData(String name, String email){                           //1
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

    public boolean insertData2(String type, String place){                          //2
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE_OF_DEFECT, type);
        contentValues.put(PLACE_OF_DEFECT, place);
        long result = db.insert(TABLE_NAME_2, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllData(){                                                     //1
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }

    public Cursor getAllData2(){                                                    //2
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_2, null);
        return res;
    }

    public boolean updataData(String id, String name, String email){                //1
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, id);
        contentValues.put(USER_NAME, name);
        contentValues.put(USER_EMAIL, email);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{ id } );
        return true;
    }

    public boolean updataData2(String id, String type, String place){               //2
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID2, id);
        contentValues.put(TYPE_OF_DEFECT, type);
        contentValues.put(PLACE_OF_DEFECT, place);

        db.update(TABLE_NAME_2, contentValues, "ID2 = ?", new String[]{ id } );
        return true;
    }



    public Integer deleteData(String id){                                           //1
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

    public Integer deleteData2(String id){                                          //2
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_2, "ID2 = ?", new String[] {id});
    }

    public Cursor readAllData() {                                                   //1
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            return cursor;
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Ошибка чтения", e);
            return null;
        }
    }

    public Cursor readAllData2() {                                                  //2
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_2, null);
            return cursor;
        } catch (Exception e) {
            android.util.Log.e("DatabaseHelper", "Ошибка чтения", e);
            return null;
        }
    }

}