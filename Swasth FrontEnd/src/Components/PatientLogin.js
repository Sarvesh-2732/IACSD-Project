import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { GoogleOAuthProvider, GoogleLogin } from "@react-oauth/google";
import { motion, AnimatePresence } from "framer-motion";

const PatientLogin = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [captchaInput, setCaptchaInput] = useState("");
  const [captchaChallenge, setCaptchaChallenge] = useState("");
  const [error, setError] = useState(null);

  // Google Client ID (Replace with your actual Google Client ID)
  const GOOGLE_CLIENT_ID = "809590510532-mbl666uasimacu36l0acqao1g9544dj4.apps.googleusercontent.com";

  // Function to generate a CAPTCHA challenge
  const generateCaptcha = () => {
    const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    const length = 6;
    let captcha = "";
    for (let i = 0; i < length; i++) {
      captcha += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    setCaptchaChallenge(captcha);
    setCaptchaInput("");
  };

  useEffect(() => {
    generateCaptcha();
  }, []);

  // Handle normal login
  const handleLogin = async (e) => {
    e.preventDefault();
    setError(null);

    if (captchaInput.trim().toUpperCase() !== captchaChallenge.toUpperCase()) {
      setError("Incorrect CAPTCHA. Please try again.");
      generateCaptcha();
      return;
    }

    try {
      const response = await axios.post("http://localhost:3500/api/auth/patient/login", {
        email,
        password,
      });

      if (response.status === 200) {
        const { userId, token } = response.data;
        localStorage.setItem("token", token);
        localStorage.setItem("userId", userId);
        localStorage.setItem("userType", "patient");
        navigate("/patient/home");
      }
    } catch (err) {
      console.error("Login Error:", err.response);
      setError("Invalid email or password");
    }
  };

  // Handle Google OAuth Login
  const handleGoogleLoginSuccess = async (response) => {
    try {
        // Add headers and proper error handling
        const googleAuthResponse = await axios.post(
            "http://localhost:3500/api/auth/oauth2/login/success",
            { token: response.credential },
            {
                headers: {
                    'Content-Type': 'application/json'
                },
                withCredentials: true // Important for CORS
            }
        );
        
        if (googleAuthResponse.data) {
            const { token, userId } = googleAuthResponse.data;
            localStorage.setItem("token", token);
            localStorage.setItem("userId", userId);
            localStorage.setItem("userType", "patient");
            navigate("/patient/home");
        }
    } catch (error) {
        console.error("Detailed error:", {
            message: error.message,
            response: error.response?.data,
            status: error.response?.status
        });
        setError(error.response?.data?.message || "Google login failed. Please try again.");
    }
};
  return (
    <GoogleOAuthProvider clientId={GOOGLE_CLIENT_ID}>
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-r from-blue-50 to-purple-100 p-6">
        <motion.div
          initial={{ opacity: 0, y: 50 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: -50 }}
          transition={{ duration: 0.5 }}
          className="bg-white shadow-2xl rounded-2xl p-10 w-full max-w-md"
        >
          <h2 className="text-3xl font-bold text-gray-800 mb-8 text-center">
            Patient Login
          </h2>

          <AnimatePresence>
            {error && (
              <motion.p
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                className="text-red-500 text-sm mb-4 text-center"
              >
                {error}
              </motion.p>
            )}
          </AnimatePresence>

          <form onSubmit={handleLogin} className="space-y-5">
            <div className="mb-6">
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
            <div className="mb-4">
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
            <div className="mb-6">
              <label className="block text-gray-700 font-medium mb-2">
                Enter CAPTCHA:{" "}
                <span className="font-mono bg-gray-200 px-3 py-1 rounded shadow-md">
                  {captchaChallenge}
                </span>
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
              className="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold transition transform hover:scale-105 hover:bg-blue-700"
            >
              Login
            </button>
          </form>

          {/* Google Login Button */}
          <div className="mt-4 flex justify-center">
            <GoogleLogin
              onSuccess={handleGoogleLoginSuccess}
              onError={() => setError("Google login failed. Please try again.")}
            />
          </div>

          <p className="text-sm text-gray-600 mt-6 text-center">
            Don't have an account?{" "}
            <button
              className="text-blue-600 hover:underline font-medium"
              onClick={() => navigate("/signup/patient")}
            >
              Sign up
            </button>
          </p>
        </motion.div>
      </div>
    </GoogleOAuthProvider>
  );
};

export default PatientLogin;
