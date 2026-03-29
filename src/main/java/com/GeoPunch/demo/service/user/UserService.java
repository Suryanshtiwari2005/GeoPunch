package com.GeoPunch.demo.service.user;

import com.GeoPunch.demo.dto.user.UserRequestDto;
import com.GeoPunch.demo.dto.user.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDto createUser(UserRequestDto request);

    UserResponseDto getUserById(UUID id);

    List<UserResponseDto> getUsersByOrganization(UUID organizationId);
}
