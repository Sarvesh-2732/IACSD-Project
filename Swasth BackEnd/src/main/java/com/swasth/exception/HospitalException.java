package com.swasth.exception;
public class HospitalException extends RuntimeException {
    public HospitalException(String message) {
        super(message);
    }

    public HospitalException(String message, Throwable cause) {
        super(message, cause);
    }
}
