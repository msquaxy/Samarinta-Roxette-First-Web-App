package edu.bsit.samarinta.controller;

import edu.bsit.samarinta.dto.DashboardResponse;
import edu.bsit.samarinta.dto.UserResponse;
import edu.bsit.samarinta.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    private static final String AUTH = "Authorization";
    private final UserService service;

    public DashboardController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public DashboardResponse home(
            @RequestHeader(value = AUTH, required = false) String t) {
        return service.dashboard(service.authenticate(t));
    }

    @GetMapping("/users") // ADMIN only
    public List<UserResponse> users(
            @RequestHeader(value = AUTH, required = false) String t) {
        service.requireRole(service.authenticate(t), "ADMIN");
        return service.findAll();
    }
}
