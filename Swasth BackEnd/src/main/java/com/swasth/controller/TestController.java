package com.swasth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swasth.pojos.Tests;
import com.swasth.services.TestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/test")  // Updated to follow consistent API path format
@CrossOrigin(origins = "http://localhost:3000")  // Match your CORS configuration
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/add")
    public ResponseEntity<String> addTest(@Valid @RequestBody Tests test) {
        try {
            boolean addOperation = testService.addTests(test);
            if (addOperation) {
                return ResponseEntity.ok("Test added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body("Failed to add test");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/testDetails/{id}")
    public ResponseEntity<Tests> getTestDetails(@PathVariable int id) {
        try {
            Tests test = testService.readTestsDetails(id);
            if (test == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(null); 
            }
            return ResponseEntity.ok(test);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null);
        }
    }

    @PutMapping("/updateTest")
    public ResponseEntity<String> updateTest(@Valid @RequestBody Tests test) {
        try {
            Tests updatedTest = testService.updateTestsDetails(test);
            if (updatedTest != null) {
                return ResponseEntity.ok("Test updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Test not found with ID: " + test.getTestId());
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                                 .body("Validation Failed: " + ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An unexpected error occurred.");
        }
    }

    @DeleteMapping("/deleteTest/{id}")
    public ResponseEntity<String> deleteTest(@PathVariable int id) {
        try {
            boolean deleted = testService.deleteTests(id);
            if (deleted) {
                return ResponseEntity.ok("Test deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Test not found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An unexpected error occurred.");
        }
    }
}
