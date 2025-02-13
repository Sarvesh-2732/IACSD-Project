package com.swasth.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    // Inject the sender email address from properties
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    public void sendDoctorCredentials(String toEmail, String plainPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Your Doctor Account Credentials");
        message.setText("Dear Doctor,\n\nYour account has been created successfully.\n\n" +
                        "Email: " + toEmail + "\n" +
                        "Password: " + plainPassword + "\n\n" +
                        "Please change your password after your first login.\n\n" +
                        "Regards,\nHospital Team");
        mailSender.send(message);
    }
    
    public void sendAppointmentConfirmation(String patientEmail, String doctorName, String appointmentDate, String appointmentTime) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(patientEmail);
        message.setSubject("Appointment Confirmation");
        message.setText("Dear Patient,\n\nYour appointment with Dr. " + doctorName +
                        " has been scheduled on " + appointmentDate + " at " + appointmentTime +
                        ".\n\nThank you for choosing our services.\n\nRegards,\nHospital Team");
        mailSender.send(message);
    }
}
