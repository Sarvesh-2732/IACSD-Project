package com.swasth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.swasth.dto.DoctorHospitalDTO;
import com.swasth.dto.HospitalDetailsDTO;
import com.swasth.exception.ApiResponse;
import com.swasth.exception.HospitalException;
import com.swasth.exception.ResourceNotFoundException;
import com.swasth.pojos.Doctors;
import com.swasth.pojos.Hospital;
import com.swasth.pojos.Rooms;
import com.swasth.services.DoctorService;
import com.swasth.services.EmailService;
import com.swasth.services.HospitalService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hospital")  // Updated to match SecurityConfig path pattern
@CrossOrigin(origins = "http://localhost:3000")
public class HospitalController {

	@Autowired
	private PasswordEncoder passwordEncoder;
    private final HospitalService hospitalService;
    private final DoctorService doctorService;  
    
    private EmailService emailService;
    @Autowired
    public HospitalController(HospitalService hospitalService,DoctorService doctorService,EmailService emailService) {
        this.hospitalService = hospitalService;
        this.doctorService=doctorService;
        this.emailService=emailService;
    }

    // Helper method to get current authenticated hospital's ID
    private int getCurrentHospitalId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Integer.parseInt(authentication.getName());
    }

    @PostMapping("/add")
    public ResponseEntity<String> insertHospital(@Valid @RequestBody Hospital hospital) {
        try {
            boolean insertOperation = hospitalService.addHospital(hospital);
            return insertOperation ? 
                ResponseEntity.ok("Hospital added successfully") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add hospital");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
        }
    }

//    @GetMapping("/details")
//    public ResponseEntity<Hospital> getCurrentHospitalDetails() {
//        try {
//            int hospitalId = getCurrentHospitalId();
//            HospitalDetailsDTO hospital = hospitalService.readHospitalDetails(hospitalId);
//            return hospital != null ? 
//                ResponseEntity.ok(hospital) : 
//                ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @GetMapping("/details/{id}")
    public ResponseEntity<HospitalDetailsDTO> getHospitalDetails(@PathVariable int id) {
        try {
            // Verify if the requesting hospital is accessing its own details
            int currentHospitalId = getCurrentHospitalId();
            if (currentHospitalId != id) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            HospitalDetailsDTO hospital = hospitalService.readHospitalDetails(id);
            if (hospital == null) {
                return ResponseEntity.notFound().build();
            }

            // Convert to DTO before sending
            return ResponseEntity.ok(hospital);
        } catch (Exception e) {
            throw new HospitalException("Error retrieving hospital details");
        }
    }
    
//    @GetMapping("/{hospitalId}/doctors")
//    public ResponseEntity<?> getDoctorsByHospital(@PathVariable int hospitalId) {
//        try {
//            List<DoctorDTO> doctors = hospitalService.getDoctorsByHospital(hospitalId);
//            return ResponseEntity.ok(doctors);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(new ApiResponse(false, e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ApiResponse(false, "Error fetching doctors"));
//        }
//    }
    
    @GetMapping("/{hospitalId}/doctors")
    public ResponseEntity<?> getDoctorsByHospital(
            @PathVariable int hospitalId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            // Assume hospitalService.getDoctorsByHospital now returns a Page<DoctorHospitalDTO>
            Page<DoctorDTO> doctorsPage = hospitalService.getDoctorsByHospital(hospitalId, pageable);
            return ResponseEntity.ok(doctorsPage);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse(false, "Error fetching doctors"));
        }
    }


