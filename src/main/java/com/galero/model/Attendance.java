package com.galero.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attendanceId;

    @Column(nullable = false)
    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    @NotNull(message = "Player cannot be null")
    private Player player;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Player first name cannot be blank")
    private String playerFirstName;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Player last name cannot be blank")
    private String playerLastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edition_id", nullable = false)
    @NotNull(message = "Edition cannot be null")
    private Edition edition;
}
