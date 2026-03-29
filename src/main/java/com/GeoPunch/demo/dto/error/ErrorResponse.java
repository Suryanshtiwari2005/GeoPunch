package com.GeoPunch.demo.dto.error;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private boolean success;
    private String message;
    private String error;
    private LocalDateTime timestamp;
}
