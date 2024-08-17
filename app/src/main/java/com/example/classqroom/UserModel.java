package com.example.classqroom;

import androidx.annotation.NonNull;

public class UserModel {

    private int id;
    private String name;
    private String surname;
    private int studentNumber;
    private String email;
    private String password;

    public UserModel(int id, String name, String surname, String email, int studentNumber, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.studentNumber = studentNumber;
        this.password = password;
    }

    public UserModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserModel(String email, int studentNumber) {
        this.email = email;
        this.studentNumber = studentNumber;
    }

    public UserModel(String email) {
        this.email = email;
    }

    public UserModel() {
    }

    @NonNull
    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", studentNumber=" + studentNumber +
                ", password='" + password + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
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
}
