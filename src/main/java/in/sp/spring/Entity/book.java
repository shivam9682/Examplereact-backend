package in.sp.spring.Entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String author;
    private String genre;
    private String status;

    // 👇 ADD THIS
    private String imagePath;
    
    private String borrowedBy;
    private LocalDate issueDate;
    private LocalDate returnDate;
     // add new  
    private String department;
    private int quantity;
    private int totalQuantity;
    private int availableQuantity;
    private int reservedQuantity;
     
    private String userBorrowed;
    private String userReserved;
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "preview_images", columnDefinition = "TEXT")
    private String previewImages;
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPreviewImages() {
		return previewImages;
	}
	public void setPreviewImages(String previewImages) {
		this.previewImages = previewImages;
	}
	public String getUserBorrowed() {
		return userBorrowed;
	}
	public void setUserBorrowed(String userBorrowed) {
		this.userBorrowed = userBorrowed;
	}
	public String getUserReserved() {
		return userReserved;
	}
	public void setUserReserved(String userReserved) {
		this.userReserved = userReserved;
	}
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public int getAvailableQuantity() {
		return availableQuantity;
	}
	public void setAvailableQuantity(int availableQuantity) {
		this.availableQuantity = availableQuantity;
	}
	public int getReservedQuantity() {
		return reservedQuantity;
	}
	public void setReservedQuantity(int reservedQuantity) {
		this.reservedQuantity = reservedQuantity;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	public String getLibraryBranch() {
		return libraryBranch;
	}
	public void setLibraryBranch(String libraryBranch) {
		this.libraryBranch = libraryBranch;
	}
	public boolean isFavorite() {
		return favorite;
	}
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	private String semester;
    private String libraryBranch;
    private boolean favorite;
    private int lateFee;
    public int getLateFee() {
		return lateFee;
	}
	public void setLateFee(int lateFee) {
		this.lateFee = lateFee;
	}
	public String getBorrowedBy() {
		return borrowedBy;
	}
	public void setBorrowedBy(String borrowedBy) {
		this.borrowedBy = borrowedBy;
	}
	public LocalDate getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}
	public LocalDate getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
	

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // 👇 NEW
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}