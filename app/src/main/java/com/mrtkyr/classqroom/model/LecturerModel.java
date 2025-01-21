package com.mrtkyr.classqroom.model;


public class LecturerModel {
    private String uuid;
    private String title;
    private int departmentId;
    private boolean inLecture;

    public LecturerModel(String uuid, String title, int departmentId, boolean inLecture) {
        this.uuid = uuid;
        this.title = title;
        this.departmentId = departmentId;
        this.inLecture = inLecture;
    }

    public LecturerModel(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public boolean isInLecture() {
        return inLecture;
    }

    public void setInLecture(boolean inLecture) {
        this.inLecture = inLecture;
    }
}
