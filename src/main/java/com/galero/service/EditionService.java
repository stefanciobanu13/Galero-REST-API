package com.galero.service;

import com.galero.dto.EditionDTO;
import com.galero.exception.ResourceNotFoundException;
import com.galero.model.Edition;
import com.galero.repository.EditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EditionService {

    @Autowired
    private EditionRepository editionRepository;

    public EditionDTO createEdition(EditionDTO editionDTO) {
        Edition edition = new Edition();
        edition.setEditionNumber(editionDTO.getEditionNumber());
        edition.setDate(editionDTO.getDate());
        Edition saved = editionRepository.save(edition);
        return convertToDTO(saved);
    }

    public EditionDTO getEditionById(Integer id) {
        Edition edition = editionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Edition not found with ID: " + id));
        return convertToDTO(edition);
    }

    public EditionDTO getEditionByNumber(Integer editionNumber) {
        Edition edition = editionRepository.findByEditionNumber(editionNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Edition not found with number: " + editionNumber));
        return convertToDTO(edition);
    }

    public List<EditionDTO> getAllEditions() {
        return editionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EditionDTO updateEdition(Integer id, EditionDTO editionDTO) {
        Edition edition = editionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Edition not found with ID: " + id));
        
        edition.setEditionNumber(editionDTO.getEditionNumber());
        edition.setDate(editionDTO.getDate());
        
        Edition updated = editionRepository.save(edition);
        return convertToDTO(updated);
    }

    public void deleteEdition(Integer id) {
        Edition edition = editionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Edition not found with ID: " + id));
        editionRepository.delete(edition);
    }

    private EditionDTO convertToDTO(Edition edition) {
        return new EditionDTO(edition.getEditionId(), edition.getEditionNumber(), edition.getDate());
    }
}
