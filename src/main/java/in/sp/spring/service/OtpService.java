package in.sp.spring.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    private Map<String, String> otpStorage = new HashMap<>();

    public String sendOtp(String email) {
        Random random = new Random();
        String otp = String.valueOf(100000 + random.nextInt(900000));

        otpStorage.put(email, otp);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("🔐 Password Reset OTP - Library Management System");

            String htmlContent = """
                <div style="font-family: Arial, sans-serif; background-color: #f4f6f9; padding: 40px;">
                    <div style="max-width: 600px; margin: auto; background: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">
                        
                        <div style="background: linear-gradient(135deg, #2563eb, #1e40af); padding: 25px; text-align: center;">
                            <h1 style="color: #ffffff; margin: 0; font-size: 28px;">
                                Library Management System
                            </h1>
                        </div>

                        <div style="padding: 35px; color: #333333;">
                            <h2 style="margin-top: 0; color: #111827;">Password Reset Request</h2>

                            <p style="font-size: 16px; line-height: 1.7;">
                                Hello,
                            </p>

                            <p style="font-size: 16px; line-height: 1.7;">
                                We received a request to reset your account password.
                                Please use the OTP below to continue:
                            </p>

                            <div style="text-align: center; margin: 30px 0;">
                                <span style="
                                    display: inline-block;
                                    background: #eff6ff;
                                    color: #1d4ed8;
                                    font-size: 32px;
                                    font-weight: bold;
                                    letter-spacing: 8px;
                                    padding: 18px 35px;
                                    border-radius: 10px;
                                    border: 2px dashed #2563eb;
                                ">
                                    """ + otp + """
                                </span>
                            </div>

                            <p style="font-size: 15px; color: #6b7280; line-height: 1.6;">
                                This OTP is valid for a limited time only.
                                Please do not share it with anyone for security reasons.
                            </p>

                            <p style="font-size: 15px; color: #6b7280; line-height: 1.6;">
                                If you did not request a password reset, you can safely ignore this email.
                            </p>
                        </div>

                        <div style="background: #f9fafb; padding: 20px; text-align: center; border-top: 1px solid #e5e7eb;">
                            <p style="margin: 0; font-size: 14px; color: #6b7280;">
                                © 2026 Library Management System | Secure Access
                            </p>
                        </div>
                    </div>
                </div>
                """;

            helper.setText(htmlContent, true);

            mailSender.send(message);

            return "OTP sent successfully";

        } catch (MessagingException e) {
            e.printStackTrace();
            return "Failed to send OTP";
        }
    }

    public boolean verifyOtp(String email, String otp) {
        return otp.equals(otpStorage.get(email));
    }
}