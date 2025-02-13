package com.swasth.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.swasth.services.DoctorService;
import com.swasth.services.HospitalService;
import com.swasth.services.PatientService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTTokenValidator extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final HospitalService hospitalService;

    // Constructor for Dependency Injection
    public JWTTokenValidator(JwtUtil jwtUtil,DoctorService doctorService, 
                             PatientService patientService, HospitalService hospitalService) {
        this.jwtUtil = jwtUtil;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.hospitalService = hospitalService;
    }

	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = jwtUtil.getClaims(token);
                String username = claims.getSubject();
                String role = claims.get("role", String.class);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = getUserDetails(username, role);
                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

            } catch (ExpiredJwtException | SignatureException | MalformedJwtException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or Expired JWT Token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

	private UserDetails getUserDetails(String userId, String role) {
	    try {
	        int id = Integer.parseInt(userId);
	        
	        // Create authority list with the role
	        List<SimpleGrantedAuthority> authorities = 
	            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

	        switch (role) {
	            case "DOCTOR":
	                return doctorService.readDoctorsDetails(id) != null ? 
	                    new User(userId, "", authorities) : null;
	            case "PATIENT":
	                return patientService.readPatientDetails(id) != null ? 
	                    new User(userId, "", authorities) : null;
	            case "HOSPITAL":
	                return hospitalService.readHospitalDetails(id) != null ? 
	                    new User(userId, "", authorities) : null;
	            default:
	                return null;
	        }
	    } catch (NumberFormatException e) {
	        return null;
	    }
	}
}
