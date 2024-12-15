package com.example.classqroom;

import java.util.UUID;

public class StudentModel {
    private UUID uuid;
    private int studentNumber;
    private int grade;
    private boolean isInCampus;

    public StudentModel(UUID uuid, int studentNumber, int grade, boolean isInCampus) {
        this.uuid = uuid;
        this.studentNumber = studentNumber;
        this.grade = grade;
        this.isInCampus = false;

    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public boolean getIsInCampus() {
        return isInCampus;
    }

    public void setIsInCampus(boolean isinCampus) {
        this.isInCampus = isinCampus;
    }
}
