package com.example.classqroom;

public class StudentModel {
    private int id;
    private String uuid;
    private String stdName;
    private String stdSurname;
    private String stdEmail;
    private String number;
    private String department;
    private String grade;

    public StudentModel(int id, String uuid, String stdName, String stdSurname, String stdEmail, String number, String department, String grade) {
        this.id = id;
        this.uuid = uuid;
        this.stdName = stdName;
        this.stdSurname = stdSurname;
        this.stdEmail = stdEmail;
        this.number = number;
        this.department = department;
        this.grade = grade;
    }

    public StudentModel(int id, String stdName, String stdSurname, String stdEmail, String number) {
        this.id = id;
        this.stdName = stdName;
        this.stdSurname = stdSurname;
        this.stdEmail = stdEmail;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName;
    }

    public String getStdSurname() {
        return stdSurname;
    }

    public void setStdSurname(String stdSurname) {
        this.stdSurname = stdSurname;
    }

    public String getStdEmail() {
        return stdEmail;
    }

    public void setStdEmail(String stdEmail) {
        this.stdEmail = stdEmail;
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
