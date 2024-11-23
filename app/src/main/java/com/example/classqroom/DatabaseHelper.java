package com.example.classqroom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String USER_TABLE = "USER_TABLE";
    public static final String STUDENT_TABLE = "STUDENT_TABLE";
    public static final String LECTURER_TABLE = "LECTURER_TABLE";
    public static final String COLUMN_UUID = "UUID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_SURNAME = "SURNAME";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_USER_TYPE = "USER_TYPE";

    public static final String COLUMN_STUDENT_NUMBER = "STUDENT_NUMBER";
    public static final String COLUMN_DEPARTMENT = "DEPARTMENT";
    public static final String COLUMN_STUDENT_GRADE = "STUDENT_GRADE";
    public static final String COLUMN_LECTURER_LECTURE = "LECTURER_LECTURE";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTableStatement = "CREATE TABLE " + USER_TABLE + " ("
                + COLUMN_UUID + " TEXT PRIMARY KEY UNIQUE, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_SURNAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_USER_TYPE + " TEXT)";
        db.execSQL(createUserTableStatement);

        String createStudentTableStatement = "CREATE TABLE " + STUDENT_TABLE + " ("
                + COLUMN_UUID + " TEXT PRIMARY KEY UNIQUE, "
                + COLUMN_STUDENT_NUMBER + " TEXT UNIQUE, "
                + COLUMN_DEPARTMENT + " TEXT, "
                + COLUMN_STUDENT_GRADE + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_UUID + ") REFERENCES " + USER_TABLE + "(" + COLUMN_UUID + "))";
        db.execSQL(createStudentTableStatement);

        String createLecturerTableStatement = "CREATE TABLE " + LECTURER_TABLE + " ("
                + COLUMN_UUID + " TEXT PRIMARY KEY UNIQUE, "
                + COLUMN_DEPARTMENT + " TEXT, "
                + COLUMN_LECTURER_LECTURE + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_UUID + ") REFERENCES " + USER_TABLE + "(" + COLUMN_UUID + "))";
        db.execSQL(createLecturerTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LECTURER_TABLE);
            onCreate(db);
        }
    }

    public boolean addNewUser(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_UUID, userModel.getUuid());
        cv.put(COLUMN_NAME, userModel.getName());
        cv.put(COLUMN_SURNAME, userModel.getSurname());
        cv.put(COLUMN_EMAIL, userModel.getEmail());
        cv.put(COLUMN_PASSWORD, userModel.getPassword());
        cv.put(COLUMN_USER_TYPE, userModel.getType());

        long insert = db.insert(USER_TABLE, null, cv);
        return insert != -1;
    }

    public boolean addNewStudent(StudentModel studentModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_UUID, studentModel.getUuid());
        cv.put(COLUMN_STUDENT_NUMBER, studentModel.getNumber());
        cv.put(COLUMN_DEPARTMENT, studentModel.getDepartment());
        cv.put(COLUMN_STUDENT_GRADE, studentModel.getGrade());

        long insert = db.insert(STUDENT_TABLE, null, cv);
        return insert != -1;
    }

    public boolean addNewLecturer(int uuid, String department, String lecture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_UUID, uuid);
        cv.put(COLUMN_DEPARTMENT, department);
        cv.put(COLUMN_LECTURER_LECTURE, lecture);

        long insert = db.insert(LECTURER_TABLE, null, cv);
        return insert != -1;
    }

    public boolean checkUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT 1 FROM " + USER_TABLE + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email})) {
            return cursor != null && cursor.moveToFirst();
        }
    }

    public boolean authenticateUser(String email, String inputPassword) {
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_PASSWORD + " FROM " + USER_TABLE + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email})) {

            if (cursor != null && cursor.moveToFirst()) {
                String storedPassword = cursor.getString(0);
                return storedPassword.equals(inputPassword);
            }
        }
        return false;
    }
    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_NAME + " FROM " + USER_TABLE + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email})) {

            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        }
        return null;
    }

    public String getUserType(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_USER_TYPE + " FROM " + USER_TABLE + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email})) {

            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        }
        return null;
    }
}