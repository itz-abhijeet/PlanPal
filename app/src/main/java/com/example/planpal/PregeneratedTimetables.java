package com.example.planpal;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class PregeneratedTimetables extends AppCompatActivity {

    private ListView pdfListView;
    private ArrayList<Uri> pdfUris;
    private ArrayList<String> pdfFileNames; // List to hold file names
    private PDFAdapter pdfAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pregenerated_timetables);

        pdfListView = findViewById(R.id.pdfListView);
        pdfUris = new ArrayList<>();
        pdfFileNames = new ArrayList<>(); // Initialize file name list

        // Fetch PDF files from the Downloads folder
        fetchPDFFiles();

        pdfAdapter = new PDFAdapter(this, pdfUris, pdfFileNames);
        pdfListView.setAdapter(pdfAdapter);

        // Set up an on item click listener to open the PDF when clicked
        pdfListView.setOnItemClickListener((parent, view, position, id) -> {
            Uri selectedPdfUri = pdfUris.get(position);
            openPDF(selectedPdfUri);
        });
    }

    private void fetchPDFFiles() {
        ContentResolver contentResolver = getContentResolver();
        Uri contentUri = MediaStore.Files.getContentUri("external");
        String[] projection = {
                MediaStore.MediaColumns._ID,  // For the ID
                MediaStore.MediaColumns.DISPLAY_NAME  // For the file name
        };
        String selection = MediaStore.MediaColumns.MIME_TYPE + "=?";
        String[] selectionArgs = {"application/pdf"};

        Cursor cursor = contentResolver.query(contentUri, projection, selection, selectionArgs, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));
                Uri pdfUri = Uri.withAppendedPath(contentUri, String.valueOf(id));

                // Store the file name and URI
                pdfUris.add(pdfUri);
                pdfFileNames.add(displayName); // Add file name to the list
            }
            cursor.close();
        } else {
            Toast.makeText(this, "No PDFs found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPDF(Uri pdfUri) {
        // Open the PDF using an external app or a PDF viewer within your app
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }
}


