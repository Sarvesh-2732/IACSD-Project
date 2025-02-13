import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { motion } from "framer-motion";

const PatientHome = () => {
  const [patient, setPatient] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPatientDetails = async () => {
      try {
        const patientId = localStorage.getItem("userId");
        const token = localStorage.getItem("token");

        if (!patientId || !token) {
          navigate("/login/patient");
          return;
        }

        const response = await axios.get(`http://localhost:3500/api/patient/details/${patientId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setPatient(response.data);
      } catch (error) {
        console.error("Error fetching patient details:", error);
      }
    };

    fetchPatientDetails();
  }, [navigate]);

  if (!patient) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-100 to-blue-100">
        <p className="text-center text-xl font-semibold text-gray-600">
          Loading patient details...
        </p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-100 to-blue-100 p-6">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="max-w-4xl mx-auto bg-white p-8 rounded-2xl shadow-2xl"
      >
        <div className="mb-8 text-center">
          <h2 className="text-4xl font-bold text-gray-800 mb-2">
            Welcome, {patient.firstName}
          </h2>
          <p className="text-lg text-gray-700">
            Email: {patient.email} | Phone: {patient.phone} | Age: {patient.age}
          </p>
        </div>

        {/* Upcoming Appointments */}
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.3, duration: 0.5 }}
          className="mb-8"
        >
          <h3 className="text-3xl font-semibold text-gray-800 mb-4">
            Your Upcoming Appointments
          </h3>
          {patient.appointments && patient.appointments.length > 0 ? (
            <ul className="list-disc list-inside space-y-2">
              {patient.appointments.map((appointment) => (
                <li key={`${appointment.appointmentDate}-${appointment.appointmentTime}`} className="text-gray-700">
                  📅 {appointment.appointmentDate} at 🕒 {appointment.appointmentTime} - Dr. {appointment.doctorName} ({appointment.status})
                </li>
              ))}
            </ul>
          ) : (
            <p className="text-gray-600">No upcoming appointments.</p>
          )}
        </motion.div>

        {/* Medical Records (Assuming Backend Also Returns Them) */}
        {patient.medicalRecords && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.5, duration: 0.5 }}
          >
            <h3 className="text-3xl font-semibold text-gray-800 mb-4">
              Your Medical Records
            </h3>
            {patient.medicalRecords.length > 0 ? (
              <ul className="list-disc list-inside space-y-2">
                {patient.medicalRecords.map((record, index) => (
                  <li key={index} className="text-gray-700">
                    {record.date} - {record.description}
                  </li>
                ))}
              </ul>
            ) : (
              <p className="text-gray-600">No medical records available.</p>
            )}
          </motion.div>
        )}
      </motion.div>
    </div>
  );
};

export default PatientHome;
