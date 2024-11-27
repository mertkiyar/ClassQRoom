package com.example.classqroom;

import java.util.UUID;

public class LecturerModel {
    private UUID uuid;
    private int lecturerDepartmentId;

    public LecturerModel(UUID uuid, int lecturerDepartmentId) {
        this.uuid = uuid;
        this.lecturerDepartmentId = lecturerDepartmentId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getLecturerDepartmentId() {
        return lecturerDepartmentId;
    }

    public void setLecturerDepartmentId(int lecturerDepartmentId) {
        this.lecturerDepartmentId = lecturerDepartmentId;
    }
}
