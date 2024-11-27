package com.example.classqroom;

public class DepartmentModel {
    private int departmentId;
    private String departmentName;
    private String departmentLanguage;

    public DepartmentModel(int departmentId, String departmentName, String departmentLanguage) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentLanguage = departmentLanguage;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentLanguage() {
        return departmentLanguage;
    }

    public void setDepartmentLanguage(String departmentLanguage) {
        this.departmentLanguage = departmentLanguage;
    }
}
