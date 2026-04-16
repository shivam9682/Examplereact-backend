package in.sp.spring.Entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true) // ignore extra JSON fields
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // use Integer, not int, for null-safe auto-generation

    private String name;
    private String email;
    private String course;
    private String phone;
    private String address;

    public Student() { }

    public Student(Integer id, String name, String email, String course, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
        this.phone = phone;
        this.address = address;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "Student{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", course='" + course + '\'' +
               ", phone='" + phone + '\'' +
               ", address='" + address + '\'' +
               '}';
    }
}