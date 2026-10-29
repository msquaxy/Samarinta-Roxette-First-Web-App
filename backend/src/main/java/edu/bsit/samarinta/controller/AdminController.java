package edu.bsit.samarinta.controller;

import edu.bsit.samarinta.dto.UserResponse;
import edu.bsit.samarinta.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private static final String AUTH = "Authorization";
    private final UserService service;

    public AdminController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<UserResponse> listUsers(
            @RequestHeader(value = AUTH, required = false) String t) {
        service.requireRole(service.authenticate(t), "ADMIN");
        return service.findAll();
    }
}
