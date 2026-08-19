package com.mrtkyr.classqroom.model;

import com.mrtkyr.classqroom.enums.GenderType;

import java.time.LocalDateTime;

public class StudentModel {
    private String firstName;
    private String lastName;
    private GenderType gender;
    private DepartmentModel department;
    private String studentNumber;
    private Integer yearOfStudy;
    private Boolean inCourse;
    private Boolean active;
    private Boolean inCampus;
    private LocalDateTime createdAt;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public DepartmentModel getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentModel department) {
        this.department = department;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Integer getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(Integer yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public Boolean getInCourse() {
        return inCourse;
    }

    public void setInCourse(Boolean inCourse) {
        this.inCourse = inCourse;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getInCampus() {
        return inCampus;
    }

    public void setInCampus(Boolean inCampus) {
        this.inCampus = inCampus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
