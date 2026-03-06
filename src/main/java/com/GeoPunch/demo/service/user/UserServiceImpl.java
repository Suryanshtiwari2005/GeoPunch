package com.GeoPunch.demo.service.user;

import com.GeoPunch.demo.Entity.Organization;
import com.GeoPunch.demo.Entity.User;
import com.GeoPunch.demo.dto.user.UserRequestDto;
import com.GeoPunch.demo.dto.user.UserResponseDto;
import com.GeoPunch.demo.exception.DuplicateResourceException;
import com.GeoPunch.demo.exception.ResourceNotFoundException;
import com.GeoPunch.demo.repository.OrganizationRepository;
import com.GeoPunch.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public UserResponseDto createUser(UserRequestDto request) {

        // 🔒 1. Check duplicate email
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new DuplicateResourceException("Email already exists");
                });

        // 🔒 2. Check duplicate Firebase UID
        userRepository.findByFirebaseUid(request.getFirebaseUid())
                .ifPresent(user -> {
                    throw new DuplicateResourceException("Firebase UID already exists");
                });

        // 🔒 3. Validate organization exists
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .firebaseUid(request.getFirebaseUid())
                .role(request.getRole())
                .organization(organization)
                .build();

        User saved = userRepository.save(user);

        return mapToResponse(saved);
    }

    @Override
    public UserResponseDto getUserById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToResponse(user);
    }

    @Override
    public List<UserResponseDto> getUsersByOrganization(UUID organizationId) {

        return userRepository.findByOrganizationId(organizationId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔁 Mapper
    private UserResponseDto mapToResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .organizationId(user.getOrganization().getId())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
