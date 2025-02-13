import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { motion, AnimatePresence } from "framer-motion";
import PatientProfileForm from "./PatientProfileForm";

const ProfilePage = () => {
  const navigate = useNavigate();
  const [userDetails, setUserDetails] = useState(null);
  const [userType, setUserType] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const storedUserId = localStorage.getItem("userId");
    const storedUserType = localStorage.getItem("userType");
    const token = localStorage.getItem("token");

    if (!storedUserId || !storedUserType || !token) {
      navigate("/login");
      return;
    }
    setUserType(storedUserType);

    // Choose the endpoint based on user type.
    let endpoint = "";
    if (storedUserType === "patient") {
      endpoint = `http://localhost:3500/api/patient/details/${storedUserId}`;
    } else if (storedUserType === "hospital") {
      endpoint = `http://localhost:3500/api/hospital/details/${storedUserId}`;
    } else if (storedUserType === "doctor") {
      endpoint = `http://localhost:3500/api/doctor/details/${storedUserId}`;
    }

    // Fetch the details from the chosen endpoint.
    axios
      .get(endpoint, { headers: { Authorization: `Bearer ${token}` } })
      .then((res) => {
        setUserDetails(res.data);
      })
      .catch((err) => {
        console.error("Error fetching user details:", err);
        setError("Error fetching user details.");
      });
  }, [navigate]);

  // Until user details are loaded, show a loading message.
  if (!userDetails) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-100 to-blue-200 p-6">
        <p className="text-center text-xl font-semibold text-gray-600">
          Loading user details...
        </p>
      </div>
    );
  }

  // Decide what to render based on user type.
  let content = null;
  if (userType === "patient") {
    // Render an update form for patients.
    content = (
      <PatientProfileForm
        userDetails={userDetails}
        setUserDetails={setUserDetails}
        setMessage={setMessage}
        setError={setError}
      />
    );
  } else if (userType === "hospital") {
    content = (
      <div>
        <p className="text-lg text-gray-700">
          <span className="font-semibold">Name:</span> {userDetails.name}
        </p>
        <p className="text-lg text-gray-700">
          <span className="font-semibold">Email:</span> {userDetails.email}
        </p>
        <p className="text-lg text-gray-700">
          <span className="font-semibold">Location:</span> {userDetails.location}
        </p>
        {/* <p className="text-lg text-gray-700">
          <span className="font-semibold">Phone:</span> {userDetails.mobileNumber}
        </p> */}
      </div>
    );
  } else if (userType === "doctor") {
    content = (
      <div>
        <p className="text-lg text-gray-700">
          <span className="font-semibold">Name:</span>{" "}
          {userDetails.firstName} {userDetails.firstName}
        </p>
        <p className="text-lg text-gray-700">
          <span className="font-semibold">Specialization:</span>{" "}
          {userDetails.specialization || userDetails.expertiseField}
        </p>
        <p className="text-lg text-gray-700">
          <span className="font-semibold">Email:</span> {userDetails.email}
        </p>
        <p className="text-lg text-gray-700">
          <span className="font-semibold">Phone:</span> {userDetails.mobileNumber}
        </p>
        <p className="text-lg text-gray-700">
          <span className="font-semibold">Experience:</span> {userDetails.experience}{" "}
          years
        </p>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-100 to-blue-200 p-6">
      <div className="max-w-lg bg-white p-10 rounded-3xl shadow-2xl w-full">
        <h2 className="text-4xl font-bold text-center text-gray-800 mb-8">
          Your Profile
        </h2>

        <AnimatePresence>
          {message && (
            <motion.p
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="text-green-600 text-center mb-4"
            >
              {message}
            </motion.p>
          )}
          {error && (
            <motion.p
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="text-red-600 text-center mb-4"
            >
              {error}
            </motion.p>
          )}
        </AnimatePresence>

        {content}
      </div>
    </div>
  );
};

export default ProfilePage;
