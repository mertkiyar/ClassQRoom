package com.example.classqroom;

import androidx.annotation.NonNull;

public class UserModel {

    private int id;
    private String uuid;
    private String email;
    private String password;
    private String type;

    public UserModel(int id, String uuid, String email, String password, String type) {
        this.id = id;
        this.uuid = uuid;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public UserModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserModel(String email) {
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}