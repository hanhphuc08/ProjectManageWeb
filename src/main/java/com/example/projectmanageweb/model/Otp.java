package com.example.projectmanageweb.model;

import java.time.Instant;

public class Otp {
    private int otpId;
    private String email;
    private String otpCode;
    private Instant createdAt;
    private Instant expiresAt;
    private boolean isUsed;
    
    // Constructors
    public Otp() {}
    
    public Otp(String email, String otpCode, Instant expiresAt) {
        this.email = email;
        this.otpCode = otpCode;
        this.expiresAt = expiresAt;
        this.isUsed = false;
    }
    
    // Getters and Setters
    public int getOtpId() {
        return otpId;
    }
    
    public void setOtpId(int otpId) {
        this.otpId = otpId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getOtpCode() {
        return otpCode;
    }
    
    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    
    public Instant getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public boolean isUsed() {
        return isUsed;
    }
    
    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
    
    // Helper methods
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
    
    public boolean isValid() {
        return !isUsed && !isExpired();
    }
}

