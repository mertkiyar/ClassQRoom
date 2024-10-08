package com.example.classqroom;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_USER_NAME = "USER_NAME";
    public static final String COLUMN_USER_SURNAME = "USER_SURNAME";
    public static final String COLUMN_USER_EMAIL = "USER_EMAIL";
    public static final String COLUMN_USER_NUMBER = "USER_NUMBER";
    public static final String COLUMN_USER_PASSWORD = "USER_PASSWORD";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + USER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_NAME + " TEXT, " + COLUMN_USER_SURNAME + " TEXT, " + COLUMN_USER_EMAIL + " TEXT UNIQUE, " + COLUMN_USER_NUMBER + " INT, " + COLUMN_USER_PASSWORD + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    public boolean addNewUser(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        try {
            cv.put(COLUMN_USER_NAME, userModel.getName());
            cv.put(COLUMN_USER_SURNAME, userModel.getSurname());
            cv.put(COLUMN_USER_EMAIL, userModel.getEmail());
            cv.put(COLUMN_USER_NUMBER, userModel.getStudentNumber());
            cv.put(COLUMN_USER_PASSWORD, userModel.getPassword());

            long insert = db.insert(USER_TABLE, null, cv);
            return insert != -1;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        boolean isAvailable = false;

        try {
            cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USER_EMAIL + " = ?", new String[]{email});
            if (cursor != null && cursor.moveToFirst()) {
                isAvailable = true;
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return isAvailable;
    }

    public boolean authenticateUser(String email, String inputPassword) {
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_PASSWORD + " FROM " + USER_TABLE + " WHERE " + COLUMN_USER_EMAIL + " = ?", new String[]{email})) {

            if (cursor != null && cursor.moveToFirst()) {
                String storedPassword = cursor.getString(0);
                Log.d("RegisterActivity", storedPassword);
                Log.d("RegisterActivity", inputPassword);
                return storedPassword.equals(inputPassword);
            }
        }
        return false;
    }
}
