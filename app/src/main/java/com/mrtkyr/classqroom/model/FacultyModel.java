package com.mrtkyr.classqroom.model;

import java.time.LocalDateTime;

public class FacultyModel {
    private Short facultyId;
    private String facultyName;
    private String createdAt;

    public Short getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Short facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
