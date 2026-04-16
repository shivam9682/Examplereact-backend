package in.sp.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.sp.spring.Entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    
    User findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);

    Optional<User> findById(int id);

    boolean existsById(int id);
    
    @Query("SELECT COUNT(u) FROM User u WHERE MONTH(u.registrationDate) = :month AND YEAR(u.registrationDate) = :year")
    Long getMonthlyStudentRegistrations(@Param("month") int month, @Param("year") int year);
}