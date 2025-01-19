package com.example.planpal.models;

public class SubjectClassroom {
    private String roomNo;
    private String year;
    private String subjectCode;

    // Constructor
    public SubjectClassroom(String roomNo, String year, String subjectCode) {
        this.roomNo = roomNo;
        this.year = year;
        this.subjectCode = subjectCode;
    }

    // Getter and Setter for roomNo
    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    // Getter and Setter for year
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    // Getter and Setter for subjectCode
    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
}
