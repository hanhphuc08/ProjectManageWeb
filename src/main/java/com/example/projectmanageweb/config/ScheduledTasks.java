package com.example.projectmanageweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.projectmanageweb.service.OtpService;

@Component
public class ScheduledTasks {
    
    @Autowired
    private OtpService otpService;

    @Scheduled(fixedRate = 300000)
    public void cleanupExpiredOtps() {
        try {
            otpService.cleanupExpiredOtps();
            System.out.println("Cleaned up expired OTPs at: " + java.time.LocalDateTime.now());
        } catch (Exception e) {
            System.err.println("Error cleaning up expired OTPs: " + e.getMessage());
        }
    }
}

