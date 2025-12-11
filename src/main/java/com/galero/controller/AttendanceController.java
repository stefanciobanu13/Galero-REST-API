package com.galero.controller;

import com.galero.dto.AttendanceDTO;
import com.galero.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendance")
@Tag(name = "Attendance", description = "API for managing player attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping
    @Operation(summary = "Record player attendance")
    public ResponseEntity<AttendanceDTO> recordAttendance(@Valid @RequestBody AttendanceDTO attendanceDTO) {
        AttendanceDTO created = attendanceService.recordAttendance(attendanceDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get attendance by ID")
    public ResponseEntity<AttendanceDTO> getAttendanceById(@PathVariable Integer id) {
        AttendanceDTO attendance = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/player/{playerId}")
    @Operation(summary = "Get attendance records for a player")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByPlayer(@PathVariable Integer playerId) {
        List<AttendanceDTO> attendance = attendanceService.getAttendanceByPlayer(playerId);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/edition/{editionId}")
    @Operation(summary = "Get attendance records for an edition")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByEdition(@PathVariable Integer editionId) {
        List<AttendanceDTO> attendance = attendanceService.getAttendanceByEdition(editionId);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get attendance records for a specific date")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceDTO> attendance = attendanceService.getAttendanceByDate(date);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/player/{playerId}/edition/{editionId}")
    @Operation(summary = "Get attendance records for a player in a specific edition")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByPlayerAndEdition(
            @PathVariable Integer playerId,
            @PathVariable Integer editionId) {
        List<AttendanceDTO> attendance = attendanceService.getAttendanceByPlayerAndEdition(playerId, editionId);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping
    @Operation(summary = "Get all attendance records")
    public ResponseEntity<List<AttendanceDTO>> getAllAttendance() {
        List<AttendanceDTO> attendance = attendanceService.getAllAttendance();
        return ResponseEntity.ok(attendance);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an attendance record")
    public ResponseEntity<AttendanceDTO> updateAttendance(@PathVariable Integer id, @Valid @RequestBody AttendanceDTO attendanceDTO) {
        AttendanceDTO updated = attendanceService.updateAttendance(id, attendanceDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an attendance record")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Integer id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }
}
