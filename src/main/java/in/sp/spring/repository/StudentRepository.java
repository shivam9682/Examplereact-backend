package in.sp.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.spring.Entity.Student;


@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> { }