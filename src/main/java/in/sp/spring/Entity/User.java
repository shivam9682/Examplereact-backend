package in.sp.spring.Entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "users") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    @Column(nullable = false, unique = true)
    private String email;
   
    private String studentClass;
    private String gender;

    @Column(unique = true)
    private String address;
    private String password;
     //Add new 
    private String photo;
    private String membershipCardNumber;
    private String department;
    private String semester;
    private int borrowLimit = 3;
    private int borrowedCount = 0;
    private String branchName;
    public LocalDate getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}
	private LocalDate registrationDate;
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getMembershipCardNumber() {
		return membershipCardNumber;
	}
	public void setMembershipCardNumber(String membershipCardNumber) {
		this.membershipCardNumber = membershipCardNumber;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public int getBorrowLimit() {
		return borrowLimit;
	}
	public void setBorrowLimit(int borrowLimit) {
		this.borrowLimit = borrowLimit;
	}
	public int getBorrowedCount() {
		return borrowedCount;
	}
	public void setBorrowedCount(int borrowedCount) {
		this.borrowedCount = borrowedCount;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStudentClass() {
		return studentClass;
	}
	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

    // getters & setters
    
}
