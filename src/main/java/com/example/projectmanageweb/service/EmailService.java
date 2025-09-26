package com.example.projectmanageweb.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otpCode) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setTo(to);
            helper.setSubject("🔐 Mã OTP khôi phục mật khẩu - ProjectManageWeb");
            helper.setText(buildOtpEmailContent(otpCode), true); // true = HTML

            mailSender.send(mimeMessage);
            System.out.println("✅ OTP email sent successfully to: " + to);
        } catch (MessagingException e) {
            System.err.println("❌ Failed to send OTP email: " + e.getMessage());
            throw new RuntimeException("Không thể gửi email OTP", e);
        }
    }

    private String buildOtpEmailContent(String otpCode) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; background: #f4f6f8; padding: 20px; }
                    .container { max-width: 600px; margin: auto; background: #fff; border-radius: 10px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); padding: 30px; }
                    h2 { color: #2c3e50; text-align: center; }
                    .otp-box { background: #f8f9fa; border: 2px solid #007bff; border-radius: 8px; padding: 20px; text-align: center; margin: 20px 0; }
                    .otp-title { color: #007bff; margin: 0; font-size: 20px; }
                    .otp-code { font-size: 32px; font-weight: bold; color: #007bff; letter-spacing: 5px; margin: 10px 0; }
                    ul { margin: 10px 0; }
                    li { margin-bottom: 6px; }
                    .footer { font-size: 12px; color: #666; text-align: center; margin-top: 30px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>Khôi phục mật khẩu</h2>
                    <p>Xin chào,</p>
                    <p>Bạn đã yêu cầu khôi phục mật khẩu cho tài khoản <strong>ProjectManageWeb</strong>.</p>
                    <div class="otp-box">
                        <div class="otp-title">Mã OTP của bạn:</div>
                        <div class="otp-code">%s</div>
                    </div>
                    <p><strong>Lưu ý quan trọng:</strong></p>
                    <ul>
                        <li>Mã OTP này chỉ có hiệu lực trong <strong>1 phút</strong></li>
                        <li>Mỗi mã OTP chỉ có thể sử dụng một lần</li>
                        <li>Nếu bạn không yêu cầu khôi phục mật khẩu, vui lòng bỏ qua email này</li>
                    </ul>
                    <p>Nếu bạn gặp vấn đề, vui lòng liên hệ với chúng tôi.</p>
                    <hr>
                    <p class="footer">
                        Email này được gửi tự động từ hệ thống ProjectManageWeb.<br>
                        Vui lòng không trả lời email này.
                    </p>
                </div>
            </body>
            </html>
            """, otpCode);
    }
}

