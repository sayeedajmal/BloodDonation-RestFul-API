package com.strong.BloodDonation.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.strong.BloodDonation.Email.MailService;
import com.strong.BloodDonation.Model.AuthenticationResponse;
import com.strong.BloodDonation.Model.LoginRequest;
import com.strong.BloodDonation.Model.Staff;
import com.strong.BloodDonation.Model.Token;
import com.strong.BloodDonation.Repository.StaffRepo;
import com.strong.BloodDonation.Repository.TokenRepository;
import com.strong.BloodDonation.Security.JwtUtil;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService {

    @Autowired
    private StaffRepo repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtils;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MailService mailService;

    public AuthenticationResponse register(Staff request) {

        // check if staff already exist. if exist than authenticate the staff
        if (repository.findByStaffName(request.getUsername()).isPresent()
                || repository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthenticationResponse(null, null, "staff already exist");
        }

        Staff staff = new Staff();
        staff.setStaffName(request.getStaffName());
        staff.setAddress(request.getAddress());
        staff.setContactNumber(request.getContactNumber());
        staff.setEmail(request.getEmail());
        staff.setEnabled(false);
        staff.setPosition(null);
        staff.setStaffId(staff.getStaffId());
        staff.setPassword(passwordEncoder.encode(request.getPassword()));
        staff.setPosition(request.getPosition());

        staff = repository.save(staff);

        String accessToken = jwtUtils.generateAccessToken(staff);
        String refreshToken = jwtUtils.generateRefreshToken(staff);

        saveUserToken(accessToken, refreshToken, staff);
        mailService.sendStaffWelcomeEmail(staff);
        return new AuthenticationResponse(accessToken, refreshToken, "staff registration successful");

    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));
        } catch (AuthenticationException e) {
            throw new JwtException("Incorrect email or password", e);
        }
        Staff staff = repository.findByEmail(request.getEmail()).orElseThrow();
        String accessToken = jwtUtils.generateAccessToken(staff);
        String refreshToken = jwtUtils.generateRefreshToken(staff);

        revokeAllTokenByUser(staff);
        saveUserToken(accessToken, refreshToken, staff);

        return new AuthenticationResponse(accessToken, refreshToken, "staff login successful");

    }

    private void revokeAllTokenByUser(Staff staff) {
        List<Token> validTokens = tokenRepository.findAllAccessTokensByStaff(staff.getStaffId());
        if (validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t -> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }

    private void saveUserToken(String accessToken, String refreshToken, Staff staff) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setStaff(staff);
        tokenRepository.save(token);
    }

    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request,
            HttpServletResponse response) {
        // extract the token from authorization header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        // extract email from token
        String email = jwtUtils.extractUserEmail(token);

        // check if the staff exists in the database
        Staff staff = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        // check if the token is valid
        if (jwtUtils.isValidRefreshToken(token, staff)) {
            // generate access token and refresh token
            String accessToken = jwtUtils.generateAccessToken(staff);
            String refreshToken = jwtUtils.generateRefreshToken(staff);

            // revoke all tokens for this user and save new tokens
            revokeAllTokenByUser(staff);
            saveUserToken(accessToken, refreshToken, staff);

            // return new tokens in the response
            AuthenticationResponse authenticationResponse = new AuthenticationResponse(accessToken, refreshToken,
                    "New token generated");
            return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
