package edu.bsit.samarinta.dto;

public record LoginResponse(
    String token,
    UserResponse user
) {}
