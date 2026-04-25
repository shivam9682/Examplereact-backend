package in.sp.spring.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import in.sp.spring.Entity.User;
import in.sp.spring.repository.UserRepository;
import in.sp.spring.service.OtpService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class ForgotPasswordController {

    @Autowired
    private OtpService otpService;
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/send-otp")
    public Map<String, String> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Map<String, String> response = new HashMap<>();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            response.put("message", "Email not found");
            return response;
        }

        String message = otpService.sendOtp(email);
        response.put("message", message);

        return response;
    }

    @PostMapping("/verify-otp")
    public Map<String, String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        Map<String, String> response = new HashMap<>();

        if (otpService.verifyOtp(email, otp)) {
            response.put("status", "success");
            response.put("message", "OTP verified successfully");
        } else {
            response.put("status", "error");
            response.put("message", "Invalid OTP");
        }

        return response;
    }

    @PostMapping("/reset-password")
    public Map<String, String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        Map<String, String> response = new HashMap<>();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            response.put("status", "error");
            response.put("message", "User not found");
            return response;
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        response.put("status", "success");
        response.put("message", "Password reset successful");

        return response;
    }
}
	    
