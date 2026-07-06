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
}
