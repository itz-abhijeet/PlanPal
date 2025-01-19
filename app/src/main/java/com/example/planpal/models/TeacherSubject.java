package com.example.planpal.models;

public class TeacherSubject {
    private String teacherEmail;
    private String subjectCode;

    // Constructor
    public TeacherSubject(String teacherEmail, String subjectCode) {
        this.teacherEmail = teacherEmail;
        this.subjectCode = subjectCode;
    }

    // Getter and Setter for teacherEmail
    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    // Getter and Setter for subjectCode
    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
}

