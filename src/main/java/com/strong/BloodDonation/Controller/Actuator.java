package com.strong.BloodDonation.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class Actuator {

    @GetMapping("actuator/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<String>("UP", HttpStatus.OK);
    }
}
