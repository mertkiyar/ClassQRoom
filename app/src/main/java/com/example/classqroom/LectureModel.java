package com.example.classqroom;

public class LectureModel {
    private int lectureId;
    private String lectureName;
    private String lectureCode;
    private String lecturer;
    private String lectureLanguage;
    private int lectureDepartmentId;
    private String lectureType;
    private int acts;
    private int credit;

    public LectureModel(int lectureId, String lectureName, String lectureCode, String lecturer, String lectureLanguage, int lectureDepartmentId, String lectureType, int acts, int credit) {
        this.lectureId = lectureId;
        this.lectureName = lectureName;
        this.lectureCode = lectureCode;
        this.lecturer = lecturer;
        this.lectureLanguage = lectureLanguage;
        this.lectureDepartmentId = lectureDepartmentId;
        this.lectureType = lectureType;
        this.acts = acts;
        this.credit = credit;
    }

    public LectureModel(String lectureName, String lectureCode, String lecturer, String lectureLanguage, int lectureDepartmentId, String lectureType) {
        this.lectureName = lectureName;
        this.lectureCode = lectureCode;
        this.lecturer = lecturer;
        this.lectureLanguage = lectureLanguage;
        this.lectureDepartmentId = lectureDepartmentId;
        this.lectureType = lectureType;
    }

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
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

    public String getLectureLanguage() {
        return lectureLanguage;
    }

    public void setLectureLanguage(String lectureLanguage) {
        this.lectureLanguage = lectureLanguage;
    }

    public int getLectureDepartmentId() {
        return  lectureDepartmentId;
    }

    public void setLectureDepartmentId(int lectureDepartmentId) {
        this.lectureDepartmentId = lectureDepartmentId;
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
