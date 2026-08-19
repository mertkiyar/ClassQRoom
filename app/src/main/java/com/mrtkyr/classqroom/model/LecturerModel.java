package com.mrtkyr.classqroom.model;

import com.mrtkyr.classqroom.enums.AcademicRole;
import com.mrtkyr.classqroom.enums.AcademicTitle;
import com.mrtkyr.classqroom.enums.GenderType;

import java.time.LocalDateTime;

public class LecturerModel {
    private String firstName;
    private String lastName;
    private GenderType gender;
    private DepartmentModel department;
    private AcademicTitle lecturerTitle;
    private AcademicRole lecturerRole;
    private String phone;
    private String extPhone;
    private Boolean inCourse;
    private LocalDateTime createdAt;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public GenderType getGender() { return gender; }
    public void setGender(GenderType gender) { this.gender = gender; }
    
    public DepartmentModel getDepartment() { return department; }
    public void setDepartment(DepartmentModel department) { this.department = department; }
    
    public AcademicTitle getLecturerTitle() { return lecturerTitle; }
    public void setLecturerTitle(AcademicTitle lecturerTitle) { this.lecturerTitle = lecturerTitle; }
    
    public AcademicRole getLecturerRole() { return lecturerRole; }
    public void setLecturerRole(AcademicRole lecturerRole) { this.lecturerRole = lecturerRole; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getExtPhone() { return extPhone; }
    public void setExtPhone(String extPhone) { this.extPhone = extPhone; }
    
    public Boolean getInCourse() { return inCourse; }
    public void setInCourse(Boolean inCourse) { this.inCourse = inCourse; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
