package com.galero.service;

import com.galero.dto.AttendanceDTO;
import com.galero.exception.ResourceNotFoundException;
import com.galero.model.Attendance;
import com.galero.model.Edition;
import com.galero.model.Player;
import com.galero.repository.AttendanceRepository;
import com.galero.repository.EditionRepository;
import com.galero.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private EditionRepository editionRepository;

    public AttendanceDTO recordAttendance(AttendanceDTO attendanceDTO) {
        Player player = playerRepository.findById(attendanceDTO.getPlayerId())
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with ID: " + attendanceDTO.getPlayerId()));
        
        Edition edition = editionRepository.findById(attendanceDTO.getEditionId())
                .orElseThrow(() -> new ResourceNotFoundException("Edition not found with ID: " + attendanceDTO.getEditionId()));
        
        Attendance attendance = new Attendance();
        attendance.setDate(attendanceDTO.getDate());
        attendance.setPlayer(player);
        attendance.setEdition(edition);
        attendance.setStatus(attendanceDTO.getStatus());
        
        Attendance saved = attendanceRepository.save(attendance);
        return convertToDTO(saved);
    }

    public AttendanceDTO getAttendanceById(Integer id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + id));
        return convertToDTO(attendance);
    }

    public List<AttendanceDTO> getAttendanceByPlayer(Integer playerId) {
        return attendanceRepository.findByPlayerPlayerId(playerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceDTO> getAttendanceByEdition(Integer editionId) {
        return attendanceRepository.findByEditionEditionId(editionId).stream()
                .sorted((a1, a2) -> {
                    // Custom sort order: inscris, rezerva, retras
                    int statusOrder1 = getStatusOrder(a1.getStatus());
                    int statusOrder2 = getStatusOrder(a2.getStatus());
                    return Integer.compare(statusOrder1, statusOrder2);
                })
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private int getStatusOrder(com.galero.model.AttendanceStatus status) {
        switch (status) {
            case inscris:
                return 1;
            case rezerva:
                return 2;
            case retras:
                return 3;
            default:
                return 4;
        }
    }

    public List<AttendanceDTO> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceDTO> getAttendanceByPlayerAndEdition(Integer playerId, Integer editionId) {
        return attendanceRepository.findByPlayerPlayerIdAndEditionEditionId(playerId, editionId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceDTO> getAllAttendance() {
        return attendanceRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AttendanceDTO updateAttendance(Integer id, AttendanceDTO attendanceDTO) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + id));
        
        attendance.setDate(attendanceDTO.getDate());
        attendance.setStatus(attendanceDTO.getStatus());
        
        Attendance updated = attendanceRepository.save(attendance);
        return convertToDTO(updated);
    }

    public void deleteAttendance(Integer id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + id));
        attendanceRepository.delete(attendance);
    }

    private AttendanceDTO convertToDTO(Attendance attendance) {
        return new AttendanceDTO(
                attendance.getAttendanceId(),
                attendance.getDate(),
                attendance.getPlayer().getPlayerId(),
                attendance.getEdition().getEditionId(),
                attendance.getStatus()
        );
    }
}
