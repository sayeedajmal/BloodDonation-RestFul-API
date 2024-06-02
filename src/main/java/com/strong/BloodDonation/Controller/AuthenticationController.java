package com.strong.BloodDonation.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.strong.BloodDonation.Model.AuthenticationResponse;
import com.strong.BloodDonation.Model.LoginRequest;
import com.strong.BloodDonation.Model.Staff;
import com.strong.BloodDonation.Service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody Staff staff) {
        AuthenticationResponse register = authService.register(staff);
        if (register.getMessage().contains("staff already exist")) {
            return new ResponseEntity<>(register.getMessage(), HttpStatus.CONFLICT);
        } else
            return ResponseEntity.ok(register);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        return authService.refreshToken(request, response);
    }
}