package in.sp.spring.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.sp.spring.Entity.Student;
import in.sp.spring.service.StudentService;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*") // allow requests from React frontend
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Add student
    @PostMapping("/add")
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        try {
            System.out.println("Received student: " + student); // debug
            Student saved = studentService.addStudent(student);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get all students
    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Integer id) {
        Student student = studentService.getStudentById(id);
        if (student != null) return ResponseEntity.ok(student);
        return ResponseEntity.badRequest().body("Student not found");
    }

    // Update student
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        Student updated = studentService.updateStudent(id, student);
        if (updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.badRequest().body("Student not found");
    }

    // Delete student
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.deleteStudent(id));
    }
}