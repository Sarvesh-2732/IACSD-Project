package com.swasth.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.swasth.security.JWTTokenValidator;
import com.swasth.security.JwtUtil;
import com.swasth.services.AppointmentService;
import com.swasth.services.DoctorService;
import com.swasth.services.HospitalService;
import com.swasth.services.PatientService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtUtil jwtUtil;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final HospitalService hospitalService;
    private final AppointmentService appointmentService;
    
    public SecurityConfig(JwtUtil jwtUtil, 
                         DoctorService doctorService,
                         PatientService patientService,
                         HospitalService hospitalService,
                         AppointmentService appointmentService) {
        this.jwtUtil = jwtUtil;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.hospitalService = hospitalService;
        this.appointmentService = appointmentService;
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/auth/**").permitAll()
//                .requestMatchers("/api/doctor/**").hasRole("DOCTOR")
//                .requestMatchers("/api/patient/**").hasRole("PATIENT")
//                .requestMatchers("/api/hospital/**").hasRole("HOSPITAL")
//                .anyRequest().authenticated()
//            )
//            .addFilterBefore(new JWTTokenValidator(jwtUtil, doctorService, patientService, hospitalService), 
//                           UsernamePasswordAuthenticationFilter.class)
//            .csrf(csrf -> csrf.disable())
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()));
//
//        return http.build();
//    }
    
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .authorizeHttpRequests(auth -> auth
//                // Temporarily permit all requests for testing
//                .anyRequest().permitAll()
//                // Comment out role-based security temporarily
//                /*
//                .requestMatchers("/api/auth/**").permitAll()
//                .requestMatchers("/api/doctor/**").hasRole("DOCTOR")
//                .requestMatchers("/api/patient/**").hasRole("PATIENT")
//                .requestMatchers("/api/hospital/**").hasRole("HOSPITAL")
//                .anyRequest().authenticated()
//                */
//            )
//            // Comment out JWT filter temporarily
//            /*
//            .addFilterBefore(new JWTTokenValidator(jwtUtil, doctorService, patientService, hospitalService), 
//                             UsernamePasswordAuthenticationFilter.class)
//            */
//            .csrf(csrf -> csrf.disable())
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()));
//        return http.build();
//    }
   
//        @Value("${google.client.id}")
//        private String googleClientId;
//        
//        @Bean
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            http
//                .sessionManagement(management -> 
//                    management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                    .requestMatchers(HttpMethod.POST, "/api/auth/oauth2/login/success").permitAll()
//                    .requestMatchers("/api/auth/patient/**").permitAll()
//                    .requestMatchers("/api/doctor/**").hasRole("DOCTOR")
//                    .requestMatchers("/api/patient/**").hasRole("PATIENT")
//                    .requestMatchers("/api/hospital/**").hasRole("HOSPITAL")
//                    .anyRequest().authenticated()
//                )
//                .addFilterBefore(
//                    new JWTTokenValidator(jwtUtil, doctorService, patientService, hospitalService),
//                    UsernamePasswordAuthenticationFilter.class
//                )
//                .csrf(csrf -> csrf.disable())
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
//            
//            return http.build();
//        }
//
//        @Bean
//        public CorsConfigurationSource corsConfigurationSource() {
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//            config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//            config.setExposedHeaders(Arrays.asList("Authorization"));
//            config.setAllowCredentials(true);
//            
//            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//            source.registerCorsConfiguration("/**", config);
//            return source;
//        }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(management -> 
                management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/api/auth/patient/register").permitAll()
            	.requestMatchers("/api/auth/patient/login").permitAll()
            	.requestMatchers("/api/auth/oauth2/login/success").permitAll()
            	.requestMatchers("/api/auth/**").permitAll()
            	.requestMatchers("/api/doctors/**").permitAll()            	
                .requestMatchers("/api/test-auth/**").permitAll()
                .requestMatchers("/api/appointments/**").permitAll()            	                
                .requestMatchers("/api/doctor/**").hasRole("DOCTOR")
                .requestMatchers("/api/patient/**").hasRole("PATIENT")
                .requestMatchers("/api/hospital/**").hasRole("HOSPITAL")
                .anyRequest().authenticated()
            )            
            .addFilterBefore(new JWTTokenValidator(jwtUtil, doctorService, 
                patientService, hospitalService), 
                UsernamePasswordAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

//    private CorsConfigurationSource corsConfigurationSource() {
//        return request -> {
//            CorsConfiguration cfg = new CorsConfiguration();
//            cfg.setAllowedOrigins(List.of("*"));
//            cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
//            cfg.setAllowCredentials(true);
//            cfg.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//            cfg.setExposedHeaders(List.of("Authorization"));
//            cfg.setMaxAge(3600L);
//            return cfg;
//        };
//    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // Explicitly allow frontend URL
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Include OPTIONS
        config.setAllowCredentials(true); // Required if sending cookies or Authorization headers
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setMaxAge(3600L); // Cache preflight requests for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}