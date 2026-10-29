package edu.bsit.samarinta.controller;

import edu.bsit.samarinta.dto.UpdateProfileRequest;
import edu.bsit.samarinta.dto.UserResponse;
import edu.bsit.samarinta.model.User;
import edu.bsit.samarinta.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/users", "/api/user"})
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private static final String AUTH = "Authorization";
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping({"/me", "/profile"})
    public UserResponse me(@RequestHeader(value = AUTH, required = false) String t) {
        User user = service.authenticate(t);
        return service.toResponse(user);
    }

    @PutMapping({"/me", "/profile"})
    public UserResponse updateProfile(
            @RequestHeader(value = AUTH, required = false) String t,
            @Valid @RequestBody UpdateProfileRequest req) {
        User user = service.authenticate(t);
        return service.updateProfile(user, req);
    }
}