//    @PostMapping("/{hospitalId}/doctors")
//    public ResponseEntity<DoctorHospitalDTO> addDoctorToHospital(
//            @PathVariable int hospitalId,
//            @Valid @RequestBody Doctors doctor) {
//        try {
//            // Verify if the requesting hospital is adding to their own hospital
//            int currentHospitalId = getCurrentHospitalId();
//            if (currentHospitalId != hospitalId) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//            }
//
//            // Check if doctor with email already exists
//            Doctors existingDoctorEmail = doctorService.findByEmail(doctor.getEmail());
//            if (existingDoctorEmail != null) {
//                return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(null); // Email already exists
//            }
//
//            // Check if doctor with mobile number already exists
//            Doctors existingDoctorMobile = doctorService.findByMobileNumber(doctor.getMobileNumber());
//            if (existingDoctorMobile != null) {
//                return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(null); // Mobile number already exists
//            }
//
//            // Set the hospital for the doctor
//            Hospital hospital = hospitalService.findById(hospitalId);                
//            doctor.setHospital(hospital);
//            
//            // Set role and encrypt password
//            doctor.setRole("DOCTOR");
//            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
//
//            // Save the doctor
//            boolean isAdded = doctorService.addDoctor(doctor);
//            if (!isAdded) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//            }
//
//            // Convert to DTO and return
//            DoctorHospitalDTO doctorHospitalDTO = convertToDTO(doctor);
//            return ResponseEntity.status(HttpStatus.CREATED).body(doctorHospitalDTO);
//
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
    @PostMapping("/{hospitalId}/doctors")
    public ResponseEntity<DoctorHospitalDTO> addDoctorToHospital(
            @PathVariable int hospitalId,
            @Valid @RequestBody Doctors doctor) {
        try {
            int currentHospitalId = getCurrentHospitalId();
            if (currentHospitalId != hospitalId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            // Check if doctor with email or mobile number already exists
            Doctors existingDoctorEmail = doctorService.findByEmail(doctor.getEmail());
            if (existingDoctorEmail != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Email already exists
            }
            Doctors existingDoctorMobile = doctorService.findByMobileNumber(doctor.getMobileNumber());
            if (existingDoctorMobile != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Mobile number already exists
            }
            
            // Set the hospital for the doctor
            Hospital hospital = hospitalService.findById(hospitalId);                
            doctor.setHospital(hospital);
            
            // Capture the plain text password before encoding
            String plainPassword = doctor.getPassword();
            
            // Set role and encrypt password
            doctor.setRole("DOCTOR");
            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
            
            // Save the doctor
            boolean isAdded = doctorService.addDoctor(doctor);
            if (!isAdded) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            
            // Send email with credentials
            emailService.sendDoctorCredentials(doctor.getEmail(), plainPassword);
            
            // Convert to DTO and return
            DoctorHospitalDTO doctorHospitalDTO = convertToDTO(doctor);
            return ResponseEntity.status(HttpStatus.CREATED).body(doctorHospitalDTO);
            
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    private DoctorHospitalDTO convertToDTO(Doctors doctor) {
        DoctorHospitalDTO dto = new DoctorHospitalDTO();
        dto.setDoctorsId(doctor.getDoctorsId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setEmail(doctor.getEmail());
        dto.setMobileNumber(doctor.getMobileNumber());
        dto.setGender(doctor.getGender());
        dto.setAge(doctor.getAge());
        dto.setExperience(doctor.getExperience());
        dto.setDegree(doctor.getDegree());
        dto.setExpertiseField(doctor.getExpertiseField());
        return dto;
    }
    
    @PutMapping("/update")
    public ResponseEntity<String> updateHospitalDetails(@Valid @RequestBody Hospital hospital) {
        try {
            // Ensure hospital can only update its own details
            int currentHospitalId = getCurrentHospitalId();
            if (hospital.getHospitalId() != currentHospitalId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only update your own details");
            }

            Hospital updatedHospital = hospitalService.updateHospitalDetails(hospital);
            return ResponseEntity.ok("Hospital details updated successfully");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Validation failed: " + ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteHospital(@PathVariable int id) {
        try {
            // Ensure hospital can only delete its own account
            int currentHospitalId = getCurrentHospitalId();
            if (id != currentHospitalId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only delete your own account");
            }

            boolean deleted = hospitalService.deleteHospital(id);
            return deleted ? 
                ResponseEntity.ok("Hospital deleted successfully") :
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Hospital not found with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred");
        }
    }
    
    @DeleteMapping("/{hospitalId}/doctors/{doctorId}")
    public ResponseEntity<?> deleteDoctor(@PathVariable("hospitalId") int hospitalId,
                                          @PathVariable("doctorId") int doctorId) {
        try {
            boolean deleted = doctorService.deleteDoctor(doctorId);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponse(true, "Doctor deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(new ApiResponse(false, "Doctor not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse(false, "Error deleting doctor: " + e.getMessage()));
        }
    }
    
    // Room management endpoints
    @PostMapping("/rooms/add")
    public ResponseEntity<String> addRoom(@Valid @RequestBody Rooms room) {
        try {
            int hospitalId = getCurrentHospitalId();
            boolean roomAdded = hospitalService.addRoomToHospital(hospitalId, room);
            return roomAdded ? 
                ResponseEntity.ok("Room added successfully") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add room");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred");
        }
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<Rooms>> getCurrentHospitalRooms() {
        try {
            int hospitalId = getCurrentHospitalId();
            List<Rooms> rooms = hospitalService.getRoomsByHospital(hospitalId);
            return !rooms.isEmpty() ? 
                ResponseEntity.ok(rooms) : 
                ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/rooms/update")
    public ResponseEntity<String> updateRoom(@Valid @RequestBody Rooms room) {
        try {
            int currentHospitalId = getCurrentHospitalId();
            if (room.getHospital().getHospitalId() != currentHospitalId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only update rooms in your hospital");
            }

            boolean updated = hospitalService.updateRoomDetails(room);
            return updated ? 
                ResponseEntity.ok("Room updated successfully") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update room");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable int roomId) {
        try {
            // Verify room belongs to current hospital before deletion
            int currentHospitalId = getCurrentHospitalId();
            if (!hospitalService.isRoomBelongsToHospital(roomId, currentHospitalId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only delete rooms in your hospital");
            }

            boolean deleted = hospitalService.deleteRoom(roomId);
            return deleted ? 
                ResponseEntity.ok("Room deleted successfully") :
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Room not found with ID: " + roomId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred");
        }
    }
}