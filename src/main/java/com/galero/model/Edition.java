package com.galero.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "editions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Edition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer editionId;

    @Column(nullable = false, unique = true)
    private Integer editionNumber;

    @Column(nullable = false)
    private LocalDate date;
}
