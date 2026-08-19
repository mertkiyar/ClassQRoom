package com.mrtkyr.classqroom.model;

import com.mrtkyr.classqroom.enums.AttendanceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AttendanceRecordModel {
    private UUID studentId;
    private StudentModel student;
    private UUID attendanceSessionId;
    private AttendanceSessionModel attendanceSession;
    private AttendanceType attendanceType;
    private BigDecimal currentLat;
    private BigDecimal currentLong;
    private LocalDateTime attendAt;
    private Boolean late;
    private UUID deviceId;
    private String clientIp;

    public StudentModel getStudent() {
        return student;
    }

    public void setStudent(StudentModel student) {
        this.student = student;
    }

    public AttendanceSessionModel getAttendanceSession() {
        return attendanceSession;
    }

    public void setAttendanceSession(AttendanceSessionModel attendanceSession) {
        this.attendanceSession = attendanceSession;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public UUID getAttendanceSessionId() {
        return attendanceSessionId;
    }

    public void setAttendanceSessionId(UUID attendanceSessionId) {
        this.attendanceSessionId = attendanceSessionId;
    }

    public AttendanceType getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(AttendanceType attendanceType) {
        this.attendanceType = attendanceType;
    }

    public BigDecimal getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(BigDecimal currentLat) {
        this.currentLat = currentLat;
    }

    public BigDecimal getCurrentLong() {
        return currentLong;
    }

    public void setCurrentLong(BigDecimal currentLong) {
        this.currentLong = currentLong;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public LocalDateTime getAttendAt() {
        return attendAt;
    }

    public void setAttendAt(LocalDateTime attendAt) {
        this.attendAt = attendAt;
    }

    public Boolean getLate() {
        return late;
    }

    public void setLate(Boolean late) {
        this.late = late;
    }
}
