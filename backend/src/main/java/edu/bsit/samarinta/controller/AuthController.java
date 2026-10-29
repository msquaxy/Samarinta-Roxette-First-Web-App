package edu.bsit.samarinta.controller;

import edu.bsit.samarinta.dto.LoginRequest;
import edu.bsit.samarinta.dto.LoginResponse;
import edu.bsit.samarinta.dto.RegisterRequest;
import edu.bsit.samarinta.dto.UserResponse;
import edu.bsit.samarinta.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest req) {
        return service.register(req);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return service.login(req);
    }

    @PostMapping("/logout")
    public Map<String, String> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        service.logout(token);
        return Map.of("message", "Logged out successfully");
    }
}
