package com.example.projectmanageweb.controller;

import javax.naming.Binding;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.projectmanageweb.dto.RegisterRequest;
import com.example.projectmanageweb.service.AuthService;
import com.example.projectmanageweb.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
/*@RequiredArgsConstructor */
public class AuthController {
	
	private final AuthService authService;
	private final UserService userService;
	
	public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }
	
	@GetMapping("/login")
    public String login() {	
		return "commons/login";
	}
	
	@GetMapping("/register")
	public String registerForm(Model model) {
		if(!model.containsAttribute("form")) {
			model.addAttribute("form", new RegisterRequest());
		}
		return "commons/register";
		
	}
	
	@PostMapping("/register")
	public String doRegister(@Valid @ModelAttribute("form") RegisterRequest form,
								BindingResult br,
								RedirectAttributes ra,
								Model model) {
		//TODO: process POST request
		
		// Kiểm tra mật khẩu khớp
	    if (!br.hasFieldErrors("password") && !br.hasFieldErrors("confirmPassword")) {
	        if (!form.getPassword().equals(form.getConfirmPassword())) {
	            br.rejectValue("confirmPassword", "match", "Mật khẩu nhập lại không khớp");
	        }
	    }

	    // Kiểm tra email trùng
	    if (!br.hasFieldErrors("email") && userService.findByEmail(form.getEmail()).isPresent()) {
	        br.rejectValue("email", "exists", "Email đã được đăng ký trước đó");
	    }

	    if (br.hasErrors()) {
	        // gom toàn bộ message để show bằng SweetAlert
	        var messages = br.getAllErrors().stream()
	                .map(err -> err.getDefaultMessage())
	                .toList();
	        model.addAttribute("errors", messages);
	        return "commons/register";
	    }
		try {
			authService.register(form);
			// Register thanh cong va chuyen sang trang login
			ra.addAttribute("registered", "1");
			return "redirect:/login";
		}
		catch(IllegalStateException ex){
			model.addAttribute("error",ex.getMessage());
			return "commons/register";
		}
	}
	
	
	

}
