package com.example.classqroom;

public class LectureModel {
    private int id;
    private String lectureName;
    private String lectureCode;
    private String lecturer;
    private String departments;
    private String lectureType;
    private int acts;
    private int credit;

    public LectureModel(int id, String lectureName, String lectureCode, String lecturer, String departments, String lectureType, int acts, int credit) {
        this.id = id;
        this.lectureName = lectureName;
        this.lectureCode = lectureCode;
        this.lecturer = lecturer;
        this.departments = departments;
        this.lectureType = lectureType;
        this.acts = acts;
        this.credit = credit;
    }

    public LectureModel(String lectureName, String lectureCode, String lecturer, String departments, String lectureType) {
        this.lectureName = lectureName;
        this.lectureCode = lectureCode;
        this.lecturer = lecturer;
        this.departments = departments;
        this.lectureType = lectureType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public String getLectureCode() {
        return lectureCode;
    }

    public void setLectureCode(String lectureCode) {
        this.lectureCode = lectureCode;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getDepartments() {
        return departments;
    }

    public void setDepartments(String departments) {
        this.departments = departments;
    }

    public String getLectureType() {
        return lectureType;
    }

    public void setLectureType(String lectureType) {
        this.lectureType = lectureType;
    }

    public int getActs() {
        return acts;
    }

    public void setActs(int acts) {
        this.acts = acts;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
