package com.galero.dto;

import com.galero.model.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    private Integer attendanceId;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @NotNull(message = "Player ID cannot be null")
    private Integer playerId;

    @NotNull(message = "Edition ID cannot be null")
    private Integer editionId;

    @NotNull(message = "Status cannot be null")
    private AttendanceStatus status;
}
