package com.example.classqroom;

import java.util.UUID;

public class StudentModel {
    private UUID uuid;
    private String studentNumber;
    private int studentDepartmentId;
    private String grade;

    public StudentModel(UUID uuid, String studentNumber, int studentDepartmentId, String grade) {
        this.uuid = uuid;
        this.studentNumber = studentNumber;
        this.studentDepartmentId = studentDepartmentId;
        this.grade = grade;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public int getStudentDepartmentId() {
        return studentDepartmentId;
    }

    public void setStudentDepartmentId(int studentDepartmentId) {
        this.studentDepartmentId = studentDepartmentId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
