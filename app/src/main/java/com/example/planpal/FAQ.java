package com.example.planpal;

import android.os.Bundle;
import android.widget.ExpandableListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FAQ extends AppCompatActivity {
    ExpandableListView faqListView;
    List<String> faqQuestions;
    HashMap<String, String> faqAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faq);

        faqListView = findViewById(R.id.faq_list);
        initFAQs();

        FAQAdapter adapter = new FAQAdapter(this, faqQuestions, faqAnswers);
        faqListView.setAdapter(adapter);
        faqListView.setGroupIndicator(null);

    }

    private void initFAQs() {
        faqQuestions = new ArrayList<>();
        faqAnswers = new HashMap<>();

        faqQuestions.add("How do I add a subject?");
        faqQuestions.add("How do I add a teacher?");
        faqQuestions.add("How do I assign a teacher to a subject?");
        faqQuestions.add("How do I generate a timetable?");
        faqQuestions.add("How do I view or delete a subject?");
        faqQuestions.add("How do I view or delete a teacher?");
        faqQuestions.add("How do I update a teacher-subject assignment?");
        faqQuestions.add("Can I view pre-generated timetables?");
        faqQuestions.add("What if I have questions or need support?");

        faqAnswers.put("How do I add a subject?", "Go to Subject > Add Subject, enter the required details, and click Add.");
        faqAnswers.put("How do I add a teacher?", "Go to Teacher > Add Teacher, enter the teacher's name and email, and click Add.");
        faqAnswers.put("How do I assign a teacher to a subject?", "Go to Teacher - Subject > Add Teacher - Subject, select the teacher, subject, and class, and click Assign.");
        faqAnswers.put("How do I generate a timetable?", "Go to Generate Timetable, click Generate Timetable, and click Save to export a sharable PDF.");
        faqAnswers.put("How do I view or delete a subject?", "Go to View/Remove Subject, locate the subject, and click Remove to delete it.");
        faqAnswers.put("How do I view or delete a teacher?", "Go to View/Remove Teacher, locate the teacher, and click Remove to delete them.");
        faqAnswers.put("How do I update a teacher-subject assignment?", "Go to Add Teacher - Subject, select the updated details, and reassign by overwriting the previous assignment.");
        faqAnswers.put("Can I view pre-generated timetables?", "Yes, go to the Pregenerated Timetables page to view previously saved timetables.");
        faqAnswers.put("What if I have questions or need support?", "Click on the Contact Us option to ask your doubts or request assistance.");

    }
}
