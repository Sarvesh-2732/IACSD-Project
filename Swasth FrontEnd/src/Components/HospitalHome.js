import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { motion } from "framer-motion";
import DoctorsList from "./DoctorsList";

const HospitalHome = () => {
  const [hospital, setHospital] = useState(null);
  // Store only the current page of doctors
  const [doctors, setDoctors] = useState([]);
  // Pagination state
  const [currentPage, setCurrentPage] = useState(0); // zero-based index to match backend
  const [totalPages, setTotalPages] = useState(0);
  const pageSize = 6; // Number of doctors per page
  const [patients, setPatients] = useState([]);
  
  // Form state for adding doctor and report (omitted here for brevity)
  const [doctorFirstName, setDoctorFirstName] = useState("");
  const [doctorLastName, setDoctorLastName] = useState("");
  const [doctorEmail, setDoctorEmail] = useState("");
  const [doctorPassword, setDoctorPassword] = useState("");
  const [doctorMobileNumber, setDoctorMobileNumber] = useState("");
  const [doctorGender, setDoctorGender] = useState("");
  const [doctorAge, setDoctorAge] = useState("");
  const [doctorExperience, setDoctorExperience] = useState("");
  const [doctorDegree, setDoctorDegree] = useState("");
  const [doctorExpertiseField, setDoctorExpertiseField] = useState("");
  const [patientId, setPatientId] = useState("");
  const [report, setReport] = useState("");

  const navigate = useNavigate();

  // Function to fetch a page of doctors
  const fetchDoctorsPage = async (page) => {
    const hospitalId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");
    const headers = { Authorization: `Bearer ${token}` };
    try {
      const response = await axios.get(
        `http://localhost:3500/api/hospital/${hospitalId}/doctors?page=${page}&size=${pageSize}`,
        { headers }
      );
      // The backend returns a Page object (Spring Data Page)
      const data = response.data;
      setDoctors(data.content); // content holds the list of doctors for this page
      setCurrentPage(data.number); // current page (zero-based)
      setTotalPages(data.totalPages); // total pages available
    } catch (error) {
      console.error("Error fetching doctors:", error.response || error.message);
    }
  };

  useEffect(() => {
    const hospitalId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");
    if (!hospitalId || localStorage.getItem("userType") !== "hospital") {
      navigate("/hospital/login");
      return;
    }
    const headers = { Authorization: `Bearer ${token}` };

    // Fetch hospital details
    axios
      .get(`http://localhost:3500/api/hospital/details/${hospitalId}`, { headers })
      .then((res) => setHospital(res.data))
      .catch((err) => console.error("Error fetching hospital data", err));

    // Fetch the first page of doctors
    fetchDoctorsPage(0);

    // Fetch hospital patients
    axios
      .get(`http://localhost:3500/api/hospital/${hospitalId}/patients`, { headers })
      .then((res) => setPatients(res.data))
      .catch((err) => console.error("Error fetching patients", err));
  }, [navigate]);

  // Callback to remove a doctor (similar to before)
  const handleRemoveDoctor = async (doctorId) => {
    console.log("Attempting to remove doctor with id:", doctorId);
    const hospitalId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");

    if (!doctorId) {
      console.error("No doctor ID provided.");
      return;
    }

    try {
      await axios.delete(
        `http://localhost:3500/api/hospital/${hospitalId}/doctors/${doctorId}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      console.log("Doctor deleted successfully");
      // After deletion, re-fetch the current page (or reset to page 0)
      fetchDoctorsPage(currentPage);
    } catch (error) {
      console.error("Error deleting doctor:", error.response?.data || error.message);
    }
  };

  // Handler for page changes
  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
      fetchDoctorsPage(newPage);
    }
  };

  // (handleAddDoctor and handleAddReport remain similar to before; you may choose to refresh the page after adding a doctor)
  const handleAddDoctor = async (e) => {
    e.preventDefault();
    const hospitalId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");
    const doctorData = {
      firstName: doctorFirstName,
      lastName: doctorLastName,
      email: doctorEmail,
      password: doctorPassword,
      mobileNumber: doctorMobileNumber,
      gender: doctorGender,
      age: doctorAge,
      experience: doctorExperience,
      degree: doctorDegree,
      expertiseField: doctorExpertiseField,
      role: "DOCTOR"
    };

    try {
      const response = await axios.post(
        `http://localhost:3500/api/hospital/${hospitalId}/doctors`,
        doctorData,
        { 
          headers: { 
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
          } 
        }
      );

      if (response.status === 201) {
        // Option 1: Re-fetch the current page to include the new doctor
        fetchDoctorsPage(currentPage);
        // Option 2: Or, update your doctors state manually if the new doctor belongs on the current page.
        // Reset form fields
        setDoctorFirstName("");
        setDoctorLastName("");
        setDoctorEmail("");
        setDoctorPassword("");
        setDoctorMobileNumber("");
        setDoctorGender("");
        setDoctorAge("");
        setDoctorExperience("");
        setDoctorDegree("");
        setDoctorExpertiseField("");
      }
    } catch (error) {
      console.error("Error adding doctor", error.response?.data || error.message);
    }
  };

  const handleAddReport = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    try {
      await axios.post(
        `http://localhost:3500/api/patients/${patientId}/reports`,
        { details: report },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setPatientId("");
      setReport("");
    } catch (error) {
      console.error("Error adding report", error);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-blue-100 p-6">
      <div className="max-w-6xl mx-auto space-y-12">
        {/* Header Section */}
        <motion.div
          initial={{ opacity: 0, y: -30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.7 }}
          className="bg-gradient-to-r from-blue-600 to-indigo-600 p-8 rounded-3xl shadow-2xl text-white text-center"
        >
          <h1 className="text-5xl font-extrabold">
            Welcome to {hospital ? hospital.name : "Our Hospital"}
          </h1>
          <p className="mt-4 text-xl">
            Caring for our community with excellence and compassion.
          </p>
        </motion.div>

        {/* Hospital Information Section */}
        {hospital && (
          <motion.div
            initial={{ opacity: 0, x: -30 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.6 }}
            className="bg-white p-8 rounded-3xl shadow-xl"
          >
            <h2 className="text-4xl font-bold text-gray-800 mb-4">Hospital Details</h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div className="flex items-center space-x-2">
                <span role="img" aria-label="location" className="text-2xl">📍</span>
                <span className="text-lg text-gray-600">{hospital.location}</span>
              </div>
              <div className="flex items-center space-x-2">
                <span role="img" aria-label="email" className="text-2xl">✉️</span>
                <span className="text-lg text-gray-600">{hospital.email}</span>
              </div>
              {/* <div className="flex items-center space-x-2">
                <span role="img" aria-label="phone" className="text-2xl">📞</span>
                <span className="text-lg text-gray-600">{hospital.mobileNumber}</span>
              </div> */}
            </div>
          </motion.div>
        )}

        {/* Doctors Section */}
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.7 }}
        >
          {Array.isArray(doctors) && (
            <DoctorsList doctors={doctors} onRemoveDoctor={handleRemoveDoctor} />
          )}
          {/* Pagination Controls */}
          {totalPages > 1 && (
            <div className="flex justify-center mt-6 space-x-4">
              <button 
                onClick={() => handlePageChange(currentPage - 1)}
                disabled={currentPage === 0}
                className="px-4 py-2 bg-gray-200 text-gray-700 rounded hover:bg-gray-300 disabled:opacity-50"
              >
                Prev
              </button>
              <div className="px-4 py-2 bg-gray-100 text-gray-700">
                Page {currentPage + 1} of {totalPages}
              </div>
              <button 
                onClick={() => handlePageChange(currentPage + 1)}
                disabled={currentPage === totalPages - 1}
                className="px-4 py-2 bg-gray-200 text-gray-700 rounded hover:bg-gray-300 disabled:opacity-50"
              >
                Next
              </button>
            </div>
          )}
        </motion.div>

        {/* Forms Section */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-12">
          {/* Add Doctor Form Section */}
          <motion.div
            initial={{ opacity: 0, x: -30 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.6 }}
            className="bg-white p-8 rounded-3xl shadow-2xl"
          >
            <h3 className="text-3xl font-bold text-gray-800 mb-6">Add a New Doctor</h3>
            <form onSubmit={handleAddDoctor} className="space-y-4">
              {/* ... input fields for doctor information (same as before) ... */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <input
                  type="text"
                  placeholder="First Name"
                  value={doctorFirstName}
                  onChange={(e) => setDoctorFirstName(e.target.value)}
                  className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
                <input
                  type="text"
                  placeholder="Last Name"
                  value={doctorLastName}
                  onChange={(e) => setDoctorLastName(e.target.value)}
                  className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
              <input
                type="email"
                placeholder="Email"
                value={doctorEmail}
                onChange={(e) => setDoctorEmail(e.target.value)}
                className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                required
              />
              <input
                type="password"
                placeholder="Password"
                value={doctorPassword}
                onChange={(e) => setDoctorPassword(e.target.value)}
                className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                required
              />
              <input
                type="tel"
                placeholder="Mobile Number"
                value={doctorMobileNumber}
                onChange={(e) => setDoctorMobileNumber(e.target.value)}
                className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                required
              />
              <select
                value={doctorGender}
                onChange={(e) => setDoctorGender(e.target.value)}
                className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                required
              >
                <option value="">Select Gender</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <input
                  type="number"
                  placeholder="Age"
                  value={doctorAge}
                  onChange={(e) => setDoctorAge(e.target.value)}
                  className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
                <input
                  type="number"
                  placeholder="Experience (years)"
                  value={doctorExperience}
                  onChange={(e) => setDoctorExperience(e.target.value)}
                  className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                />
              </div>
              <input
                type="text"
                placeholder="Degree"
                value={doctorDegree}
                onChange={(e) => setDoctorDegree(e.target.value)}
                className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                required
              />
              <input
                type="text"
                placeholder="Expertise Field"
                value={doctorExpertiseField}
                onChange={(e) => setDoctorExpertiseField(e.target.value)}
                className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                required
              />
              <button
                type="submit"
                className="w-full py-3 bg-blue-600 text-white rounded-lg font-bold hover:bg-blue-700 transition duration-300"
              >
                Add Doctor
              </button>
            </form>
          </motion.div>

          {/* Add Patient Report Form Section */}
          <motion.div
            initial={{ opacity: 0, x: 30 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.6 }}
            className="bg-white p-8 rounded-3xl shadow-2xl"
          >
            <h3 className="text-3xl font-bold text-gray-800 mb-6">Add Patient Report</h3>
            <form onSubmit={handleAddReport} className="space-y-5">
              <input
                type="text"
                placeholder="Patient ID"
                value={patientId}
                onChange={(e) => setPatientId(e.target.value)}
                className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                required
              />
              <textarea
                placeholder="Report Details"
                value={report}
                onChange={(e) => setReport(e.target.value)}
                className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 h-40"
                required
              ></textarea>
              <button
                type="submit"
                className="w-full py-3 bg-green-600 text-white rounded-lg font-bold hover:bg-green-700 transition duration-300"
              >
                Add Report
              </button>
            </form>
          </motion.div>
        </div>
      </div>
    </div>
  );
};

export default HospitalHome;
