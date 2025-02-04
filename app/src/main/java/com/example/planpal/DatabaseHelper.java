package com.example.planpal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "college.db";
    private static final int DATABASE_VERSION = 2; // Incremented version for changes

    // Table names
    public static final String TABLE_TEACHER = "TeacherDB";
    public static final String TABLE_CLASSROOM = "ClassroomDB";
    public static final String TABLE_SUBJECT = "SubjectDB";
    private static final String TABLE_TEACHER_SUBJECT = "TeacherSubjectDB";
    private static final String TABLE_SUBJECT_CLASSROOM = "SubjectClassroomDB";
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

    // Columns for TeacherSubjectDB
    private static final String TSD_TEACHER_EMAIL = "teacher_email";
    private static final String TSD_SUBJECT_CODE = "subject_code";

    // Columns for SubjectClassroomDB
    private static final String SCD_ROOM_NO = "room_number";
    private static final String SCD_YEAR = "year";
    private static final String SCD_SUBJECT_CODE = "subject_code";

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

        // Create SubjectDB table
        db.execSQL("CREATE TABLE " + TABLE_SUBJECT + " (" +
                SUBJECT_CODE + " TEXT UNIQUE NOT NULL, " +
                SUBJECT_NAME + " TEXT NOT NULL, " +
                SUBJECT_CREDITS + " INTEGER NOT NULL)");

        // Create TeacherSubjectDB table
        db.execSQL("CREATE TABLE " + TABLE_TEACHER_SUBJECT + " (" +
                TSD_TEACHER_EMAIL + " TEXT NOT NULL, " +
                TSD_SUBJECT_CODE + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + TSD_TEACHER_EMAIL + ", " + TSD_SUBJECT_CODE + "), " +
                "FOREIGN KEY(" + TSD_TEACHER_EMAIL + ") REFERENCES " + TABLE_TEACHER + "(" + TEACHER_EMAIL + "), " +
                "FOREIGN KEY(" + TSD_SUBJECT_CODE + ") REFERENCES " + TABLE_SUBJECT + "(" + SUBJECT_CODE + "))");

        // Create SubjectClassroomDB table
        db.execSQL("CREATE TABLE " + TABLE_SUBJECT_CLASSROOM + " (" +
                SCD_ROOM_NO + " TEXT NOT NULL, " +
                SCD_YEAR + " TEXT NOT NULL, " +
                SCD_SUBJECT_CODE + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + SCD_ROOM_NO + ", " + SCD_YEAR + ", " + SCD_SUBJECT_CODE + "), " +
                "FOREIGN KEY(" + SCD_ROOM_NO + ", " + SCD_YEAR + ") REFERENCES " + TABLE_CLASSROOM + "(" + CLASSROOM_ROOM + ", " + CLASSROOM_YEAR + "), " +
                "FOREIGN KEY(" + SCD_SUBJECT_CODE + ") REFERENCES " + TABLE_SUBJECT + "(" + SUBJECT_CODE + "))");

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER_SUBJECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECT_CLASSROOM);
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

    // Add methods for TeacherSubjectDB
    public boolean addTeacherSubject(String teacherEmail, String subjectCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TSD_TEACHER_EMAIL, teacherEmail);
        values.put(TSD_SUBJECT_CODE, subjectCode);
        long result = db.insert(TABLE_TEACHER_SUBJECT, null, values);
        return result != -1;
    }

    // Add methods for SubjectClassroomDB
    public boolean addSubjectClassroom(String roomNo, String year, String subjectCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCD_ROOM_NO, roomNo);
        values.put(SCD_YEAR, year);
        values.put(SCD_SUBJECT_CODE, subjectCode);
        long result = db.insert(TABLE_SUBJECT_CLASSROOM, null, values);
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

    public boolean removeTeacherSubject(String teacherEmail, String subjectCode) {
        return delete(TABLE_TEACHER_SUBJECT, TSD_TEACHER_EMAIL + "=? AND " + TSD_SUBJECT_CODE + "=?", new String[]{teacherEmail, subjectCode});
    }

    public boolean removeSubjectClassroom(String roomNo, String year, String subjectCode) {
        return delete(TABLE_SUBJECT_CLASSROOM, SCD_ROOM_NO + "=? AND " + SCD_YEAR + "=? AND " + SCD_SUBJECT_CODE + "=?", new String[]{roomNo, year, subjectCode});
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

    public Cursor viewTeacherSubject(String teacherEmail) {
        return query(TABLE_TEACHER_SUBJECT, null, TSD_TEACHER_EMAIL + "=?", new String[]{teacherEmail});
    }

    public Cursor viewSubjectClassroom(String roomNo, String year) {
        return query(TABLE_SUBJECT_CLASSROOM, null, SCD_ROOM_NO + "=? AND " + SCD_YEAR + "=?", new String[]{roomNo, year});
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

    public boolean updateTeacherSubject(String teacherEmail, String oldSubjectCode, String newSubjectCode) {
        ContentValues values = new ContentValues();
        values.put(TSD_SUBJECT_CODE, newSubjectCode);

        // Update the subject for the teacher with the old subject code
        return update(TABLE_TEACHER_SUBJECT, values, TSD_TEACHER_EMAIL + "=? AND " + TSD_SUBJECT_CODE + "=?",
                new String[]{teacherEmail, oldSubjectCode});
    }

    public boolean updateSubjectClassroom(String oldRoomNo, String oldYear, String oldSubjectCode,
                                          String newRoomNo, String newYear, String newSubjectCode) {
        ContentValues values = new ContentValues();
        values.put(SCD_ROOM_NO, newRoomNo);
        values.put(SCD_YEAR, newYear);
        values.put(SCD_SUBJECT_CODE, newSubjectCode);

        // Update the room and year for the subject
        return update(TABLE_SUBJECT_CLASSROOM, values, SCD_ROOM_NO + "=? AND " + SCD_YEAR + "=? AND " + SCD_SUBJECT_CODE + "=?",
                new String[]{oldRoomNo, oldYear, oldSubjectCode});
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


}
