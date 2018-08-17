package com.cnu.assignment04.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path="/api/health")
public class HealthController {

    @GetMapping(path="")
    public ResponseEntity checkHealth() {
        return new ResponseEntity(HttpStatus.OK);
    }

}