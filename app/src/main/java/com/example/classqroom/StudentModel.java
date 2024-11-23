package com.example.classqroom;

public class StudentModel {
    private String uuid;
    private String number;
    private String department;
    private String grade;

    public StudentModel(String uuid, String number, String department, String grade) {
        this.uuid = uuid;
        this.number = number;
        this.department = department;
        this.grade = grade;
    }

    public StudentModel(String stdEmail, String number) {
        this.number = number;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
