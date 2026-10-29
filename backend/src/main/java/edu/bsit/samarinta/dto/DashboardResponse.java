package edu.bsit.samarinta.dto;

import java.time.LocalDate;

public record DashboardResponse(
    String fullName,
    String email,
    String role,
    LocalDate memberSince,
    long totalUsers
) {}
