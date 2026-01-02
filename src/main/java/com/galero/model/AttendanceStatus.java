package com.galero.model;

public enum AttendanceStatus {
    inscris("inscris"),
    retras("retras"),
    rezerva("rezerva");

    private final String value;

    AttendanceStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AttendanceStatus fromValue(String value) {
        for (AttendanceStatus status : AttendanceStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid attendance status: " + value);
    }
}
