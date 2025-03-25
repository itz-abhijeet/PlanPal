package com.example.planpal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "college.db";
    private static final int DATABASE_VERSION = 2; // Incremented version for changes

    // Table names
    public static final String TABLE_TEACHER = "TeacherDB";
    public static final String TABLE_CLASSROOM = "ClassroomDB";
    public static final String TABLE_SUBJECT = "SubjectDB";
    public static final String TABLE_TIMETABLE = "TimetableDB";
    public static final String TABLE_TEACHER_SUBJECT = "TeacherSubjectDB";

    // Columns for TeacherDB
    public static final String TEACHER_EMAIL = "email";
    public static final String TEACHER_NAME = "name";

    // Columns for ClassroomDB
    public static final String CLASSROOM_ROOM = "room_number";
    public static final String CLASSROOM_YEAR = "year";
    public static final String CLASSROOM_CAPACITY = "capacity";
    public static final String CLASSROOM_COURSE = "course_code";
    public static final String CLASSROOM_TYPE = "type";

    // Columns for SubjectDB
    public static final String SUBJECT_CODE = "code";
    public static final String SUBJECT_NAME = "subject";
    public static final String SUBJECT_CREDITS = "credits";
    public static final String SUBJECT_IS_LAB = "lab";


    // Columns for TimetableDB
    public static final String TIMETABLE_DAY = "day";
    public static final String TIMETABLE_HOUR = "hour";
    public static final String TIMETABLE_SUBJECT_CODE = "subject_code";
    public static final String TIMETABLE_ROOM_NO = "room_number";
    public static final String TIMETABLE_YEAR = "year";


    // Columns for TeacherSubjectDB
    public static final String TEACHER_SUBJECT_ID = "id";
    public static final String TEACHER_SUBJECT_CLASS = "class";
    public static final String TEACHER_SUBJECT_SUBJECT = "subject";
    public static final String TEACHER_SUBJECT_TEACHER = "teacher";
    public static final String TEACHER_SUBJECT_SUBJECT_CODE = "subject_code";
    public static final String TEACHER_SUBJECT_TEACHER_EMAIL = "teacher_email";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create TeacherDB table
        db.execSQL("CREATE TABLE " + TABLE_TEACHER + " (" +
                TEACHER_EMAIL + " TEXT PRIMARY KEY, " +
                TEACHER_NAME + " TEXT NOT NULL)");

        // Create ClassroomDB table
        db.execSQL("CREATE TABLE " + TABLE_CLASSROOM + " (" +
                CLASSROOM_ROOM + " TEXT NOT NULL, " +
                CLASSROOM_YEAR + " TEXT NOT NULL, " +
                CLASSROOM_CAPACITY + " INTEGER NOT NULL, " +
                CLASSROOM_COURSE + " TEXT NOT NULL, " +
                CLASSROOM_TYPE + " TEXT NOT NULL, " +
                "UNIQUE(" + CLASSROOM_ROOM + ", " + CLASSROOM_YEAR + ", " +
                CLASSROOM_COURSE + ", " + CLASSROOM_TYPE + "))");

        db.execSQL("CREATE TABLE " + TABLE_TEACHER_SUBJECT + " (" +
                TEACHER_SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TEACHER_SUBJECT_CLASS + " TEXT NOT NULL, " +
                TEACHER_SUBJECT_SUBJECT + " TEXT NOT NULL, " +
                TEACHER_SUBJECT_TEACHER + " TEXT NOT NULL, " +

                TEACHER_SUBJECT_SUBJECT_CODE + " TEXT, " +
                TEACHER_SUBJECT_TEACHER_EMAIL + " TEXT, " +
                "FOREIGN KEY (" + TEACHER_SUBJECT_SUBJECT_CODE + ") REFERENCES " + TABLE_SUBJECT + "(" + SUBJECT_CODE + "), " +
                "FOREIGN KEY (" + TEACHER_SUBJECT_TEACHER_EMAIL + ") REFERENCES " + TABLE_TEACHER + "(" + TEACHER_EMAIL + ")," +

                "FOREIGN KEY(" + TEACHER_SUBJECT_SUBJECT + ") REFERENCES " + TABLE_SUBJECT + "(" + SUBJECT_CODE + "), " +
                "FOREIGN KEY(" + TEACHER_SUBJECT_TEACHER + ") REFERENCES " + TABLE_TEACHER + "(" + TEACHER_EMAIL + "))");

        // Create SubjectDB table
        db.execSQL("CREATE TABLE " + TABLE_SUBJECT + " (" +
                SUBJECT_CODE + " TEXT UNIQUE NOT NULL, " +
                SUBJECT_NAME + " TEXT NOT NULL, " +
                SUBJECT_CREDITS + " INTEGER NOT NULL, " +
                SUBJECT_IS_LAB + " TEXT NOT NULL DEFAULT 'NO')");


        db.execSQL("CREATE TABLE " + TABLE_TIMETABLE + " (" +
                TIMETABLE_DAY + " TEXT NOT NULL, " +
                TIMETABLE_HOUR + " INTEGER NOT NULL, " +
                TIMETABLE_SUBJECT_CODE + " TEXT NOT NULL, " +
                TIMETABLE_ROOM_NO + " TEXT NOT NULL, " +
                TIMETABLE_YEAR + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + TIMETABLE_DAY + ", " + TIMETABLE_HOUR + ", " + TIMETABLE_ROOM_NO + ", " + TIMETABLE_YEAR + "), " +
                "FOREIGN KEY(" + TIMETABLE_SUBJECT_CODE + ") REFERENCES " + TABLE_SUBJECT + "(" + SUBJECT_CODE + "), " +
                "FOREIGN KEY(" + TIMETABLE_ROOM_NO + ", " + TIMETABLE_YEAR + ") REFERENCES " + TABLE_CLASSROOM + "(" + CLASSROOM_ROOM + ", " + CLASSROOM_YEAR + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSROOM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER_SUBJECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMETABLE);
        onCreate(db);

    }


    // ------------------------------------------ Add methods ------------------------------------------


    // Add methods for TeacherDB, ClassroomDB, SubjectDB
    public boolean addTeacher(String email, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEACHER_EMAIL, email);
        values.put(TEACHER_NAME, name);
        long result = db.insert(TABLE_TEACHER, null, values);
        return result != -1;
    }

    public boolean addClassroom(String room, String year, int capacity, String course, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASSROOM_ROOM, room);
        values.put(CLASSROOM_YEAR, year);
        values.put(CLASSROOM_CAPACITY, capacity);
        values.put(CLASSROOM_COURSE, course);
        values.put(CLASSROOM_TYPE, type);
        long result = db.insert(TABLE_CLASSROOM, null, values);
        return result != -1;
    }

    public boolean addSubject(String code, String name, int credits, String lab) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUBJECT_CODE, code);
        values.put(SUBJECT_NAME, name);
        values.put(SUBJECT_CREDITS, credits);
        values.put(SUBJECT_IS_LAB, lab); // Now stores "YES" or "NO"

        long result = db.insert(TABLE_SUBJECT, null, values);
        return result != -1;
    }



    // ------------------------------------------ Remove Methods ------------------------------------------

    public boolean removeTeacher(String email) {
        return delete(TABLE_TEACHER, TEACHER_EMAIL + "=?", new String[]{email});
    }

    public boolean removeClassroom(String room, String year) {
        return delete(TABLE_CLASSROOM, CLASSROOM_ROOM + "=? AND " + CLASSROOM_YEAR + "=?", new String[]{room, year});
    }

    public boolean removeSubject(String code) {
        return delete(TABLE_SUBJECT, SUBJECT_CODE + "=?", new String[]{code});
    }

    // Generalized deletion
    private boolean delete(String tableName, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(tableName, whereClause, whereArgs);
        return result > 0;
    }

    // ------------------------------------------ View Methods ------------------------------------------

    public Cursor viewTable(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + tableName, null);
    }

    // Generalized query
    private Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tableName, columns, selection, selectionArgs, null, null, null);
    }

    // Add this method to DatabaseHelper
    public boolean addTimetableEntry(int dayIndex, int hour, String subjectCode, String roomNumber, String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIMETABLE_DAY, dayIndex);
        values.put(TIMETABLE_HOUR, hour);
        values.put(TIMETABLE_SUBJECT_CODE, subjectCode);
        values.put(TIMETABLE_ROOM_NO, roomNumber);
        values.put(TIMETABLE_YEAR, year);
        long result = db.insert(TABLE_TIMETABLE, null, values);
        return result != -1;
    }


    //Teacher-Subject Methods

    public List<Pair<String, Boolean>> getAllSubjects() {
        List<Pair<String, Boolean>> subjectList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + SUBJECT_NAME + ", " + SUBJECT_IS_LAB + " FROM " + TABLE_SUBJECT, null);

        if (cursor.moveToFirst()) {
            do {
                String subjectName = cursor.getString(0);
                boolean isLab = cursor.getString(1).equalsIgnoreCase("YES");
                subjectList.add(new Pair<>(subjectName, isLab));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return subjectList;
    }


    public List<String> getAllTeachers() {
        List<String> teacherList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + TEACHER_NAME + " FROM " + TABLE_TEACHER, null);

        if (cursor.moveToFirst()) {
            do {
                teacherList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return teacherList;
    }

    public void assignTeacherToSubject(String className, String subject, String teacher, String subjectCode, String teacherEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.TEACHER_SUBJECT_CLASS, className);
        values.put(DatabaseHelper.TEACHER_SUBJECT_SUBJECT, subject);
        values.put(DatabaseHelper.TEACHER_SUBJECT_TEACHER, teacher);
        values.put(DatabaseHelper.TEACHER_SUBJECT_SUBJECT_CODE, subjectCode);
        values.put(DatabaseHelper.TEACHER_SUBJECT_TEACHER_EMAIL, teacherEmail);

        db.insertWithOnConflict(DatabaseHelper.TABLE_TEACHER_SUBJECT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteTeacherAssignmentsForClass(String className) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TEACHER_SUBJECT, TEACHER_SUBJECT_CLASS + " = ?", new String[]{className});
    }

    public List<String[]> viewTeacherAssignments(String className) {
        List<String[]> assignments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT subject, teacher FROM TeacherSubjectDB WHERE class=?", new String[]{className});

        if (cursor != null && cursor.moveToFirst()) { // Check for null cursor
            do {
                String subject = cursor.getString(0);
                String teacher = cursor.getString(1);
                assignments.add(new String[]{subject, teacher});
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return assignments;
    }

    public void logTeacherSubjectDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TeacherSubjectDB", null);

        if (cursor.moveToFirst()) {
            do {
                String className = cursor.getString(1); // Adjust index as per schema
                String subject = cursor.getString(2);
                String teacher = cursor.getString(3);
                Log.d("DB_CHECK", "Class: " + className + ", Subject: " + subject + ", Teacher: " + teacher);
            } while (cursor.moveToNext());
        } else {
            Log.d("DB_CHECK", "No assignments found!");
        }
        cursor.close();
        db.close();
    }


    // Method to get subject name by subject code
    public String getSubjectNameBySubjectCode(String subjectCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        String subjectName = null;

        Cursor cursor = db.rawQuery("SELECT " + SUBJECT_NAME + " FROM " + TABLE_SUBJECT +
                " WHERE " + SUBJECT_CODE + " = ?", new String[]{subjectCode});

        if (cursor.moveToFirst()) {
            subjectName = cursor.getString(0);
        }
        cursor.close();
        db.close();

        return subjectName;
    }

    // Method to get teacher name by teacher email
    public String getTeacherNameByTeacherMail(String teacherEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String teacherName = null;

        Cursor cursor = db.rawQuery("SELECT " + TEACHER_NAME + " FROM " + TABLE_TEACHER +
                " WHERE " + TEACHER_EMAIL + " = ?", new String[]{teacherEmail});

        if (cursor.moveToFirst()) {
            teacherName = cursor.getString(0);
        }
        cursor.close();
        db.close();

        return teacherName;
    }

    public String getAssignedClassForTeacherAndSubject(String teacherEmail, String subjectCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        String assignedClass = null;

        Cursor cursor = db.rawQuery(
                "SELECT " + TEACHER_SUBJECT_CLASS + " FROM " + TABLE_TEACHER_SUBJECT +
                        " WHERE " + TEACHER_SUBJECT_TEACHER_EMAIL + " = ? AND " +
                        TEACHER_SUBJECT_SUBJECT_CODE + " = ?",
                new String[]{teacherEmail, subjectCode}
        );

        if (cursor.moveToFirst()) {
            assignedClass = cursor.getString(cursor.getColumnIndex(TEACHER_SUBJECT_CLASS));
        }
        cursor.close();
        return assignedClass;
    }


}
