package in.sp.spring.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.spring.Entity.Student;
import in.sp.spring.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Add student
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Student getStudentById(Integer id) {
        return studentRepository.findById(id).orElse(null);
    }

    // Update student
    public Student updateStudent(Integer id, Student updatedStudent) {
        return studentRepository.findById(id).map(student -> {
            student.setName(updatedStudent.getName());
            student.setEmail(updatedStudent.getEmail());
            student.setCourse(updatedStudent.getCourse());
            student.setPhone(updatedStudent.getPhone());
            student.setAddress(updatedStudent.getAddress());
            return studentRepository.save(student);
        }).orElse(null);
    }

    // Delete student
    public String deleteStudent(Integer id) {
        studentRepository.deleteById(id);
        return "Student deleted successfully";
    }
}