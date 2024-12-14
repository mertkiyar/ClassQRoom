package com.example.classqroom;

import java.util.UUID;

public class LecturerModel {
    private UUID uuid;

    public LecturerModel(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
