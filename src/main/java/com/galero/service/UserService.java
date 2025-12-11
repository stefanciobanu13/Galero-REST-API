package com.galero.service;

import com.galero.dto.UserDTO;
import com.galero.exception.ResourceNotFoundException;
import com.galero.model.User;
import com.galero.model.Player;
import com.galero.repository.UserRepository;
import com.galero.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setGoogleId(userDTO.getGoogleId());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setProfilePictureUrl(userDTO.getProfilePictureUrl());
        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : User.UserRole.user);

        if (userDTO.getPlayerId() != null) {
            Player player = playerRepository.findById(userDTO.getPlayerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + userDTO.getPlayerId()));
            user.setPlayer(player);
        }

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Transactional
    public UserDTO updateUser(Integer userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setProfilePictureUrl(userDTO.getProfilePictureUrl());
        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : user.getRole());

        if (userDTO.getPlayerId() != null) {
            Player player = playerRepository.findById(userDTO.getPlayerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + userDTO.getPlayerId()));
            user.setPlayer(player);
        } else {
            user.setPlayer(null);
        }

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Transactional
    public UserDTO findOrCreateByGoogleId(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByGoogleId(userDTO.getGoogleId());

        if (existingUser.isPresent()) {
            return convertToDTO(existingUser.get());
        }

        return createUser(userDTO);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return convertToDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByGoogleId(String googleId) {
        User user = userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with google id: " + googleId));
        return convertToDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return convertToDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setGoogleId(user.getGoogleId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        if (user.getPlayer() != null) {
            dto.setPlayerId(user.getPlayer().getPlayerId());
        }

        return dto;
    }
}
