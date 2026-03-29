package com.GeoPunch.backend.controller;

import com.GeoPunch.backend.dto.ApiResponse;
import com.GeoPunch.backend.dto.user.UserRequestDto;
import com.GeoPunch.backend.dto.user.UserResponseDto;
import com.GeoPunch.backend.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(
            @Valid @RequestBody UserRequestDto request) {
        UserResponseDto response = userService.createUser(request);

        return ResponseEntity.ok(
                ApiResponse.<UserResponseDto>builder()
                        .success(true)
                        .message("User created successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable UUID id) {

        UserResponseDto response = userService.getUserById(id);

        return ResponseEntity.ok(
                ApiResponse.<UserResponseDto>builder()
                        .success(true)
                        .message("User fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getUsersByOrganization(
            @PathVariable UUID organizationId) {

        List<UserResponseDto> response =
                userService.getUsersByOrganization(organizationId);

        return ResponseEntity.ok(
                ApiResponse.<List<UserResponseDto>>builder()
                        .success(true)
                        .message("Users fetched successfully")
                        .data(response)
                        .build()
        );
    }
}
