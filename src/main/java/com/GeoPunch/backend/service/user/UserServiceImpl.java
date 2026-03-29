package com.GeoPunch.backend.service.user;

import com.GeoPunch.backend.Entity.OfficeLocation;
import com.GeoPunch.backend.Entity.Organization;
import com.GeoPunch.backend.Entity.User;
import com.GeoPunch.backend.dto.user.UserRequestDto;
import com.GeoPunch.backend.dto.user.UserResponseDto;
import com.GeoPunch.backend.exception.DuplicateResourceException;
import com.GeoPunch.backend.exception.ResourceNotFoundException;
import com.GeoPunch.backend.repository.OfficeLocationRepository;
import com.GeoPunch.backend.repository.OrganizationRepository;
import com.GeoPunch.backend.repository.UserRepository;
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
    private final OfficeLocationRepository officeRepository;

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

        OfficeLocation office = officeRepository
                .findByIdAndOrganizationId(request.getOfficeId(), request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Office not found in organization"));

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .firebaseUid(request.getFirebaseUid())
                .role(request.getRole())
                .organization(organization)
                .office(office)
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
                .officeId(user.getOffice().getId())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
