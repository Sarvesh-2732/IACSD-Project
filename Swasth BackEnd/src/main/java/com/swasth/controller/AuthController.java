package com.swasth.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.swasth.dto.AuthResponse;
import com.swasth.dto.DoctorRegistration;
import com.swasth.dto.ErrorResponse;
import com.swasth.dto.GooglePatientDTO;
import com.swasth.dto.GoogleTokenRequest;
import com.swasth.dto.HospitalRegistration;
import com.swasth.dto.LoginRequest;
import com.swasth.dto.PatientRegistration;
import com.swasth.pojos.Doctors;
import com.swasth.pojos.Hospital;
import com.swasth.pojos.Patient;
import com.swasth.security.JwtUtil;
import com.swasth.services.DoctorService;
import com.swasth.services.HospitalService;
import com.swasth.services.PatientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
//@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") 
public class AuthController {
	private final JwtUtil jwtUtil;
	private final DoctorService doctorService;
	private final PatientService patientService;
	private final HospitalService hospitalService;
	private final PasswordEncoder passwordEncoder;
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	public AuthController(JwtUtil jwtUtil, DoctorService doctorService, PatientService patientService,
			HospitalService hospitalService, PasswordEncoder passwordEncoder) {
		this.jwtUtil = jwtUtil;
		this.doctorService = doctorService;
		this.patientService = patientService;
		this.hospitalService = hospitalService;
		this.passwordEncoder = passwordEncoder;
	}

	// Doctor Login
	@PostMapping("/doctor/login")
	public ResponseEntity<AuthResponse> doctorLogin(@Valid @RequestBody LoginRequest loginRequest) {
	    logger.info("Login attempt for email: {}", loginRequest.getEmail());

	    try {
	        Doctors doctor = doctorService.findByEmail(loginRequest.getEmail());

	        if (doctor == null) {
	            logger.warn("Login failed: Email not found - {}", loginRequest.getEmail());
	            return ResponseEntity.badRequest().body(new AuthResponse(null, null, 0, "Email not found"));
	        }

	        if (!passwordEncoder.matches(loginRequest.getPassword(), doctor.getPassword())) {
	            logger.warn("Login failed: Incorrect password for email - {}", loginRequest.getEmail());
	            return ResponseEntity.badRequest().body(new AuthResponse(null, null, 0, "Incorrect password"));
	        }

	        String token = jwtUtil.generateToken(doctor.getDoctorsId(), "DOCTOR");

	        logger.info("Successful login. DoctorId: {}", doctor.getDoctorsId());

	        return ResponseEntity.ok(new AuthResponse(token, "DOCTOR", doctor.getDoctorsId(), "Login successful"));

	    } catch (Exception e) {
	        logger.error("Login error for email: {}", loginRequest.getEmail(), e);
	        return ResponseEntity.badRequest().body(new AuthResponse(null, null, 0, "Login failed: " + e.getMessage()));
	    }
	}


	@PostMapping("/patient/login")
	public ResponseEntity<AuthResponse> patientLogin(@Valid @RequestBody LoginRequest loginRequest) {
	    logger.info("Login attempt for email: {}", loginRequest.getEmail());
	   
	    try {
	        Patient patient = patientService.findByEmail(loginRequest.getEmail());

	        if (patient == null) {
	            logger.warn("Login failed: Email not found - {}", loginRequest.getEmail());
	            return ResponseEntity.badRequest().body(new AuthResponse(null, null, 0, "Email not found"));
	        }
	        
	        if (!passwordEncoder.matches(loginRequest.getPassword(), patient.getPassword())) {
	            logger.warn("Login failed: Incorrect password for email - {}", loginRequest.getEmail());
	            return ResponseEntity.badRequest().body(new AuthResponse(null, null, 0, "Incorrect password"));
	        }

	        String token = jwtUtil.generateToken(patient.getPatientId(), "PATIENT");
	        
	        logger.info("Successful login. PatientId: {}", patient.getPatientId());

	        // Ensure correct userId is returned
	        return ResponseEntity.ok(new AuthResponse(token, "PATIENT", patient.getPatientId(), "Login successful"));

	    } catch (Exception e) {
	        logger.error("Login error for email: {}", loginRequest.getEmail(), e);
	        return ResponseEntity.badRequest().body(new AuthResponse(null, null, 0, "Login failed: " + e.getMessage()));
	    }
	}
	
