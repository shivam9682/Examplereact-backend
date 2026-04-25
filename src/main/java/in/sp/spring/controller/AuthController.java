package in.sp.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import in.sp.spring.Entity.User;
import in.sp.spring.config.JwtUtils;
import in.sp.spring.repository.UserRepository;
import in.sp.spring.service.NotificationService;
import in.sp.spring.service.UserService;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:5173")
<<<<<<< HEAD

    @CrossOrigin(origins = "*")
=======
@CrossOrigin(origins = "*")
>>>>>>> 1331dab (message)
public class AuthController {

    @Autowired
    private UserService service;
     @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    // Register API
    @PostMapping(value = "/register")
    public User register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("studentClass") String studentClass,
            @RequestParam("gender") String gender,
            @RequestParam("address") String address,
            @RequestParam("password") String password,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "semester", required = false) String semester,
            @RequestParam(value = "branchName", required = false) String branchName,
            @RequestParam(value = "photo", required = false) MultipartFile photo
    ) throws IOException {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setStudentClass(studentClass);
        user.setGender(gender);
        user.setAddress(address);
        user.setPassword(password);
        user.setDepartment(department);
        user.setSemester(semester);
        user.setBranchName(branchName);

        user.setMembershipCardNumber("LIB" + System.currentTimeMillis());

        if (photo != null && !photo.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();

            Path uploadPath = Paths.get("uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(
                photo.getInputStream(),
                uploadPath.resolve(fileName),
                StandardCopyOption.REPLACE_EXISTING
            );

            user.setPhoto(fileName);
        }

        User savedUser = service.register(user);

        notificationService.sendRegistrationEmail(savedUser);
        notificationService.sendAdminNewUserNotification(savedUser);

        return savedUser;
    }
     

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> data) {

        String email = data.get("email");
        String password = data.get("password");

        User user = service.login(email, password);

        Map<String, Object> response = new HashMap<>();

        if (user != null) {

            // 🔐 generate tokens
            String accessToken = JwtUtils.generateToken(user.getEmail());
            String refreshToken = JwtUtils.generateRefreshToken(user.getEmail());

            response.put("status", "SUCCESS");

            // 🔑 JWT tokens
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);

            // user data
            response.put("status", "SUCCESS");
            response.put("id", user.getId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("photo", user.getPhoto() != null ? "uploads/" + user.getPhoto() : "");
            response.put("studentClass", user.getStudentClass());
            response.put("gender", user.getGender());
            response.put("department", user.getDepartment());
            response.put("semester", user.getSemester());
            response.put("branchName", user.getBranchName());
            response.put("address", user.getAddress());
            response.put("membershipCardNumber", user.getMembershipCardNumber());

        } else {
            response.put("status", "FAIL");
            response.put("message", "Invalid Email or Password");
        }

        return response;
    }
    // Update Profile API
    @PutMapping("/update-profile")
    public User updateProfile(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("studentClass") String studentClass,
            @RequestParam("gender") String gender,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "semester", required = false) String semester,
            @RequestParam(value = "branchName", required = false) String branchName,
            @RequestParam("address") String address,
            @RequestParam(value = "photo", required = false) MultipartFile photo
    ) throws IOException {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User existingUser = optionalUser.get();

        existingUser.setName(name);
        existingUser.setEmail(email);
        existingUser.setStudentClass(studentClass);
        existingUser.setGender(gender);
        existingUser.setDepartment(department);
        existingUser.setSemester(semester);
        existingUser.setBranchName(branchName);
        existingUser.setAddress(address);

        if (photo != null && !photo.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();

            Path uploadPath = Paths.get("uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(
                    photo.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );

            existingUser.setPhoto(fileName);
        }

        return userRepository.save(existingUser);
    }

    // Delete User API
    @DeleteMapping("/delete/{id}")
    public Map<String, String> deleteUser(@PathVariable int id) {

        Map<String, String> response = new HashMap<>();

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            response.put("status", "SUCCESS");
            response.put("message", "User deleted successfully");
        } else {
            response.put("status", "FAIL");
            response.put("message", "User not found");
        }

        return response;
    }
    
 // Get current user by email
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(@RequestParam String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    @GetMapping("/profile")
    public User getProfile(@RequestParam String email) {
        return userRepository.findByEmail(email);
    }
    
    @PostMapping("/refresh")
    public Map<String, Object> refresh(@RequestBody Map<String, String> data) {

        String refreshToken = data.get("refreshToken");

        Map<String, Object> response = new HashMap<>();

        try {
            String email = Jwts.parser()
                    .setSigningKey("mysupersecuresecretkeyforjwt2026prayagrajlibrarysystem123456789")
                    .parseClaimsJws(refreshToken)
                    .getBody()
                    .getSubject();

            String newAccessToken = JwtUtils.generateToken(email);

            response.put("status", "SUCCESS");
            response.put("accessToken", newAccessToken);

        } catch (Exception e) {
            response.put("status", "FAIL");
            response.put("message", "Invalid or Expired Refresh Token");
        }

        return response;
    }
}
