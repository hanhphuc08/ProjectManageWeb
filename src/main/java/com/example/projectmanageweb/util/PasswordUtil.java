package com.example.projectmanageweb.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
	
	public static void main(String[] args) {
        String raw = args.length > 0 ? args[0] : "123456";
        var encoder = new BCryptPasswordEncoder();
        System.out.println("Raw: " + raw);
        System.out.println("BCrypt: " + encoder.encode(raw));
    }

}
