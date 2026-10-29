package edu.bsit.samarinta.service;

import edu.bsit.samarinta.dto.*;
import edu.bsit.samarinta.exception.AuthException;
import edu.bsit.samarinta.exception.ForbiddenException;
import edu.bsit.samarinta.model.User;
import edu.bsit.samarinta.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private final UserRepository repo;
    private final Map<String, Long> sessions = new ConcurrentHashMap<>(); // token -> userId

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public UserResponse register(RegisterRequest req) {
        if (repo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already used");
        }
        User u = new User();
        u.setFullName(req.fullName());
        u.setEmail(req.email());
        u.setPassword(hash(req.password()));
        return toResponse(repo.save(u));
    }

    public String hash(String raw) {
        try {
            byte[] d = MessageDigest.getInstance("SHA-256")
                    .digest(raw.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(d);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public UserResponse toResponse(User u) {
        return new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getRole());
    }

    public LoginResponse login(LoginRequest req) {
        User u = repo.findByEmail(req.email())
                .orElseThrow(() -> new AuthException("Invalid credentials"));
        if (!u.getPassword().equals(hash(req.password()))) {
            throw new AuthException("Invalid credentials");
        }
        String token = UUID.randomUUID().toString();
        sessions.put(token, u.getId());
        return new LoginResponse(token, toResponse(u));
    }

    public User authenticate(String header) {
        if (header == null || header.isBlank()) {
            throw new AuthException("Please log in");
        }
        String token = header.replace("Bearer ", "").trim();
        Long id = sessions.get(token);
        if (id == null) {
            throw new AuthException("Session expired");
        }
        return repo.findById(id).orElseThrow(() -> new AuthException("User not found"));
    }

    public void logout(String header) {
        if (header != null && !header.isBlank()) {
            String token = header.replace("Bearer ", "").trim();
            sessions.remove(token);
        }
    }

    public DashboardResponse dashboard(User u) {
        return new DashboardResponse(
                u.getFullName(),
                u.getEmail(),
                u.getRole(),
                u.getCreatedAt(),
                repo.count()
        );
    }

    public void requireRole(User u, String role) {
        if (!role.equals(u.getRole())) {
            throw new ForbiddenException("Access denied");
        }
    }

    public List<UserResponse> findAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse updateProfile(User u, UpdateProfileRequest req) {
        u.setFullName(req.fullName().trim());
        return toResponse(repo.save(u));
    }
}
