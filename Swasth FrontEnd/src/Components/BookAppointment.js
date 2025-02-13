import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { motion } from "framer-motion";

const BookAppointment = () => {
  const { doctorId } = useParams(); // Get doctorId from URL
  const navigate = useNavigate();
  const [appointmentDate, setAppointmentDate] = useState("");
  const [appointmentTime, setAppointmentTime] = useState("");
  const [error, setError] = useState(null);

  // Get logged-in patient ID from localStorage (Assumption: It's stored after login)
  const patientId = localStorage.getItem("userId");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    try {
      await axios.post("http://localhost:3500/api/appointments/book", {
        doctorId,
        patientId,
        appointmentDate,
        appointmentTime,
        status: "Scheduled",
      });

      alert("Appointment booked successfully!");
      navigate("/patient/home"); // Redirect to the Patient home page where it has a list of his/her appointment
    } catch (err) {
      setError(err.response?.data?.message || "Failed to book appointment");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-100 to-purple-200 p-6">
      <motion.div
        initial={{ opacity: 0, scale: 0.8, y: 50 }}
        animate={{ opacity: 1, scale: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="bg-white p-8 rounded-2xl shadow-2xl w-full max-w-md"
      >
        <h2 className="text-3xl font-bold text-center text-gray-800 mb-6">
          Book Appointment
        </h2>
        {error && (
          <p className="text-red-500 text-center mb-4">{error}</p>
        )}
        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block text-gray-700 font-medium mb-2">
              Appointment Date
            </label>
            <input
              type="date"
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
              value={appointmentDate}
              onChange={(e) => setAppointmentDate(e.target.value)}
              required
            />
          </div>
          <div>
            <label className="block text-gray-700 font-medium mb-2">
              Appointment Time
            </label>
            <input
              type="time"
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
              value={appointmentTime}
              onChange={(e) => setAppointmentTime(e.target.value)}
              required
            />
          </div>
          <button
            type="submit"
            className="w-full py-3 bg-blue-500 text-white rounded-lg font-semibold transition transform hover:scale-105 hover:bg-blue-600"
          >
            Book Appointment
          </button>
        </form>
      </motion.div>
    </div>
  );
};

export default BookAppointment;
