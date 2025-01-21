package com.mrtkyr.classqroom.model;

public class FacultyModel {
    private int facultyId;
    private String facultyName;

    public FacultyModel(String facultyName) {
        this.facultyName = facultyName;
    }

    public FacultyModel(int facultyId, String facultyName) {
        this.facultyId = facultyId;
        this.facultyName = facultyName;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }
}
