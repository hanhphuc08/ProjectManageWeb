package com.example.projectmanageweb.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectmanageweb.model.Otp;
import com.example.projectmanageweb.repository.OtpRepository;
import com.example.projectmanageweb.repository.UserRepository;

@Service
public class OtpService {
    
    @Autowired
    private OtpRepository otpRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
    
    private static final SecureRandom random = new SecureRandom();
    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALIDITY_MINUTES = 1;
    
    public boolean sendOtpToEmail(String email) {
        // Kiểm tra email có tồn tại trong hệ thống không
        if (!userRepository.existsByEmail(email)) {
            return false;
        }
        
        // Xóa các OTP cũ của email này
        otpRepository.deleteByEmail(email);
        
        // Tạo OTP mới
        String otpCode = generateOtpCode();
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(OTP_VALIDITY_MINUTES * 60);
        
        Otp otp = new Otp(email, otpCode, expiresAt);
        otp.setCreatedAt(now);
        
        // Lưu OTP vào database
        otpRepository.save(otp);
        
        // Gửi email
        try {
            emailService.sendOtpEmail(email, otpCode);
            return true;
        } catch (Exception e) {
            // Nếu gửi email thất bại, xóa OTP đã tạo
            otpRepository.deleteByEmail(email);
            throw e;
        }
    }
    
    public boolean verifyOtp(String email, String otpCode) {
        Optional<Otp> otpOpt = otpRepository.findByEmailAndOtpCode(email, otpCode);
        
        if (otpOpt.isEmpty()) {
            return false;
        }
        
        Otp otp = otpOpt.get();
        
        // Kiểm tra OTP có hợp lệ không
        if (!otp.isValid()) {
            return false;
        }
        
        // Đánh dấu OTP đã sử dụng
        otpRepository.markAsUsed(email, otpCode);
        
        return true;
    }
    
    public void cleanupExpiredOtps() {
        otpRepository.deleteExpiredOtps();
    }
    
    private String generateOtpCode() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}

