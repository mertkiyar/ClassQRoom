package com.mrtkyr.classqroom.model;

import com.mrtkyr.classqroom.enums.AttendanceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AttendanceModel {
    private UUID attendanceId;
    private CourseModel course;
    private UUID nfcPath;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer allowedRadiusMeters;
    private AttendanceType attendanceType;
    private Short sessionHours;
    private LocalDateTime startedAt;
    private LocalDateTime expiresAt;
    private boolean active;

    public UUID getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(UUID attendanceId) {
        this.attendanceId = attendanceId;
    }

    public CourseModel getCourse() {
        return course;
    }

    public void setCourse(CourseModel course) {
        this.course = course;
    }

    public UUID getNfcPath() {
        return nfcPath;
    }

    public void setNfcPath(UUID nfcPath) {
        this.nfcPath = nfcPath;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Integer getAllowedRadiusMeters() {
        return allowedRadiusMeters;
    }

    public void setAllowedRadiusMeters(Integer allowedRadiusMeters) {
        this.allowedRadiusMeters = allowedRadiusMeters;
    }

    public AttendanceType getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(AttendanceType attendanceType) {
        this.attendanceType = attendanceType;
    }

    public Short getSessionHours() {
        return sessionHours;
    }

    public void setSessionHours(Short sessionHours) {
        this.sessionHours = sessionHours;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
