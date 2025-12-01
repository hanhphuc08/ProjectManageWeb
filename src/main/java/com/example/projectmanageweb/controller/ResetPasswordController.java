package com.example.projectmanageweb.controller;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projectmanageweb.dto.ChangePasswordForm;
import com.example.projectmanageweb.dto.OtpVerifyForm;
import com.example.projectmanageweb.dto.ResetRequestForm;
import com.example.projectmanageweb.repository.UserRepository;
import com.example.projectmanageweb.service.OtpService;

import jakarta.validation.Valid;

@Controller
public class ResetPasswordController {

	private final OtpService otpService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordController(OtpService otpService,
                                   UserRepository userRepository,
                                   PasswordEncoder passwordEncoder) {
        this.otpService = otpService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================== B1: REQUEST OTP ==================

    @GetMapping("/reset/request")
    public String showRequestForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new ResetRequestForm());
        }
        // file: templates/commons/forgotpassword.html
        return "commons/forgotpassword";
    }

    @PostMapping("/reset/request")
    public String handleRequestOtp(
            @Valid @ModelAttribute("form") ResetRequestForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes ra) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList());
            // quay lại đúng file forgotpassword
            return "commons/forgotpassword";
        }

        boolean sent = otpService.sendOtpToEmail(form.getEmail());
        if (!sent) {
            model.addAttribute("error", "Email không tồn tại trong hệ thống.");
            return "commons/forgotpassword";
        }

        ra.addFlashAttribute("email", form.getEmail());
        return "redirect:/reset/verify?email=" + form.getEmail();
    }

    // ================== B2: VERIFY OTP ==================

    @GetMapping("/reset/verify")
    public String showVerifyForm(@RequestParam("email") String email, Model model) {
        OtpVerifyForm form = new OtpVerifyForm();
        form.setEmail(email);
        model.addAttribute("form", form);
        // file: templates/commons/verify-otp.html
        return "commons/verify-otp";
    }

    @PostMapping("/reset/verify")
    public String handleVerifyOtp(
            @Valid @ModelAttribute("form") OtpVerifyForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes ra) {

        if (bindingResult.hasErrors()) {
            List<String> errs = bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            model.addAttribute("errors", errs);
            return "commons/verify-otp";
        }

        boolean ok = otpService.verifyOtp(form.getEmail(), form.getOtpCode());
        if (!ok) {
            model.addAttribute("error", "OTP không hợp lệ hoặc đã hết hạn.");
            return "commons/verify-otp";
        }

        ra.addFlashAttribute("email", form.getEmail());
        return "redirect:/reset/change?email=" + form.getEmail();
    }

    // ================== B3: CHANGE PASSWORD ==================

    @GetMapping("/reset/change")
    public String showChangePasswordForm(@RequestParam("email") String email,
                                         Model model) {
        ChangePasswordForm form = new ChangePasswordForm();
        form.setEmail(email);
        model.addAttribute("form", form);
        // file: templates/commons/resetpassword.html
        return "commons/resetpassword";
    }

    @PostMapping("/reset/change")
    public String handleChangePassword(
            @Valid @ModelAttribute("form") ChangePasswordForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes ra) {

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "match", "Mật khẩu nhập lại không khớp");
        }

        if (bindingResult.hasErrors()) {
            List<String> errs = bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            model.addAttribute("errors", errs);
            return "commons/resetpassword";
        }

        String encoded = passwordEncoder.encode(form.getNewPassword());
        userRepository.updatePasswordByEmail(form.getEmail(), encoded);

        // để login.html show SweetAlert:
        ra.addFlashAttribute("passwordChanged", true);
        return "redirect:/login";
    }
}
