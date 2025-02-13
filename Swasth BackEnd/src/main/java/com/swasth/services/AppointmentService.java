package com.swasth.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.swasth.dao.AppointmentDao;
import com.swasth.dao.DoctorDao;
import com.swasth.dao.PatientDao;
import com.swasth.pojos.Appointment;
import com.swasth.pojos.Doctors;
import com.swasth.pojos.Patient;
import com.swasth.dto.AppointmentRequest;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentDao appointmentRepository;

    @Autowired
    private DoctorDao doctorsRepository;

    @Autowired
    private PatientDao patientRepository;
    
    @Autowired
    private EmailService emailService;

    @Transactional
    public void bookAppointment(AppointmentRequest request) {
        // Find the doctor
        Doctors doctor = doctorsRepository.findById(request.getDoctorId())
            .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Find the patient
        Patient patient = patientRepository.findById(request.getPatientId())
            .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Create a new appointment
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setStatus(request.getStatus());

        // Save the appointment
        appointmentRepository.save(appointment);

        // Prepare details for the email
        String doctorName = doctor.getFirstName() + " " + doctor.getLastName();
        String appointmentDate = request.getAppointmentDate().toString();  // Format as desired
        String appointmentTime = request.getAppointmentTime().toString();  // Format as desired

        // Send appointment confirmation email to the patient
        emailService.sendAppointmentConfirmation(patient.getEmail(), doctorName, appointmentDate, appointmentTime);
    }
    
    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        return appointmentRepository.findByDoctor_DoctorsId(doctorId);
    }
}
