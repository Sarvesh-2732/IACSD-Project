package com.swasth.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swasth.dto.DoctorDTO;
import com.swasth.exception.ApiResponse;
import com.swasth.services.DoctorService;

@RestController
@RequestMapping("/api/doctors")  // Public endpoint for fetching doctor data 
//this controller is made because the doctors controller method is only accessible to those with role Doctor 
//that is why i was not able to get all the doctors in the patient page for appointment booking of patient 
@CrossOrigin(origins = "http://localhost:3000")
public class PublicDoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<?> getAllDoctors(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(defaultValue = "") String search) {
        try {
            int pageSize = 6; // Adjust as needed

            // Default sort by "firstName" (adjust if necessary)
            Sort sortOrder = Sort.by("firstName");
            if (sort.equalsIgnoreCase("desc")) {
                sortOrder = sortOrder.descending();
            } else {
                sortOrder = sortOrder.ascending();
            }
            // PageRequest is zero-indexed
            Pageable pageable = PageRequest.of(page - 1, pageSize, sortOrder);
            Page<DoctorDTO> doctorsPage = doctorService.getAllDoctors(search, pageable);
            return ResponseEntity.ok(doctorsPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse(false, "Error fetching doctors"));
        }
    }
}