	 @PostMapping("/oauth2/login/success")
	    public ResponseEntity<?> googleLogin(@RequestBody GoogleTokenRequest tokenRequest) {
	        try {
	            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
	                new NetHttpTransport(), 
	                new GsonFactory()
	            )
	            .setAudience(Collections.singletonList("809590510532-mbl666uasimacu36l0acqao1g9544dj4.apps.googleusercontent.com"))
	            .build();

	            GoogleIdToken idToken = verifier.verify(tokenRequest.getToken());
	            if (idToken == null) {
	                return ResponseEntity
	                    .status(HttpStatus.UNAUTHORIZED)
	                    .body(new ErrorResponse("Invalid Google token"));
	            }

	            GoogleIdToken.Payload payload = idToken.getPayload();
	            String email = payload.getEmail();
	            
	            // Add email verification check
	            if (!payload.getEmailVerified()) {
	                return ResponseEntity
	                    .status(HttpStatus.UNAUTHORIZED)
	                    .body(new ErrorResponse("Email not verified"));
	            }

	            Patient patient = patientService.findByEmail(email);
	            if (patient == null) {
	                GooglePatientDTO googlePatientDTO = new GooglePatientDTO(
	                    email, 
	                    (String) payload.get("name")
	                );
	                patient = patientService.addGooglePatient(googlePatientDTO);
	            }

	            String jwtToken = jwtUtil.generateToken(patient.getPatientId(), "PATIENT");
	            
	            return ResponseEntity.ok(new AuthResponse(
	                jwtToken,
	                "PATIENT",
	                patient.getPatientId(),
	                "Google login successful"
	            ));
	            
	        } catch (Exception e) {
	            logger.error("Google authentication error", e);
	            return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ErrorResponse("Authentication failed: " + e.getMessage()));
	        }
	    }
	// Hospital Login
	@PostMapping("/hospital/login")
	public ResponseEntity<AuthResponse> hospitalLogin(@Valid @RequestBody LoginRequest loginRequest) {
	    logger.info("Login attempt for email: {}", loginRequest.getEmail());

	    try {
	        Hospital hospital = hospitalService.findByEmail(loginRequest.getEmail());

	        if (hospital == null) {
	            logger.warn("Login failed: Email not found - {}", loginRequest.getEmail());
	            return ResponseEntity.badRequest().body(new AuthResponse(null, null, 0, "Email not found"));
	        }

	        if (!passwordEncoder.matches(loginRequest.getPassword(), hospital.getPassword())) {
	            logger.warn("Login failed: Incorrect password for email - {}", loginRequest.getEmail());
	            return ResponseEntity.badRequest().body(new AuthResponse(null, null, 0, "Incorrect password"));
	        }

	        String token = jwtUtil.generateToken(hospital.getHospitalId(), "HOSPITAL");

	        logger.info("Successful login. HospitalId: {}", hospital.getHospitalId());

	        return ResponseEntity.ok(new AuthResponse(token, "HOSPITAL", hospital.getHospitalId(), "Login successful"));

	    } catch (Exception e) {
	        logger.error("Login error for email: {}", loginRequest.getEmail(), e);
	        return ResponseEntity.badRequest().body(new AuthResponse(null, null, 0, "Login failed: " + e.getMessage()));
	    }
	}


	// Doctor Registration
	@Transactional
	@PostMapping("/doctor/register")
	public ResponseEntity<AuthResponse> registerDoctor(@Valid @RequestBody DoctorRegistration request) {
		try {
			if (doctorService.findByEmail(request.getEmail()) != null) {
				return ResponseEntity.badRequest().body(new AuthResponse(null, null, 0, "Email already registered"));
			}

			if (doctorService.findByMobileNumber(Long.parseLong(request.getMobileNumber())) != null) {
				return ResponseEntity.badRequest()
						.body(new AuthResponse(null, null, 0, "Mobile number already registered"));
			}

//			Hospital hospital = hospitalService.findById(request.getHospitalId());
//			if (hospital == null) {
//				return ResponseEntity.badRequest().body(new AuthResponse(null, null, 0, "Invalid hospital ID"));
//			}

			Doctors doctor = new Doctors();
			doctor.setFirstName(request.getFirstName());
			doctor.setLastName(request.getLastName());
			doctor.setEmail(request.getEmail());
			doctor.setPassword(passwordEncoder.encode(request.getPassword()));
			doctor.setMobileNumber(Long.parseLong(request.getMobileNumber()));
			doctor.setGender(request.getGender());
			doctor.setAge(request.getAge());
			doctor.setExperience(request.getExperience());
			doctor.setDegree(request.getDegree());
			doctor.setExpertiseField(request.getExpertiseField());
//			doctor.setHospital(hospital);
			doctor.setRole("DOCTOR");

			Doctors savedDoctor = doctorService.save(doctor);
			String token = jwtUtil.generateToken(savedDoctor.getDoctorsId(), "DOCTOR");

			return ResponseEntity
					.ok(new AuthResponse(token, "DOCTOR", savedDoctor.getDoctorsId(), "Registration successful"));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(new AuthResponse(null, null, 0, "Registration failed: " + e.getMessage()));
		}
	}

	// Patient Registration
	@Transactional
	@PostMapping("/patient/register")
	public ResponseEntity<AuthResponse> registerPatient(@Valid @RequestBody PatientRegistration request) {
		try {
			logger.info("Received registration request: {}", request);
			if (patientService.findByEmail(request.getEmail()) != null) {
				return ResponseEntity.badRequest().body(new AuthResponse(null, null, 0, "Email already registered"));
			}

			if (patientService.findByMobileNumber(request.getMobileNumber()) != null) {
				return ResponseEntity.badRequest()
						.body(new AuthResponse(null, null, 0, "Mobile number already registered"));
			}

			Patient patient = new Patient();
			patient.setFirstName(request.getFirstName());
			patient.setLastName(request.getLastName());
			patient.setEmail(request.getEmail());
			patient.setPassword(passwordEncoder.encode(request.getPassword()));
			patient.setMobileNumber(request.getMobileNumber());
			patient.setAge(request.getAge());
			patient.setGender(request.getGender());
			patient.setWeight(BigDecimal.valueOf(request.getWeight())); 
			patient.setHeight(BigDecimal.valueOf(request.getHeight()));
			patient.setRole("PATIENT");
			patient.setRegistrationDate(LocalDate.now()); // Fixed Date Issue

			Patient savedPatient = patientService.save(patient);
			String token = jwtUtil.generateToken(savedPatient.getPatientId(), "PATIENT");

			return ResponseEntity
					.ok(new AuthResponse(token, "PATIENT", savedPatient.getPatientId(), "Registration successful"));
		} catch (Exception e) {
			 logger.error("Registration error", e);
			return ResponseEntity.badRequest()
					.body(new AuthResponse(null, null, 0, "Registration failed: " + e.getMessage()));
		}
	}

	// Hospital Registration
	@Transactional
	@PostMapping("/hospital/register")
	public ResponseEntity<AuthResponse> registerHospital(@Valid @RequestBody HospitalRegistration request) {
	    logger.info("Received hospital registration request: {}", request);

	    try {
	        // Check if the email is already registered
	        if (hospitalService.findByEmail(request.getEmail()) != null) {
	            logger.warn("Registration failed: Email already registered - {}", request.getEmail());
	            return ResponseEntity.badRequest().body(new AuthResponse(null, null, 0, "Email already registered"));
	        }

	        // Create a new hospital entity
	        Hospital hospital = new Hospital();
	        hospital.setName(request.getName());
	        hospital.setEmail(request.getEmail());
	        hospital.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
	        hospital.setLocation(request.getLocation());
	        hospital.setSpeciality(request.getSpeciality());
	        hospital.setTotalRooms(request.getTotalRooms());
	        hospital.setTestsFacility(request.getTestsFacility());
	        hospital.setTreatmentFacility(request.getTreatmentFacility());
	        hospital.setRole("HOSPITAL");
	        hospital.setRegistrationDate(LocalDate.now());

	        // Save the hospital to the database
	        Hospital savedHospital = hospitalService.save(hospital);
	        
	        // Generate JWT token
	        String token = jwtUtil.generateToken(savedHospital.getHospitalId(), "HOSPITAL");

	        logger.info("Hospital registered successfully. HospitalId: {}", savedHospital.getHospitalId());

	        return ResponseEntity.ok(new AuthResponse(token, "HOSPITAL", savedHospital.getHospitalId(), "Registration successful"));

	    } catch (Exception e) {
	        logger.error("Hospital registration failed for email: {}", request.getEmail(), e);
	        return ResponseEntity.badRequest().body(new AuthResponse(null, null, 0, "Registration failed: " + e.getMessage()));
	    }
	}

}