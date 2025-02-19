package com.example.planpal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "college.db";
    private static final int DATABASE_VERSION = 2; // Incremented version for changes

    // Table names
    public static final String TABLE_TEACHER = "TeacherDB";
    public static final String TABLE_CLASSROOM = "ClassroomDB";
    public static final String TABLE_SUBJECT = "SubjectDB";
    private static final String TABLE_TIMETABLE = "TimetableDB";


    // Columns for TeacherDB
    private static final String TEACHER_EMAIL = "email";
    private static final String TEACHER_NAME = "name";

    // Columns for ClassroomDB
    public static final String CLASSROOM_ROOM = "room_number";
    public static final String CLASSROOM_YEAR = "year";
    public static final String CLASSROOM_CAPACITY = "capacity";
    public static final String CLASSROOM_COURSE = "course_code";
    public static final String CLASSROOM_TYPE = "type";

    // Columns for SubjectDB
    public static final String SUBJECT_CODE = "code";
    public static final String SUBJECT_NAME = "subject";
    private static final String SUBJECT_CREDITS = "credits";

    // Columns for TimetableDB
    private static final String TIMETABLE_DAY = "day";
    private static final String TIMETABLE_HOUR = "hour";
    private static final String TIMETABLE_SUBJECT_CODE = "subject_code";
    private static final String TIMETABLE_ROOM_NO = "room_number";
    private static final String TIMETABLE_YEAR = "year";

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

            String createTeacherSubjectTable = "CREATE TABLE IF NOT EXISTS TeacherSubjectDB (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "class TEXT, " +
                    "subject TEXT, " +
                    "teacher TEXT)";
            db.execSQL(createTeacherSubjectTable);


        // Create SubjectDB table
        db.execSQL("CREATE TABLE " + TABLE_SUBJECT + " (" +
                SUBJECT_CODE + " TEXT UNIQUE NOT NULL, " +
                SUBJECT_NAME + " TEXT NOT NULL, " +
                SUBJECT_CREDITS + " INTEGER NOT NULL)");

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

    public boolean addSubject(String code, String subject, int credits) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUBJECT_CODE, code);
        values.put(SUBJECT_NAME, subject);
        values.put(SUBJECT_CREDITS, credits);
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

    public Cursor viewTeacher(String email) {
        return query(TABLE_TEACHER, null, TEACHER_EMAIL + "=?", new String[]{email});
    }

    public Cursor viewClassroom(String room, String year) {
        return query(TABLE_CLASSROOM, null, CLASSROOM_ROOM + "=? AND " + CLASSROOM_YEAR + "=?", new String[]{room, year});
    }

    public Cursor viewSubject(String code) {
        return query(TABLE_SUBJECT, null, SUBJECT_CODE + "=?", new String[]{code});
    }

    // Generalized query
    private Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tableName, columns, selection, selectionArgs, null, null, null);
    }

    // ------------------------------------------ Update Methods ------------------------------------------

    public boolean updateTeacher(String email, String name) {
        ContentValues values = new ContentValues();
        values.put(TEACHER_NAME, name);
        return update(TABLE_TEACHER, values, TEACHER_EMAIL + "=?", new String[]{email});
    }

    public boolean updateClassroom(String room, String year, int capacity, String course, String type) {
        ContentValues values = new ContentValues();
        values.put(CLASSROOM_CAPACITY, capacity);
        values.put(CLASSROOM_COURSE, course);
        values.put(CLASSROOM_TYPE, type);
        return update(TABLE_CLASSROOM, values, CLASSROOM_ROOM + "=? AND " + CLASSROOM_YEAR + "=?", new String[]{room, year});
    }

    public boolean updateSubject(String code, String subject, int credits) {
        ContentValues values = new ContentValues();
        values.put(SUBJECT_NAME, subject);
        values.put(SUBJECT_CREDITS, credits);
        return update(TABLE_SUBJECT, values, SUBJECT_CODE + "=?", new String[]{code});
    }

    // Generalized update
    private boolean update(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.update(tableName, values, whereClause, whereArgs);
        return result > 0;
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


    public Cursor viewTeacherAssignments(String className) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM TeacherSubjectDB WHERE class = ?", new String[]{className});
    }


    //Teacher-Subject Methods

    public List<String> getAllSubjects() {
        List<String> subjectList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + SUBJECT_NAME + " FROM " + TABLE_SUBJECT, null);

        if (cursor.moveToFirst()) {
            do {
                subjectList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
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

    public boolean assignTeacherToSubject(String className, String subject, String teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("class", className);
        values.put("subject", subject);
        values.put("teacher", teacher);

        long result = db.insertWithOnConflict("TeacherSubjectDB", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }


    public void deleteTeacherAssignmentsForClass(String className) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("TeacherSubjectDB", "class = ?", new String[]{className});
    }


}
