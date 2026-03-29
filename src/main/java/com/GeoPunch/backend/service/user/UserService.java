package com.GeoPunch.backend.service.user;

import com.GeoPunch.backend.dto.user.UserRequestDto;
import com.GeoPunch.backend.dto.user.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDto createUser(UserRequestDto request);

    UserResponseDto getUserById(UUID id);

    List<UserResponseDto> getUsersByOrganization(UUID organizationId);
}
