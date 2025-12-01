package com.example.projectmanageweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projectmanageweb.dto.ResetRequestForm;
import com.example.projectmanageweb.dto.ResetPasswordRequest;
import com.example.projectmanageweb.repository.UserRepository;
import com.example.projectmanageweb.service.OtpService;

import jakarta.validation.Valid;

/*@Controller
@RequestMapping("/reset")*/
public class ForgotPasswordController {
    
	/*
	 * @Autowired private OtpService otpService;
	 * 
	 * @Autowired private UserRepository userRepository;
	 * 
	 * @Autowired private PasswordEncoder passwordEncoder;
	 * 
	 * @GetMapping("/request") public String forgotPasswordForm(Model model) { if
	 * (!model.containsAttribute("form")) { model.addAttribute("form", new
	 * ResetRequestForm()); } return "commons/forgotpassword"; }
	 * 
	 * @PostMapping("/request") public String sendOtp(@Valid @ModelAttribute("form")
	 * ResetRequestForm form, BindingResult bindingResult, RedirectAttributes
	 * redirectAttributes, Model model) {
	 * 
	 * if (bindingResult.hasErrors()) { var messages =
	 * bindingResult.getAllErrors().stream() .map(err -> err.getDefaultMessage())
	 * .toList(); model.addAttribute("errors", messages); return
	 * "commons/forgotpassword"; }
	 * 
	 * try { boolean success = otpService.sendOtpToEmail(form.getEmail()); if
	 * (success) { redirectAttributes.addFlashAttribute("success",
	 * "Mã OTP đã được gửi đến email của bạn. Vui lòng kiểm tra hộp thư.");
	 * redirectAttributes.addFlashAttribute("email", form.getEmail()); return
	 * "redirect:/reset/verify"; } else { model.addAttribute("error",
	 * "Email không tồn tại trong hệ thống."); return "commons/forgotpassword"; } }
	 * catch (Exception e) { model.addAttribute("error",
	 * "Không thể gửi email. Vui lòng thử lại sau."); return
	 * "commons/forgotpassword"; } }
	 * 
	 * @GetMapping("/verify") public String verifyOtpForm(Model model) { if
	 * (!model.containsAttribute("form")) { ResetPasswordRequest form = new
	 * ResetPasswordRequest(); // Lấy email từ flash attribute nếu có if
	 * (model.containsAttribute("email")) { form.setEmail((String)
	 * model.getAttribute("email")); } model.addAttribute("form", form); } return
	 * "commons/resetpassword"; }
	 * 
	 * @PostMapping("/verify") public String
	 * resetPassword(@Valid @ModelAttribute("form") ResetPasswordRequest form,
	 * BindingResult bindingResult, RedirectAttributes redirectAttributes, Model
	 * model) {
	 * 
	 * // Kiểm tra mật khẩu khớp if (!bindingResult.hasFieldErrors("newPassword") &&
	 * !bindingResult.hasFieldErrors("confirmPassword")) { if
	 * (!form.getNewPassword().equals(form.getConfirmPassword())) {
	 * bindingResult.rejectValue("confirmPassword", "match",
	 * "Mật khẩu nhập lại không khớp"); } }
	 * 
	 * if (bindingResult.hasErrors()) { var messages =
	 * bindingResult.getAllErrors().stream() .map(err -> err.getDefaultMessage())
	 * .toList(); model.addAttribute("errors", messages); return
	 * "commons/resetpassword"; }
	 * 
	 * try { // Xác thực OTP boolean otpValid =
	 * otpService.verifyOtp(form.getEmail(), form.getOtpCode()); if (!otpValid) {
	 * model.addAttribute("error", "Mã OTP không hợp lệ hoặc đã hết hạn."); return
	 * "commons/resetpassword"; }
	 * 
	 * // Cập nhật mật khẩu mới String hashedPassword =
	 * passwordEncoder.encode(form.getNewPassword());
	 * userRepository.updatePasswordByEmail(form.getEmail(), hashedPassword);
	 * 
	 * redirectAttributes.addFlashAttribute("success",
	 * "Mật khẩu đã được đặt lại thành công. Vui lòng đăng nhập với mật khẩu mới.");
	 * return "redirect:/login";
	 * 
	 * } catch (Exception e) { model.addAttribute("error",
	 * "Có lỗi xảy ra. Vui lòng thử lại."); return "commons/resetpassword"; } }
	 */
}

