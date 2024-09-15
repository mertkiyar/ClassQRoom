package com.example.classqroom;

public class LecturerModel {
    private int id;
    private String uuid;
    private String name;
    private String surname;
    private String lctEmail;
    private String department;
    private String lecture;

    public LecturerModel(int id, String uuid, String name, String surname, String lctEmail, String department, String lecture) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.surname = surname;
        this.lctEmail = lctEmail;
        this.department = department;
        this.lecture = lecture;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLctEmail() {
        return lctEmail;
    }

    public void setLctEmail(String stdEmail) {
        this.lctEmail = lctEmail;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }
}
