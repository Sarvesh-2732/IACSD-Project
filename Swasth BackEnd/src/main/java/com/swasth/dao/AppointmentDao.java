package com.swasth.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swasth.pojos.Appointment;

@Repository
public interface AppointmentDao extends JpaRepository<Appointment, Integer> {
    
    // Find all appointments for a specific doctor
    List<Appointment> findByDoctor_DoctorsId(int doctorId);

    // Find all appointments for a specific patient
    List<Appointment> findByPatient_PatientId(int patientId);
}
