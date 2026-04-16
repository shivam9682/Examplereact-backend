package in.sp.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import in.sp.spring.Entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findByEmail(String email);
}