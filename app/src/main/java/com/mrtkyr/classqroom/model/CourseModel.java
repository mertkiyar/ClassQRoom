package com.mrtkyr.classqroom.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class CourseModel {
    private UUID courseId;
    private String courseName;
    private String courseCode;
    private BigDecimal courseEcts;
    private BigDecimal courseCredit;
    private Short hoursTheoretical;
    private Short hoursPractical;
    private Integer minAttendancePercent;
    private LanguageModel language;
    private Boolean online;
    private Boolean elective;
    private DepartmentModel department;
    private String createdAt;

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public BigDecimal getCourseEcts() {
        return courseEcts;
    }

    public void setCourseEcts(BigDecimal courseEcts) {
        this.courseEcts = courseEcts;
    }

    public BigDecimal getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(BigDecimal courseCredit) {
        this.courseCredit = courseCredit;
    }

    public Short getHoursTheoretical() {
        return hoursTheoretical;
    }

    public void setHoursTheoretical(Short hoursTheoretical) {
        this.hoursTheoretical = hoursTheoretical;
    }

    public Short getHoursPractical() {
        return hoursPractical;
    }

    public void setHoursPractical(Short hoursPractical) {
        this.hoursPractical = hoursPractical;
    }

    public Integer getMinAttendancePercent() {
        return minAttendancePercent;
    }

    public void setMinAttendancePercent(Integer minAttendancePercent) {
        this.minAttendancePercent = minAttendancePercent;
    }

    public LanguageModel getLanguage() {
        return language;
    }

    public void setLanguage(LanguageModel language) {
        this.language = language;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Boolean getElective() {
        return elective;
    }

    public void setElective(Boolean elective) {
        this.elective = elective;
    }

    public DepartmentModel getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentModel department) {
        this.department = department;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return courseCode + "  -  " + courseName;
    }
}
