package com.galero.repository;

import com.galero.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findByPlayerPlayerId(Integer playerId);
    List<Attendance> findByEditionEditionId(Integer editionId);
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByPlayerPlayerIdAndEditionEditionId(Integer playerId, Integer editionId);
}
