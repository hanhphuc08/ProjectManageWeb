package com.example.projectmanageweb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {
    
    @NotBlank(message = "Vui lòng nhập email")
    @Email(message = "Email không hợp lệ")
    private String email;
    
    @NotBlank(message = "Vui lòng nhập mã OTP")
    @Size(min = 6, max = 6, message = "Mã OTP phải có 6 chữ số")
    private String otpCode;
    
    @NotBlank(message = "Vui lòng nhập mật khẩu mới")
    @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 ký tự trở lên")
    private String newPassword;
    
    @NotBlank(message = "Vui lòng nhập lại mật khẩu mới")
    private String confirmPassword;
    
    public ResetPasswordRequest() {}
    
    public ResetPasswordRequest(String email, String otpCode, String newPassword, String confirmPassword) {
        this.email = email;
        this.otpCode = otpCode;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
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
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

