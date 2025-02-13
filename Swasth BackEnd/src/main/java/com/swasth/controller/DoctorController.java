package com.swasth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swasth.dto.DoctorDTO;
import com.swasth.exception.ApiResponse;
import com.swasth.pojos.Appointment;
import com.swasth.pojos.Doctors;
import com.swasth.services.AppointmentService;
import com.swasth.services.DoctorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/doctor")  // Updated to match SecurityConfig path pattern
@CrossOrigin(origins = "http://localhost:3000")  // Match your CORS configuration
public class DoctorController {
    
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;
    
    // Helper method to get current authenticated doctor's ID
    private int getCurrentDoctorId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Integer.parseInt(authentication.getName());
    }

    @PostMapping("/add")
    public ResponseEntity<String> insertDoctor(@Valid @RequestBody Doctors doctor) {
        try {
            if (doctor.getFirstName() == null || doctor.getFirstName().isEmpty() 
                    || doctor.getLastName() == null || doctor.getLastName().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("Doctor first name and last name are required");
            }

            boolean insertOperation = doctorService.addDoctor(doctor);
            if (insertOperation) {
                return ResponseEntity.ok("Doctor added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add doctor");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/detailsAll")
    public ResponseEntity<?> getAllDoctors(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(defaultValue = "") String search) {
        try {
            int pageSize = 6; // Change as needed
            // Set default sort property. Adjust "firstName" if needed.
            Sort sortOrder = Sort.by("firstName");
            if (sort.equalsIgnoreCase("desc")) {
                sortOrder = sortOrder.descending();
            } else {
                sortOrder = sortOrder.ascending();
            }
            // Note: Spring Data PageRequest is zero-indexed
            Pageable pageable = PageRequest.of(page - 1, pageSize, sortOrder);
            Page<DoctorDTO> doctorsPage = doctorService.getAllDoctors(search, pageable);
            return ResponseEntity.ok(doctorsPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse(false, "Error fetching doctors"));
        }
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<Doctors> getDoctorDetails(@PathVariable int id) {
        try {
            // Check if the requesting doctor is accessing their own details
            int currentDoctorId = getCurrentDoctorId();
            if (currentDoctorId != id) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Doctors doctor = doctorService.readDoctorsDetails(id);
            if (doctor == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(doctor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/update")
    public ResponseEntity<String> updateDoctorDetails(@Valid @RequestBody Doctors doctor) {
        try {
            // Ensure doctor can only update their own details
            int currentDoctorId = getCurrentDoctorId();
            if (doctor.getDoctorsId() != currentDoctorId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only update your own details");
            }

            Doctors updatedDoctor = doctorService.updateDoctorDetails(doctor);
            return ResponseEntity.ok("Doctor details updated successfully");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                .body("Validation Failed: " + ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable int id) {
        try {
            // Ensure doctor can only delete their own account
            int currentDoctorId = getCurrentDoctorId();
            if (id != currentDoctorId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                   .body("You can only delete your own account");
            }

            boolean deleted = doctorService.deleteDoctor(id);
            if (deleted) {
                return ResponseEntity.ok("Doctor deleted successfully");
            } else {
                // Fix: Use status() instead of notFound() to include a body
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                   .body("Doctor not found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("An unexpected error occurred.");
        }
    }
    
    @GetMapping("/{doctorId}/appointments")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@PathVariable int doctorId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorId(doctorId);
        return ResponseEntity.ok(appointments);
    }
}