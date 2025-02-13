import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { motion } from "framer-motion";

const HospitalLogin = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [captchaInput, setCaptchaInput] = useState("");
  const [captchaChallenge, setCaptchaChallenge] = useState("");
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  // Function to generate a new text-based CAPTCHA challenge
  const generateCaptcha = () => {
    const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    const length = 6;
    let captcha = "";
    for (let i = 0; i < length; i++) {
      captcha += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    setCaptchaChallenge(captcha);
    setCaptchaInput(""); // Clear previous input
  };

  useEffect(() => {
    generateCaptcha(); // Generate CAPTCHA on component mount
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (captchaInput.trim().toUpperCase() !== captchaChallenge.toUpperCase()) {
      setError("Incorrect CAPTCHA. Please try again.");
      generateCaptcha();
      return;
    }

    try {
      const response = await axios.post("http://localhost:3500/api/auth/hospital/login", {
        email,
        password,
      });

      console.log("Login Response:", response.data); // Debugging log

      if (response.status === 200) {
        const { userId, token } = response.data;

        if (!userId) {
          console.error("Error: userId is missing in response");
          setError("Login failed. Please try again.");
          return;
        }

        console.log("Storing userId:", userId); // Debugging log
        localStorage.setItem("token", token);
        localStorage.setItem("userId", userId);
        localStorage.setItem("userType", "hospital");

        navigate("/hospital/home");
      }
    } catch (err) {
      console.error("Login Error:", err.response);
      setError(err.response?.data?.message || "Invalid email or password");
    }
};

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-r from-green-100 to-blue-100 p-6">
      <motion.div
        initial={{ opacity: 0, scale: 0.8, y: 50 }}
        animate={{ opacity: 1, scale: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="bg-white p-8 rounded-2xl shadow-2xl w-full max-w-md"
      >
        <h2 className="text-3xl font-bold text-center text-gray-800 mb-6">
          Hospital Login
        </h2>
        {error && (
          <p className="text-red-500 text-center mb-4">{error}</p>
        )}
        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block text-gray-700 font-medium mb-2">
              Email
            </label>
            <input
              type="email"
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Enter your email"
              required
            />
          </div>
          <div>
            <label className="block text-gray-700 font-medium mb-2">
              Password
            </label>
            <input
              type="password"
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter your password"
              required
            />
          </div>

          {/* CAPTCHA Section */}
          <div>
            <label className="block text-gray-700 font-medium mb-2">
              Enter CAPTCHA: <span className="font-mono bg-gray-200 px-3 py-1 rounded shadow-md">{captchaChallenge}</span>
            </label>
            <div className="flex items-center space-x-4 mb-2">
              <input
                type="text"
                className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition"
                value={captchaInput}
                onChange={(e) => setCaptchaInput(e.target.value)}
                placeholder="Type the text above"
                required
              />
              <button
                type="button"
                onClick={generateCaptcha}
                className="px-3 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition"
              >
                Refresh
              </button>
            </div>
          </div>

          <button
            type="submit"
            className="w-full py-3 bg-blue-500 text-white rounded-lg font-semibold transition transform hover:scale-105 hover:bg-blue-600"
          >
            Login
          </button>
        </form>

        <div className="mt-6 text-center">
          <p className="text-gray-700">New to Swasth?</p>
          <button
            onClick={() => navigate("/hospital/signup")}
            className="text-blue-500 hover:underline font-semibold mt-2"
          >
            Sign Up
          </button>
        </div>
      </motion.div>
    </div>
  );
};

export default HospitalLogin;
