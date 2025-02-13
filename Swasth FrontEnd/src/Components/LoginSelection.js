import React from "react";
import { useNavigate } from "react-router-dom";
import PatientLogin from "./PatientLogin";

const LoginSelection = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-r from-blue-50 to-purple-100 p-4">
      <div className="bg-white shadow-2xl rounded-xl p-10 w-full max-w-md">
        <h2 className="text-3xl font-bold text-gray-800 mb-8">Login As</h2>
        <div className="space-y-6">
          <button
            onClick={() => navigate("/login/patient")}
            className="w-full py-3 px-4 bg-blue-500 text-white rounded-lg font-medium transition transform hover:scale-105 hover:bg-blue-600"
          >
            Patient Login / Signup
          </button>      
          <button
            onClick={() => navigate("/login/doctor")}
            className="w-full py-3 px-4 bg-green-500 text-white rounded-lg font-medium transition transform hover:scale-105 hover:bg-green-600"
          >
            Doctor Login / Signup
          </button>
          <button
            onClick={() => navigate("/login/hospital")}
            className="w-full py-3 px-4 bg-red-500 text-white rounded-lg font-medium transition transform hover:scale-105 hover:bg-red-600"
          >
            Hospital Login / Signup
          </button>
        </div>
      </div>
    </div>
  );
};

export default LoginSelection;
