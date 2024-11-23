package com.example.classqroom;

public class LecturerModel {
    private String uuid;
    private String department;
    private String lecture;

    public LecturerModel(String uuid, String department, String lecture) {
        this.uuid = uuid;
        this.department = department;
        this.lecture = lecture;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }
}
