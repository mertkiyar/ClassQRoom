package com.example.classqroom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_UUID = "UUID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_SURNAME = "SURNAME";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_USER_TYPE = "USER_TYPE";

    public static final String STUDENT_TABLE = "STUDENT_TABLE";
    public static final String COLUMN_STUDENT_NUMBER = "STUDENT_NUMBER";
    public static final String COLUMN_STUDENT_DEPARTMENT_ID = "STUDENT_DEPARTMENT_ID";
    public static final String COLUMN_STUDENT_GRADE = "STUDENT_GRADE";

    public static final String LECTURER_TABLE = "LECTURER_TABLE";
    public static final String COLUMN_LECTURER_DEPARTMENT_ID = "LECTURER_DEPARTMENT_ID";

    public static final String DEPARTMENT_TABLE = "DEPARTMENT_TABLE";
    public static final String COLUMN_DEPARTMENT_ID = "DEPARTMENT_ID";
    public static final String COLUMN_DEPARTMENT_NAME = "DEPARTMENT_NAME";
    public static final String COLUMN_DEPARTMENT_LANGUAGE = "DEPARTMENT_LANGUAGE";

    public static final String LECTURE_TABLE = "LECTURE_TABLE";
    public static final String COLUMN_LECTURE_ID = "LECTURE_ID";
    public static final String COLUMN_LECTURE_NAME = "LECTURE_NAME";
    public static final String COLUMN_LECTURE_CODE = "LECTURE_CODE";
    public static final String COLUMN_LECTURE_LECTURER = "LECTURE_LECTURER";
    public static final String COLUMN_LECTURE_LANGUAGE = "LECTURE_LANGUAGE";
    public static final String COLUMN_LECTURE_DEPARTMENT_ID = "LECTURE_DEPARTMENT";
    public static final String COLUMN_LECTURE_TYPE = "LECTURE_TYPE";
    public static final String COLUMN_LECTURE_ACTS = "LECTURE_ACTS";
    public static final String COLUMN_LECTURE_CREDIT = "LECTURE_CREDIT";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTableStatement = "CREATE TABLE " + USER_TABLE + " ("
                + COLUMN_UUID + " UUID PRIMARY KEY UNIQUE, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_SURNAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_USER_TYPE + " TEXT)";
        db.execSQL(createUserTableStatement);

        String createStudentTableStatement = "CREATE TABLE " + STUDENT_TABLE + " ("
                + COLUMN_UUID + " TEXT PRIMARY KEY UNIQUE, "
                + COLUMN_STUDENT_NUMBER + " TEXT UNIQUE, "
                + COLUMN_STUDENT_DEPARTMENT_ID + " INT, "
                + COLUMN_STUDENT_GRADE + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_UUID + ") REFERENCES " + USER_TABLE + "(" + COLUMN_UUID + "), "
                + "FOREIGN KEY(" + COLUMN_STUDENT_DEPARTMENT_ID + ") REFERENCES " + DEPARTMENT_TABLE + "(" + COLUMN_DEPARTMENT_ID + "))";
        db.execSQL(createStudentTableStatement);

        String createLecturerTableStatement = "CREATE TABLE " + LECTURER_TABLE + " ("
                + COLUMN_UUID + " TEXT PRIMARY KEY UNIQUE, "
                + COLUMN_LECTURER_DEPARTMENT_ID + " INT, "
                + "FOREIGN KEY(" + COLUMN_UUID + ") REFERENCES " + USER_TABLE + "(" + COLUMN_UUID + "), "
                + "FOREIGN KEY(" + COLUMN_LECTURER_DEPARTMENT_ID + ") REFERENCES " + DEPARTMENT_TABLE + "(" + COLUMN_DEPARTMENT_ID + "))";
        db.execSQL(createLecturerTableStatement);

        String createDepartmentTableStatement = "CREATE TABLE " + DEPARTMENT_TABLE + " ("
                + COLUMN_DEPARTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DEPARTMENT_NAME + " TEXT UNIQUE, "
                + COLUMN_DEPARTMENT_LANGUAGE + " TEXT)";
        db.execSQL(createDepartmentTableStatement);

        String createLectureTableStatement = "CREATE TABLE " + LECTURE_TABLE + " ("
                + COLUMN_LECTURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LECTURE_NAME + " TEXT UNIQUE, "
                + COLUMN_LECTURE_CODE + " TEXT UNIQUE, "
                + COLUMN_LECTURE_LECTURER + " TEXT, "
                + COLUMN_LECTURE_LANGUAGE + " TEXT, "
                + COLUMN_LECTURE_DEPARTMENT_ID + " INT, "
                + COLUMN_LECTURE_TYPE + " TEXT, "
                + COLUMN_LECTURE_ACTS + " INT, "
                + COLUMN_LECTURE_CREDIT + " INT, "
                + "FOREIGN KEY(" + COLUMN_LECTURE_DEPARTMENT_ID + ") REFERENCES " + DEPARTMENT_TABLE + "(" + COLUMN_DEPARTMENT_ID + "))";
        db.execSQL(createLectureTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LECTURER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DEPARTMENT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LECTURE_TABLE);
            onCreate(db);
        }
    }

    public boolean addNewUser(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_UUID, userModel.getUuid().toString());
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

        cv.put(COLUMN_UUID, studentModel.getUuid().toString());
        cv.put(COLUMN_STUDENT_NUMBER, studentModel.getStudentNumber());
        cv.put(COLUMN_STUDENT_DEPARTMENT_ID, studentModel.getStudentDepartmentId());
        cv.put(COLUMN_STUDENT_GRADE, studentModel.getGrade());

        long insert = db.insert(STUDENT_TABLE, null, cv);
        return insert != -1;
    }

    public boolean addNewLecturer(LecturerModel lecturerModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_UUID, lecturerModel.getUuid().toString());
        cv.put(COLUMN_LECTURER_DEPARTMENT_ID, lecturerModel.getLecturerDepartmentId());

        long insert = db.insert(LECTURER_TABLE, null, cv);
        return insert != -1;
    }
    public boolean addNewDepartment(DepartmentModel departmentModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DEPARTMENT_ID, departmentModel.getDepartmentId());
        cv.put(COLUMN_DEPARTMENT_NAME, departmentModel.getDepartmentName());
        cv.put(COLUMN_DEPARTMENT_LANGUAGE, departmentModel.getDepartmentLanguage());

        long insert = db.insert(DEPARTMENT_TABLE, null, cv);
        return insert != -1;
    }
    public boolean addNewLecture(LectureModel lectureModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LECTURE_ID, lectureModel.getLectureId());
        cv.put(COLUMN_LECTURE_NAME, lectureModel.getLectureName());
        cv.put(COLUMN_LECTURE_CODE, lectureModel.getLectureCode());
        cv.put(COLUMN_LECTURE_LECTURER, lectureModel.getLecturer());
        cv.put(COLUMN_LECTURE_LANGUAGE, lectureModel.getLectureLanguage());
        cv.put(COLUMN_LECTURE_DEPARTMENT_ID, lectureModel.getLectureDepartmentId());
        cv.put(COLUMN_LECTURE_TYPE, lectureModel.getLectureType());
        cv.put(COLUMN_LECTURE_ACTS, lectureModel.getActs());
        cv.put(COLUMN_LECTURE_CREDIT, lectureModel.getCredit());

        long insert = db.insert(LECTURE_TABLE, null, cv);
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
                String storedPasswordHash = cursor.getString(0);
                return storedPasswordHash.equals(hashPassword(inputPassword));
            }
        }
        return false;
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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