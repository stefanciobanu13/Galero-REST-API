package com.galero.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "players", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"first_name", "last_name"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer playerId;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Column(nullable = false)
    @NotNull(message = "Grade cannot be null")
    private Double grade;
}
