package com.mrtkyr.classqroom.main;

import java.security.Timestamp;

public class Attendance {
    private String studentName;
    private String lectureName;
    private String status;
    private Timestamp scannedAt;

    public Attendance() {}

    public Attendance(String studentName, String lectureName, String status, Timestamp scannedAt) {
        this.studentName = studentName;
        this.lectureName = lectureName;
        this.status = status;
        this.scannedAt = scannedAt;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getLectureName() {
        return lectureName;
    }

    public Timestamp getScannedAt() {
        return scannedAt;
    }

    public String getStatus() {
        return status;
    }
}
