package com.example.planpal;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PDFAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Uri> pdfUris;
    private ArrayList<String> pdfFileNames;

    public PDFAdapter(Context context, ArrayList<Uri> pdfUris, ArrayList<String> pdfFileNames) {
        this.context = context;
        this.pdfUris = pdfUris;
        this.pdfFileNames = pdfFileNames;
    }

    @Override
    public int getCount() {
        return pdfUris.size();
    }

    @Override
    public Object getItem(int position) {
        return pdfUris.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pdf, parent, false);
        }

        // Find views from item_pdf.xml
        ImageView pdfIcon = convertView.findViewById(R.id.pdfimg);
        TextView textView = convertView.findViewById(R.id.pdfNameTextView);

        // Set file name
        String pdfFileName = pdfFileNames.get(position);
        textView.setText(pdfFileName);
        textView.setTextColor(Color.BLACK);

        // Set the image (ensure `image_pdf` exists in `res/drawable`)
        pdfIcon.setImageResource(R.drawable.image_pdf);

        return convertView;
    }
}
