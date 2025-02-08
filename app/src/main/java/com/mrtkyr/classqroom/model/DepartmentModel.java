package com.mrtkyr.classqroom.model;

public class DepartmentModel {
    private int departmentId;
    private String departmentName;
    private String departmentCode;
    private String departmentLanguage;
    private int departmentFacultyId;

    public DepartmentModel(String departmentName, String departmentCode, String departmentLanguage, int departmentFacultyId) {
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.departmentLanguage = departmentLanguage;
        this.departmentFacultyId = departmentFacultyId;
    }
    public DepartmentModel(int departmentId, String departmentName,String departmentCode, String departmentLanguage, int departmentFacultyId) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.departmentLanguage = departmentLanguage;
        this.departmentFacultyId = departmentFacultyId;
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

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentLanguage() {
        return departmentLanguage;
    }

    public void setDepartmentLanguage(String departmentLanguage) {
        this.departmentLanguage = departmentLanguage;
    }

    public int getDepartmentFacultyId() {
        return departmentFacultyId;
    }

    public void setDepartmentFacultyId(int departmentFacultyId) {
        this.departmentFacultyId = departmentFacultyId;
    }
}
