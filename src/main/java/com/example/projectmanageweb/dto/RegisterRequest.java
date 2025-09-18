package com.example.projectmanageweb.dto;
import jakarta.validation.constraints.*;
public class RegisterRequest {
	
	@NotBlank(message = "Vui lòng nhập họ và tên")
	@NotBlank @Size(min = 2, max = 100, message = "Họ và tên phải từ 2–100 ký tự")
	private String fullName;
	
	
	@NotBlank(message = "Vui lòng nhập email")
	@Email(message = "Email không hợp lệ")
	@NotBlank @Email @Size(max = 100)
	private String email;
	
	
	@NotBlank(message = "Vui lòng nhập mật khẩu")
	@NotBlank @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 ký tự trở lên")
    private String password;
	
	@NotBlank(message = "Vui lòng nhập lại mật khẩu")
	@NotBlank
    private String confirmPassword;
	
	@Size(max = 20)
    private String phoneNumber;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	

}
