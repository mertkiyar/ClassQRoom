package com.mrtkyr.classqroom.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class AttendanceSessionModel {
    private UUID attendanceSessionId;
    private AttendanceModel attendance;
    private String sixDigitCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean active;

    public UUID getAttendanceSessionId() {
        return attendanceSessionId;
    }

    public void setAttendanceSessionId(UUID attendanceSessionId) {
        this.attendanceSessionId = attendanceSessionId;
    }

    public AttendanceModel getAttendance() {
        return attendance;
    }

    public void setAttendance(AttendanceModel attendance) {
        this.attendance = attendance;
    }

    public String getSixDigitCode() {
        return sixDigitCode;
    }

    public void setSixDigitCode(String sixDigitCode) {
        this.sixDigitCode = sixDigitCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
