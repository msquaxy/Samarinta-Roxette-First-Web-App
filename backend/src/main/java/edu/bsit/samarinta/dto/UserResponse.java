package edu.bsit.samarinta.dto;

public record UserResponse(
    Long id,
    String fullName,
    String email,
    String role
) {}
