package com.mrtkyr.classqroom.model;

public class LectureModel {
    private int lectureId;
    private String lectureName;
    private String lectureLanguage;
    private int acts;
    private int credit;
    private boolean isCompulsory;
    private boolean isOnline;
    private int lectureDepartmentId;
    private String lectureCode;
    private String lecturerUUID;

    public LectureModel(int lectureId, String lectureName, String lectureLanguage, int acts, int credit, boolean isCompulsory, boolean isOnline, int lectureDepartmentId, String lectureCode, String lecturerUUID) {
        this.lectureId = lectureId;
        this.lectureName = lectureName;
        this.lectureLanguage = lectureLanguage;
        this.acts = acts;
        this.credit = credit;
        this.isCompulsory = isCompulsory;
        this.isOnline = isOnline;
        this.lectureDepartmentId = lectureDepartmentId;
        this.lectureCode = lectureCode;
        this.lecturerUUID = lecturerUUID;
    }

    public LectureModel(String lectureName, String lectureCode, String lecturerUUID, String lectureLanguage, int lectureDepartmentId, boolean isCompulsory, boolean isOnline, int acts, int credit) {
        this.lectureName = lectureName;
        this.lectureCode = lectureCode;
        this.lecturerUUID = lecturerUUID;
        this.lectureLanguage = lectureLanguage;
        this.lectureDepartmentId = lectureDepartmentId;
        this.isCompulsory = isCompulsory;
        this.isOnline = isOnline;
        this.acts = acts;
        this.credit = credit;
    }

    public LectureModel(String lectureName, String lectureLanguage, int acts, int credit, boolean isCompulsory, boolean isOnline) {
        this.lectureName = lectureName;
        this.lectureLanguage = lectureLanguage;
        this.acts = acts;
        this.credit = credit;
        this.isCompulsory = isCompulsory;
        this.isOnline = isOnline;
    }

    public LectureModel(String lectureName) {
        this.lectureName = lectureName;
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

    public String getLecturerUUID() {
        return lecturerUUID;
    }

    public void setLecturerUUID(String lectureruuid) {
        this.lecturerUUID = lectureruuid;
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

    public boolean isCompulsory() {
        return isCompulsory;
    }

    public void setCompulsory(boolean compulsory) {
        isCompulsory = compulsory;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
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
