package com.example.planpal;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.HashMap;
import java.util.List;

public class FAQAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> faqQuestions;
    private HashMap<String, String> faqAnswers;

    public FAQAdapter(Context context, List<String> faqQuestions, HashMap<String, String> faqAnswers) {
        this.context = context;
        this.faqQuestions = faqQuestions;
        this.faqAnswers = faqAnswers;
    }

    @Override
    public int getGroupCount() {
        return faqQuestions.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1; // Each question has one answer
    }

    @Override
    public Object getGroup(int groupPosition) {
        return faqQuestions.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return faqAnswers.get(faqQuestions.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String question = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        }
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(question);
        textView.setTextColor(context.getResources().getColor(R.color.whale_blue)); // Set text color
        Typeface customFont = ResourcesCompat.getFont(context, R.font.teachers);
        textView.setTypeface(customFont);
        textView.setTextSize(17);
        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.textbox_background));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String answer = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
        }
        TextView textView = convertView.findViewById(android.R.id.text2);
        textView.setText(answer);
        textView.setTextColor(context.getResources().getColor(R.color.whale_blue));
        Typeface customFont = ResourcesCompat.getFont(context, R.font.teachers);
        textView.setTypeface(customFont);
        textView.setTextSize(16);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
