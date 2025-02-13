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

import com.swasth.pojos.Reports;
import com.swasth.services.ReportService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/report")
@CrossOrigin(origins = "http://localhost:3000")  // Match your CORS configuration
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/add")
    public ResponseEntity<String> addReport(@Valid @RequestBody Reports report) {
        try {
            boolean addOperation = reportService.addReports(report);
            if (addOperation) {
                return ResponseEntity.ok("Report added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body("Failed to add report");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/reportDetails/{id}")
    public ResponseEntity<Reports> getReportDetails(@PathVariable int id) {
        try {
            Reports report = reportService.readReportsDetails(id);
            if (report == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(null); 
            }
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null);
        }
    }

    @PutMapping("/updateReport")
    public ResponseEntity<String> updateReport(@Valid @RequestBody Reports report) {
        try {
            Reports updatedReport = reportService.updateReportsDetails(report);
            if (updatedReport != null) {
                return ResponseEntity.ok("Report updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Report not found with ID: " + report.getReportId());
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                                 .body("Validation Failed: " + ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An unexpected error occurred.");
        }
    }

    @DeleteMapping("/deleteReport/{id}")
    public ResponseEntity<String> deleteReport(@PathVariable int id) {
        try {
            boolean deleted = reportService.deleteReports(id);
            if (deleted) {
                return ResponseEntity.ok("Report deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Report not found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An unexpected error occurred.");
        }
    }
}
