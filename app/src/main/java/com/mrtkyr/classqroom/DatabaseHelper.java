package com.mrtkyr.classqroom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.annotation.Nullable;

import com.mrtkyr.classqroom.model.DepartmentModel;
import com.mrtkyr.classqroom.model.LectureModel;
import com.mrtkyr.classqroom.model.LecturerModel;
import com.mrtkyr.classqroom.model.StudentModel;
import com.mrtkyr.classqroom.model.UserModel;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_UUID = "UUID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_SURNAME = "SURNAME";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_USER_DEPARTMENT_ID = "DEPARTMENT_ID";
    public static final String COLUMN_USER_TYPE = "TYPE";
    public static final String COLUMN_USER_CREATION_TIME = "CREATION_TIME";
    public static final String COLUMN_USER_LAST_LOGIN = "LAST_LOGIN";

    public static final String STUDENT_TABLE = "STUDENT_TABLE";
    public static final String COLUMN_STUDENT_NUMBER = "NUMBER";
    public static final String COLUMN_STUDENT_GRADE = "GRADE";
    public static final String COLUMN_STUDENT_IS_IN_CAMPUS = "IS_IN_CAMPUS";

    public static final String LECTURER_TABLE = "LECTURER_TABLE";
    public static final String COLUMN_LECTURER_DEPARTMENT_ID = "DEPARTMENT_ID";
    public static final String COLUMN_LECTURER_IN_LECTURE = "IN_LECTURE";

    public static final String DEPARTMENT_TABLE = "DEPARTMENT_TABLE";
    public static final String COLUMN_DEPARTMENT_ID = "ID";
    public static final String COLUMN_DEPARTMENT_NAME = "NAME";
    public static final String COLUMN_DEPARTMENT_LANGUAGE = "LANGUAGE";

    public static final String LECTURE_TABLE = "LECTURE_TABLE";
    public static final String COLUMN_LECTURE_ID = "ID";
    public static final String COLUMN_LECTURE_NAME = "NAME";
    public static final String COLUMN_LECTURE_CODE = "CODE";
    public static final String COLUMN_LECTURE_LECTURER_UUID = "LECTURER_UUID";
    public static final String COLUMN_LECTURE_LANGUAGE = "LANGUAGE";
    public static final String COLUMN_LECTURE_DEPARTMENT_ID = "DEPARTMENT";
    public static final String COLUMN_LECTURE_TYPE = "TYPE";
    public static final String COLUMN_LECTURE_ACTS = "ACTS";
    public static final String COLUMN_LECTURE_CREDIT = "CREDIT";

    public static final String ATTENDANCE_TABLE = "ATTENDANCE_TABLE";
    public static final String COLUMN_ATTENDANCE_ID = "ID";
    public static final String COLUMN_ATTENDANCE_STUDENT_ID = "STUDENT_ID";
    public static final String COLUMN_ATTENDANCE_QR_ID = "QR_ID";
    public static final String COLUMN_ATTENDANCE_LECTURE_ID = "LECTURE_ID";
    public static final String COLUMN_ATTENDANCE_STATUS = "STATUS";
    public static final String COLUMN_ATTENDANCE_SCANNED_AT = "SCANNED_AT";


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
                + COLUMN_USER_DEPARTMENT_ID + " INTEGER, "
                + COLUMN_USER_TYPE + " TEXT, "
                + COLUMN_USER_CREATION_TIME + " TEXT, "
                + COLUMN_USER_LAST_LOGIN + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_USER_DEPARTMENT_ID + ") REFERENCES " + DEPARTMENT_TABLE + "(" + COLUMN_DEPARTMENT_ID + "))";
        db.execSQL(createUserTableStatement);

        String createStudentTableStatement = "CREATE TABLE " + STUDENT_TABLE + " ("
                + COLUMN_UUID + " TEXT PRIMARY KEY UNIQUE, "
                + COLUMN_STUDENT_NUMBER + " TEXT UNIQUE, "
                + COLUMN_STUDENT_GRADE + " TEXT, "
                + COLUMN_STUDENT_IS_IN_CAMPUS + " BOOLEAN, "
                + "FOREIGN KEY(" + COLUMN_UUID + ") REFERENCES " + USER_TABLE + "(" + COLUMN_UUID + "))";
        db.execSQL(createStudentTableStatement);

        String createLecturerTableStatement = "CREATE TABLE " + LECTURER_TABLE + " ("
                + COLUMN_UUID + " TEXT PRIMARY KEY UNIQUE, "
                + COLUMN_LECTURER_DEPARTMENT_ID + " INTEGER, "
                + COLUMN_LECTURER_IN_LECTURE + " BOOLEAN, "
                + "FOREIGN KEY(" + COLUMN_UUID + ") REFERENCES " + USER_TABLE + "(" + COLUMN_UUID + "))";
        db.execSQL(createLecturerTableStatement);

        String createDepartmentTableStatement = "CREATE TABLE " + DEPARTMENT_TABLE + " ("
                + COLUMN_DEPARTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DEPARTMENT_NAME + " TEXT, "
                + COLUMN_DEPARTMENT_LANGUAGE + " TEXT)";
        db.execSQL(createDepartmentTableStatement);

        String createLectureTableStatement = "CREATE TABLE " + LECTURE_TABLE + " ("
                + COLUMN_LECTURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LECTURE_NAME + " TEXT UNIQUE, "
                + COLUMN_LECTURE_CODE + " TEXT UNIQUE, "
                + COLUMN_LECTURE_LECTURER_UUID + " TEXT, "
                + COLUMN_LECTURE_LANGUAGE + " TEXT, "
                + COLUMN_LECTURE_DEPARTMENT_ID + " INTEGER, "
                + COLUMN_LECTURE_TYPE + " TEXT, "
                + COLUMN_LECTURE_ACTS + " INTEGER, "
                + COLUMN_LECTURE_CREDIT + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_LECTURE_DEPARTMENT_ID + ") REFERENCES " + DEPARTMENT_TABLE + "(" + COLUMN_DEPARTMENT_ID + "))";
        db.execSQL(createLectureTableStatement);

        String createAttendanceTableStatement = "CREATE TABLE " + ATTENDANCE_TABLE + " ("
                + COLUMN_ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ATTENDANCE_STUDENT_ID + " TEXT, "
                + COLUMN_ATTENDANCE_QR_ID + " INTEGER, "
                + COLUMN_ATTENDANCE_LECTURE_ID + " TEXT, "
                + COLUMN_ATTENDANCE_STATUS + " TEXT, "
                + COLUMN_ATTENDANCE_SCANNED_AT + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_ATTENDANCE_STUDENT_ID + ") REFERENCES " + STUDENT_TABLE + "(" + COLUMN_STUDENT_NUMBER + "), "
                + "FOREIGN KEY(" + COLUMN_ATTENDANCE_LECTURE_ID + ") REFERENCES " + LECTURE_TABLE + "(" + COLUMN_LECTURE_ID + "))";
        db.execSQL(createAttendanceTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LECTURER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DEPARTMENT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LECTURE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ATTENDANCE_TABLE);
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
        cv.put(COLUMN_USER_DEPARTMENT_ID, String.valueOf(userModel.getDepartmentId()));
        cv.put(COLUMN_USER_TYPE, userModel.getType());
        cv.put(COLUMN_USER_CREATION_TIME, userModel.getCreationTime());
        cv.put(COLUMN_USER_LAST_LOGIN, userModel.getLastLogin());

        long insert = db.insert(USER_TABLE, null, cv);
        return insert != -1;
    }

    public boolean addNewStudent(StudentModel studentModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_UUID, studentModel.getUuid().toString());
        cv.put(COLUMN_STUDENT_NUMBER, studentModel.getStudentNumber());
        cv.put(COLUMN_STUDENT_GRADE, studentModel.getGrade());
        cv.put(COLUMN_STUDENT_IS_IN_CAMPUS, String.valueOf(studentModel.getIsInCampus()));

        long insert = db.insert(STUDENT_TABLE, null, cv);
        return insert != -1;
    }

    public boolean addNewLecturer(LecturerModel lecturerModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_UUID, lecturerModel.getUuid().toString());
        cv.put(COLUMN_LECTURER_DEPARTMENT_ID, lecturerModel.getDepartmentId());
        cv.put(COLUMN_LECTURER_IN_LECTURE, String.valueOf(lecturerModel.isInLecture()));

        long insert = db.insert(LECTURER_TABLE, null, cv);
        return insert != -1;
    }
    public boolean addNewDepartment(DepartmentModel departmentModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DEPARTMENT_NAME, departmentModel.getDepartmentName());
        cv.put(COLUMN_DEPARTMENT_LANGUAGE, departmentModel.getDepartmentLanguage());

        long insert = db.insert(DEPARTMENT_TABLE, null, cv);
        if (insert != -1) {
            departmentModel.setDepartmentId((int) insert);
        }
        return insert != -1;
    }
    public boolean addNewLecture(LectureModel lectureModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LECTURE_ID, lectureModel.getLectureId());
        cv.put(COLUMN_LECTURE_NAME, lectureModel.getLectureName());
        cv.put(COLUMN_LECTURE_CODE, lectureModel.getLectureCode());
        cv.put(COLUMN_LECTURE_LECTURER_UUID, lectureModel.getLecturerUUID().toString());
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
            return cursor.moveToFirst();
        }
    }

    public boolean checkDepartment(String departmentName, String departmentLanguage) {
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT 1 FROM " + DEPARTMENT_TABLE + " WHERE " + COLUMN_DEPARTMENT_NAME + " = ? AND " + COLUMN_DEPARTMENT_LANGUAGE + " = ?",
                new String[]{departmentName, departmentLanguage})) {
            return cursor.moveToFirst();
        }
    }

    public void setLastLogin(String uuid) {
        SQLiteDatabase db = this.getWritableDatabase();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = LocalDateTime.now().format(formatter);

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_LAST_LOGIN, currentTime);

        db.update(USER_TABLE, values, COLUMN_UUID + " = ?", new String[]{uuid});
    }

    public boolean authenticateUser(String email, String inputPassword) {
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_PASSWORD + ", " + COLUMN_UUID + " FROM " + USER_TABLE + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email})) {

            if (cursor.moveToFirst()) {
                String storedPasswordHash = cursor.getString(0);
                String uuid = cursor.getString(1);

                if (storedPasswordHash.equals(hashPassword(inputPassword))) {
                    setLastLogin(uuid);
                    return true;
                }
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
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserNameFromUUID(String uuid) {
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_NAME + " FROM " + USER_TABLE + " WHERE " + COLUMN_UUID + " = ?",
                new String[]{uuid})) {

            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        }
        return null;
    }

    public UUID getUuid(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_UUID + " FROM " + USER_TABLE + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email})) {

            if (cursor.moveToFirst()) {
                String uuidString = cursor.getString(0);
                return UUID.fromString(uuidString);
            }
        }
        return null;
    }

    public String getUserType(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_USER_TYPE + " FROM " + USER_TABLE + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email})) {

            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        }
        return null;
    }

    public String getLectureCode(String lectureName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String lectureCode = null;

        String query = "SELECT " + COLUMN_LECTURE_CODE + " FROM " + LECTURE_TABLE + " WHERE " + COLUMN_LECTURE_NAME + " = ?";
        try (Cursor cursor = db.rawQuery(query, new String[]{lectureName})) {
            if (cursor.moveToFirst()) {
                lectureCode = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LECTURE_CODE));
            }
        }
        return lectureCode;
    }

    public List<String> getLecturesOfLecturer(String uuid) {
        List<String> lectures = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_LECTURE_NAME + " FROM " + LECTURE_TABLE + " WHERE " + COLUMN_LECTURE_LECTURER_UUID + " = ?";
        try (Cursor cursor = db.rawQuery(query, new String[]{uuid})) {
            if (cursor.moveToFirst()) {
                do {
                    lectures.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        }
        return lectures;
    }

    public List<DepartmentModel> getAllDepartments() {
        List<DepartmentModel> departmentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                DEPARTMENT_TABLE,
                new String[]{COLUMN_DEPARTMENT_ID, COLUMN_DEPARTMENT_NAME, COLUMN_DEPARTMENT_LANGUAGE},
                null, null, null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                int idColumnIndex = cursor.getColumnIndex(COLUMN_DEPARTMENT_ID);
                int nameColumnIndex = cursor.getColumnIndex(COLUMN_DEPARTMENT_NAME);
                int languageColumnIndex = cursor.getColumnIndex(COLUMN_DEPARTMENT_LANGUAGE);

                if (idColumnIndex != -1 && nameColumnIndex != -1 && languageColumnIndex != -1) {
                    int id = cursor.getInt(idColumnIndex);
                    String name = cursor.getString(nameColumnIndex);
                    String language = cursor.getString(languageColumnIndex);
                    DepartmentModel department = new DepartmentModel(id, name, language);
                    departmentList.add(department);
                }

            } while (cursor.moveToNext());
            cursor.close();
        }

        return departmentList;
    }

    public void deleteDepartment(int departmentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(DEPARTMENT_TABLE, COLUMN_DEPARTMENT_ID + " = ?", new String[]{String.valueOf(departmentId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void deleteLecturer(String uuid) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(LECTURER_TABLE, COLUMN_UUID + " = ?", new String[]{uuid});
            db.delete(USER_TABLE, COLUMN_UUID + " = ?", new String[]{uuid});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public int getUserDepartmentId(String uuid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_DEPARTMENT_ID + " FROM " + USER_TABLE+ " WHERE " + COLUMN_UUID + " = ?", new String[]{uuid});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }

    public void updateUserDepartment(String uuid, int departmentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("department_id", departmentId);
        db.update("users", values, "uuid = ?", new String[]{uuid});
    }

    public int getDepartmentIdByName(String departmentName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_DEPARTMENT_ID + " FROM " + DEPARTMENT_TABLE + " WHERE " + COLUMN_DEPARTMENT_NAME + " = ?" , new String[]{departmentName});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }
}