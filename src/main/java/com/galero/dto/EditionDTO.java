package com.galero.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditionDTO {
    private Integer editionId;

    @NotNull(message = "Edition number cannot be null")
    private Integer editionNumber;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;
}
