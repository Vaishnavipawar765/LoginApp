package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "MyLoginApp";
    private static final int VERSION = 2;
    public UserDBHelper(@Nullable Context context) {
        super(context,DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Users( name  TEXT ,username TEXT  PRIMARY KEY , password TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(sqLiteDatabase);

    }

    public boolean userExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username=?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public  boolean registerUser(String name,String username,String password){
        if (userExists(username)) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("username", username);
        values.put("password",password);
        long result = db.insert("Users", null, values);
        db.close();

        return  result != -1;
    }
    public String getPassword(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password from Users WHERE username=?",new String[]{username});
        String password = null;
        if (cursor.moveToFirst()){
            password = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return password;
    }
    public String getNameByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM Users WHERE username=?", new String[]{username});
        String name = null;
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return name;
    }
}
