package com.flightreservation.controllers;

import com.flightreservation.model.User;
import com.flightreservation.exceptions.UserNotFoundException;
import com.flightreservation.repositories.UserRepository;
import com.flightreservation.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecurityService securityService;

    @Autowired
    public UserController(UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          SecurityService securityService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.securityService = securityService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {

        LOGGER.info("Registering user: {}", user.getEmail());

        Map<String, Object> response = new HashMap<>();

        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                response.put("success", false);
                response.put("message", "User already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);

            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("userId", savedUser.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            LOGGER.error("Error registering user", e);
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        LOGGER.info("Login attempt for: {}", email);

        Map<String, Object> response = new HashMap<>();

        try {
            User foundUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            boolean loginResponse = securityService.login(email, password);

            if (loginResponse) {
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("user", foundUser);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid username or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (UserNotFoundException e) {
            response.put("success", false);
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            LOGGER.error("Error during login", e);
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok().build();
    }
}