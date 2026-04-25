package in.sp.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import in.sp.spring.service.AdminService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService service;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> data) {

        String email = data.get("email");
        String password = data.get("password");

        boolean isValid = service.login(email, password);

        Map<String, Object> response = new HashMap<>();

        if (isValid) {
            response.put("status", "success");
            response.put("message", "Login successful");
        } else {
            response.put("status", "error");
            response.put("message", "Invalid email or password");
        }

        return response;
    }
}
