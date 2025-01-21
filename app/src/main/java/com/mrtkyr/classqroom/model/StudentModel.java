package com.mrtkyr.classqroom.model;

public class StudentModel {
    private String uuid;
    private int studentNumber;
    private int grade;
    private boolean isInCampus;

    public StudentModel(String uuid, int studentNumber, int grade, boolean isInCampus) {
        this.uuid = uuid;
        this.studentNumber = studentNumber;
        this.grade = grade;
        this.isInCampus = false;

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
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
