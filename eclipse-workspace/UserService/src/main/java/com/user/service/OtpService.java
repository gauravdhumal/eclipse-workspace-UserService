package com.user.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private Map<String, String>
            otpStorage =
            new HashMap<>();

    public void saveOtp(
            String email,
            String otp) {

        otpStorage.put(email, otp);
    }

    public boolean verifyOtp(
            String email,
            String otp) {

        return otp.equals(
                otpStorage.get(email));
    }
}
