package in.sp.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import in.sp.spring.Entity.Admin;
import in.sp.spring.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository repo;

    public boolean login(String email, String password) {
        Admin admin = repo.findByEmail(email);

        if (admin != null && admin.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
}