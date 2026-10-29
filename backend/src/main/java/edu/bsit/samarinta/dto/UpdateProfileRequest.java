package edu.bsit.samarinta.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
    @NotBlank(message = "Full name cannot be blank")
    String fullName
) {}
