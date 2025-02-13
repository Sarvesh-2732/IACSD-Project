import React, { useState } from "react";
import axios from "axios";

const PatientProfileForm = ({ userDetails, setUserDetails, setMessage, setError }) => {
  const [patient, setPatient] = useState({
    patientId: userDetails.patientId,
    firstName: userDetails.firstName,
    lastName: userDetails.lastName,
    email: userDetails.email,
    mobileNumber: userDetails.mobileNumber,
    age: userDetails.age,
    password: "",
  });

  const handleChange = (e) => {
    setPatient({ ...patient, [e.target.name]: e.target.value });
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    setMessage("");
    setError("");
    try {
      const token = localStorage.getItem("token");
      const response = await axios.put(
        "http://localhost:3500/api/patient/update",
        {
          patientId: patient.patientId,
          firstName: patient.firstName,
          lastName: patient.lastName,
          email: patient.email,
          mobileNumber: patient.mobileNumber,
          age: patient.age,
          password: patient.password,
        },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setMessage(response.data);
      // Update the parent's userDetails state if needed:
      setUserDetails({ ...userDetails, ...patient });
    } catch (err) {
      console.error("Update Error:", err);
      setError(err.response?.data || "An error occurred");
    }
  };

  return (
    <form onSubmit={handleUpdate} className="space-y-6">
      <div>
        <label className="block text-gray-700 font-medium mb-2">
          First Name
        </label>
        <input
          type="text"
          name="firstName"
          value={patient.firstName}
          onChange={handleChange}
          className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-200"
          required
        />
      </div>     
      <div>
        <label className="block text-gray-700 font-medium mb-2">
          Email
        </label>
        <input
          type="email"
          name="email"
          value={patient.email}
          onChange={handleChange}
          className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-200"
          required
        />
      </div>
      <div>
        <label className="block text-gray-700 font-medium mb-2">
          Mobile Number
        </label>
        <input
          type="text"
          name="mobileNumber"
          value={patient.mobileNumber}
          onChange={handleChange}
          className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-200"
          required
        />
      </div>
      <div>
        <label className="block text-gray-700 font-medium mb-2">
          Age
        </label>
        <input
          type="number"
          name="age"
          value={patient.age}
          onChange={handleChange}
          className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-200"
          required
        />
      </div>
      <div>
        <label className="block text-gray-700 font-medium mb-2">
          Password (Optional)
        </label>
        <input
          type="password"
          name="password"
          value={patient.password}
          onChange={handleChange}
          placeholder="Enter new password (if changing)"
          className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-200"
        />
      </div>
      <button
        type="submit"
        className="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition transform hover:scale-105"
      >
        Update Profile
      </button>
    </form>
  );
};

export default PatientProfileForm;
