package com.galero.controller;

import com.galero.dto.EditionDTO;
import com.galero.service.EditionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/editions")
@Tag(name = "Editions", description = "API for managing football competition editions")
public class EditionController {

    @Autowired
    private EditionService editionService;

    @PostMapping
    @Operation(summary = "Create a new edition")
    public ResponseEntity<EditionDTO> createEdition(@Valid @RequestBody EditionDTO editionDTO) {
        EditionDTO created = editionService.createEdition(editionDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get edition by ID")
    public ResponseEntity<EditionDTO> getEditionById(@PathVariable Integer id) {
        EditionDTO edition = editionService.getEditionById(id);
        return ResponseEntity.ok(edition);
    }

    @GetMapping("/number/{editionNumber}")
    @Operation(summary = "Get edition by edition number")
    public ResponseEntity<EditionDTO> getEditionByNumber(@PathVariable Integer editionNumber) {
        EditionDTO edition = editionService.getEditionByNumber(editionNumber);
        return ResponseEntity.ok(edition);
    }

    @GetMapping
    @Operation(summary = "Get all editions")
    public ResponseEntity<List<EditionDTO>> getAllEditions() {
        List<EditionDTO> editions = editionService.getAllEditions();
        return ResponseEntity.ok(editions);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an edition")
    public ResponseEntity<EditionDTO> updateEdition(@PathVariable Integer id, @Valid @RequestBody EditionDTO editionDTO) {
        EditionDTO updated = editionService.updateEdition(id, editionDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an edition")
    public ResponseEntity<Void> deleteEdition(@PathVariable Integer id) {
        editionService.deleteEdition(id);
        return ResponseEntity.noContent().build();
    }
}
