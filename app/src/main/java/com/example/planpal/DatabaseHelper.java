package com.example.planpal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DatabaseHelper extends SQLiteOpenHelper {


    private final Context context;

    // Database Name and Version
    private static final String DATABASE_NAME = "college_database.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_TEACHER = "Teacher";
    public static final String TABLE_CLASSROOM = "Classroom";
    public static final String TABLE_PDF_STORAGE = "PdfStorage";

    // Teacher Table Columns
    public static final String COLUMN_TEACHER_ID = "TeacherID";
    public static final String COLUMN_TEACHER_NAME = "TeacherName";
    public static final String COLUMN_TEACHER_SUBJECT = "TeacherSubject";

    // Classroom Table Columns
    public static final String COLUMN_CLASSROOM_NAME = "ClassroomName";
    public static final String COLUMN_CLASSROOM_YEAR = "ClassroomYear";
    public static final String COLUMN_CLASSROOM_TYPE = "ClassroomType";

    // PdfStorage Table Columns
    public static final String COLUMN_TIMETABLE_PDF = "TimetablePdf";
    public static final String COLUMN_TEACHER_ALLOCATION_PDF = "TeacherAllocationPdf";

    // Create Teacher Table
    private static final String CREATE_TABLE_TEACHER = "CREATE TABLE " + TABLE_TEACHER + "("
            + COLUMN_TEACHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TEACHER_NAME + " TEXT, "
            + COLUMN_TEACHER_SUBJECT + " TEXT)";

    // Create Classroom Table
    private static final String CREATE_TABLE_CLASSROOM = "CREATE TABLE " + TABLE_CLASSROOM + "("
            + COLUMN_CLASSROOM_NAME + " TEXT PRIMARY KEY, "
            + COLUMN_CLASSROOM_YEAR + " TEXT, "
            + COLUMN_CLASSROOM_TYPE + " TEXT)";

    // Create PdfStorage Table
    private static final String CREATE_TABLE_PDF_STORAGE = "CREATE TABLE " + TABLE_PDF_STORAGE + "("
            + COLUMN_TIMETABLE_PDF + " BLOB, "
            + COLUMN_TEACHER_ALLOCATION_PDF + " BLOB)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context; // Store the context for later use
    }



    // ---------- METHODS OF DATABASE ----------



    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_TABLE_TEACHER);
        db.execSQL(CREATE_TABLE_CLASSROOM);
        db.execSQL(CREATE_TABLE_PDF_STORAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSROOM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PDF_STORAGE);
        onCreate(db);
    }

    // Insert into Teacher Table
    public void insertTeacher(String name, String subject) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEACHER_NAME, name);
        values.put(COLUMN_TEACHER_SUBJECT, subject);

        long newRowId = db.insert(TABLE_TEACHER, null, values);
        db.close();
        Log.d("Database", "Inserted teacher with ID: " + newRowId);
    }

    // Insert into Classroom Table
    public void insertClassroom(String name, String year, String type) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASSROOM_NAME, name);
        values.put(COLUMN_CLASSROOM_YEAR, year);
        values.put(COLUMN_CLASSROOM_TYPE, type);

        long newRowId = db.insert(TABLE_CLASSROOM, null, values);
        db.close();
        Log.d("Database", "Inserted classroom: " + name);
    }

    // Insert PDFs into PdfStorage Table
    public void insertPdf(byte[] timetablePdf, byte[] teacherAllocationPdf) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIMETABLE_PDF, timetablePdf);
        values.put(COLUMN_TEACHER_ALLOCATION_PDF, teacherAllocationPdf);

        long newRowId = db.insert(TABLE_PDF_STORAGE, null, values);
        db.close();
        Log.d("Database", "Inserted PDFs into PdfStorage with ID: " + newRowId);
    }

    // Query the Teacher Table
    public void queryTeachers() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                COLUMN_TEACHER_ID,
                COLUMN_TEACHER_NAME,
                COLUMN_TEACHER_SUBJECT
        };

        Cursor cursor = db.query(TABLE_TEACHER, projection, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long teacherId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_ID));
                String teacherName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_NAME));
                String teacherSubject = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_SUBJECT));

                Log.d("Teacher", "ID: " + teacherId + ", Name: " + teacherName + ", Subject: " + teacherSubject);
            }
            cursor.close();
        }
    }

    // Query the Classroom Table
    public void queryClassrooms() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                COLUMN_CLASSROOM_NAME,
                COLUMN_CLASSROOM_YEAR,
                COLUMN_CLASSROOM_TYPE
        };

        Cursor cursor = db.query(TABLE_CLASSROOM, projection, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String classroomName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASSROOM_NAME));
                String classroomYear = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASSROOM_YEAR));
                String classroomType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASSROOM_TYPE));

                Log.d("Classroom", "Name: " + classroomName + ", Year: " + classroomYear + ", Type: " + classroomType);
            }
            cursor.close();
        }
    }

    // Retrieve Timetable PDF from PdfStorage Table
    public byte[] getTimetablePdf() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PDF_STORAGE,
                new String[]{COLUMN_TIMETABLE_PDF},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            byte[] pdfBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_TIMETABLE_PDF));
            cursor.close();
            return pdfBytes;
        }
        return null;
    }


    // Read PDF file from assets folder as byte array
    public byte[] readPdfFromAssets(String fileName) throws IOException {
        try (InputStream inputStream = context.getAssets().open(fileName);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }


    //Deleting Data

    // Method to delete a teacher by their TeacherID
    public void deleteTeacher(long teacherId) {
        SQLiteDatabase db = getWritableDatabase();

        // Define the `where` clause and its arguments
        String whereClause = COLUMN_TEACHER_ID + " = ?";
        String[] whereArgs = { String.valueOf(teacherId) };

        // Perform the deletion
        int rowsDeleted = db.delete(TABLE_TEACHER, whereClause, whereArgs);

        // Close the database
        db.close();

        // Log the result
        Log.d("Database", "Deleted teacher with ID: " + teacherId + ". Rows affected: " + rowsDeleted);
    }


    // Method to delete a classroom by its name
    public void deleteClassroom(String classroomName) {
        SQLiteDatabase db = getWritableDatabase();

        // Define the `where` clause and its arguments
        String whereClause = COLUMN_CLASSROOM_NAME + " = ?";
        String[] whereArgs = { classroomName };

        // Perform the deletion
        int rowsDeleted = db.delete(TABLE_CLASSROOM, whereClause, whereArgs);

        // Close the database
        db.close();

        // Log the result
        Log.d("Database", "Deleted classroom: " + classroomName + ". Rows affected: " + rowsDeleted);
    }

}
