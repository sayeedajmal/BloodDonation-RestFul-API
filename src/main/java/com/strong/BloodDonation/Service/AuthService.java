package com.strong.BloodDonation.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.strong.BloodDonation.Model.LoginRequest;
import com.strong.BloodDonation.Model.Staff;
import com.strong.BloodDonation.Repository.StaffRepo;
import com.strong.BloodDonation.Security.JwtUtil;

import io.jsonwebtoken.JwtException;

@Service
public class AuthService {

    private StaffRepo repository;
    private JwtUtil jwtService;

    private AuthenticationManager authenticationManager;

    public String authenticate(LoginRequest request) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        if (authenticate.isAuthenticated()) {
            Staff user = repository.findByEmail(request.getEmail()).orElseThrow();
            return jwtService.generateJwtToken(user.getEmail(), user.getPosition().toString());
        } else
            throw new JwtException("Some Thing Wrong In Auth Service");
    }
}