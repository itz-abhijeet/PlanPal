package com.example.planpal.models;

public class Classroom {
    private String roomNumber;
    private String year;

    public Classroom(String roomNumber, String year) {
        this.roomNumber = roomNumber;
        this.year = year;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
