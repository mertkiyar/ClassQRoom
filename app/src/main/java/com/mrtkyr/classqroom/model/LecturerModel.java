package com.mrtkyr.classqroom.model;

import java.util.UUID;

public class LecturerModel {
    private UUID uuid;
    private String title;
    private int departmentId;
    private boolean inLecture;

    public LecturerModel(UUID uuid, String title, int departmentId, boolean inLecture) {
        this.uuid = uuid;
        this.departmentId = departmentId;
        this.inLecture = inLecture;
    }

    public LecturerModel(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
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
