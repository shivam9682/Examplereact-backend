
package in.sp.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import in.sp.spring.Entity.BookHistory;
import in.sp.spring.Entity.ReservedBook;
import in.sp.spring.Entity.User;
import in.sp.spring.Entity.book;
import in.sp.spring.repository.BookHistoryRepository;
import in.sp.spring.repository.BookRepository;
import in.sp.spring.repository.ReservedBookRepository;
import in.sp.spring.repository.UserRepository;
import in.sp.spring.service.NotificationService;
import in.sp.spring.service.bookservice;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private bookservice service;

    @Autowired
    private BookHistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReservedBookRepository reservedBookRepository;

    @Autowired
    private NotificationService notificationService;

     
    @Autowired
    private Cloudinary cloudinary;
    @GetMapping
    public List<book> getBooks() {
        return service.getAllBooks();
    }

    @GetMapping("/{id}")
    public book getBookById(@PathVariable int id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @GetMapping("/branch/{branchName}")
    public List<book> getBooksByBranch(@PathVariable String branchName) {
        return bookRepository.findByLibraryBranch(branchName);
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public book addBook(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("genre") String genre,
            @RequestParam("status") String status,
            @RequestParam("totalQuantity") int totalQuantity,
            @RequestParam(value = "libraryBranch", required = false) String libraryBranch,
            @RequestParam(value = "semester", required = false) String semester,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile file,
            @RequestParam(value = "previewImages", required = false) MultipartFile[] previewImages
    ) {
        try {
            book b = new book();

            b.setTitle(title);
            b.setAuthor(author);
            b.setGenre(genre);
            b.setStatus(status);
            b.setLibraryBranch(libraryBranch);
            b.setSemester(semester);
            b.setDepartment(department);
            b.setDescription(description);
            b.setTotalQuantity(totalQuantity);
            b.setAvailableQuantity(totalQuantity);
            b.setReservedQuantity(0);

            if (file != null && !file.isEmpty()) {

                Map uploadResult = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.emptyMap()
                );

                String imageUrl = uploadResult.get("secure_url").toString();
                b.setImagePath(imageUrl);
            }
            

            // Multiple Preview Images Upload
            if (previewImages != null && previewImages.length > 0) {

                List<String> previewUrls = new ArrayList<>();

                for (MultipartFile previewFile : previewImages) {

                    if (previewFile != null && !previewFile.isEmpty()) {

                        Map uploadResult = cloudinary.uploader().upload(
                                previewFile.getBytes(),
                                ObjectUtils.emptyMap()
                        );

                        String url = uploadResult.get("secure_url").toString();
                        previewUrls.add(url);
                    }
                }

                b.setPreviewImages(String.join(",", previewUrls));
            }

            return service.saveBook(b);

        } catch (Exception e) {
            throw new RuntimeException("Book creation failed: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public book updateBook(
            @PathVariable int id,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("genre") String genre,
            @RequestParam("status") String status,
            @RequestParam("totalQuantity") int totalQuantity,
            @RequestParam(value = "libraryBranch", required = false) String libraryBranch,
            @RequestParam(value = "semester", required = false) String semester,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile file,
            @RequestParam(value = "previewImages", required = false) MultipartFile[] previewImages
    ) {
        try {
            book existingBook = service.getBookById(id);

            if (existingBook == null) {
                throw new RuntimeException("Book not found");
            }

            existingBook.setTitle(title);
            existingBook.setAuthor(author);
            existingBook.setGenre(genre);
            existingBook.setStatus(status);
            existingBook.setLibraryBranch(libraryBranch);
            existingBook.setSemester(semester);
            existingBook.setDepartment(department);
            existingBook.setDescription(description);
            existingBook.setTotalQuantity(totalQuantity);

            if (existingBook.getAvailableQuantity() > totalQuantity) {
                existingBook.setAvailableQuantity(totalQuantity);
            }

            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Main Image Upload
            if (file != null && !file.isEmpty()) {

                String fileName = System.currentTimeMillis() + "_" +
                        file.getOriginalFilename().replaceAll(" ", "_");

                Path filePath = Paths.get(uploadDir, fileName);

                Files.copy(
                        file.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING
                );

                existingBook.setImagePath(fileName);
            }

            // Multiple Preview Images Upload
            if (previewImages != null && previewImages.length > 0) {

                List<String> previewImageNames = new java.util.ArrayList<>();

                for (MultipartFile previewFile : previewImages) {

                    if (previewFile != null && !previewFile.isEmpty()) {

                        String previewFileName = System.currentTimeMillis() + "_" +
                                previewFile.getOriginalFilename().replaceAll(" ", "_");

                        Path previewFilePath = Paths.get(uploadDir, previewFileName);

                        Files.copy(
                                previewFile.getInputStream(),
                                previewFilePath,
                                StandardCopyOption.REPLACE_EXISTING
                        );

                        previewImageNames.add(previewFileName);
                    }
                }

                existingBook.setPreviewImages(String.join(",", previewImageNames));
            }

            return service.saveBook(existingBook);

        } catch (Exception e) {
            throw new RuntimeException("Book update failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable int id) {
        service.deleteBook(id);
        return "Book Deleted Successfully";
    }
    
    @PutMapping("/borrow/{id}")
    public String borrowBook(@PathVariable int id, @RequestParam String user) {
        book b = service.getBookById(id);

        if (b == null) {
            return "Book not found";
        }

        User student = userRepository.findByEmail(user);

        if (student == null) {
            return "User not found";
        }

        if (b.getAvailableQuantity() <= 0) {
            return "Book not available";
        }

        boolean alreadyBorrowed = historyRepository.findByUserEmail(user)
                .stream()
                .anyMatch(history ->
                        history.getBookId() == b.getId() &&
                        history.getStatus() != null &&
                        history.getStatus().trim().equalsIgnoreCase("Borrowed")
                );

        if (alreadyBorrowed) {
            return "You already borrowed this book";
        }

        b.setAvailableQuantity(b.getAvailableQuantity() - 1);

        if (b.getAvailableQuantity() <= 0) {
            b.setAvailableQuantity(0);
            b.setStatus("Out Of Stock");
        } else {
            b.setStatus("Available");
        }
        b.setAvailableQuantity(b.getAvailableQuantity() - 1);

        if (b.getAvailableQuantity() <= 0) {
            b.setAvailableQuantity(0);
            b.setStatus("Out Of Stock");
        } else {
            b.setStatus("Available");
        }

        String borrowedUsers = b.getUserBorrowed();

        if (borrowedUsers == null || borrowedUsers.isEmpty()) {
            b.setUserBorrowed(user);
        } else if (!borrowedUsers.contains(user)) {
            b.setUserBorrowed(borrowedUsers + "," + user);
        }

        bookRepository.save(b);

        LocalDate today = LocalDate.now();
        LocalDate expectedReturn = today.plusDays(7);

        BookHistory history = new BookHistory();
        history.setBookId(b.getId());
        history.setTitle(b.getTitle());
        history.setUserEmail(user);
        history.setIssueDate(today);
        history.setExpectedReturnDate(expectedReturn);
        history.setStatus("Borrowed");
        history.setFine(0);

        historyRepository.save(history);
        notificationService.sendBorrowEmail(student, b);

        return "Book Borrowed Successfully";
    }

    @PutMapping("/return/{historyId}")
    public String returnBook(@PathVariable int historyId) {

        BookHistory history = historyRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("History not found"));

        LocalDate today = LocalDate.now();
        int fine = 0;

        if (history.getExpectedReturnDate() != null) {
            long lateDays = ChronoUnit.DAYS.between(history.getExpectedReturnDate(), today);

            if (lateDays > 0) {
                fine = (int) lateDays * 10;
            }
        }

        history.setActualReturnDate(today);
        history.setFine(fine);
        history.setStatus("Returned");
        historyRepository.save(history);

        book bookObj = service.getBookById(history.getBookId());

        if (bookObj != null) {

            String borrowedUsers = bookObj.getUserBorrowed();

            if (borrowedUsers != null && !borrowedUsers.isEmpty()) {

                List<String> borrowedList = new java.util.ArrayList<>(
                        java.util.Arrays.asList(borrowedUsers.split(","))
                );

                borrowedList.remove(history.getUserEmail());

                bookObj.setUserBorrowed(String.join(",", borrowedList));
            }

            List<ReservedBook> pendingReservations =
                    reservedBookRepository.findByBookIdAndStatusOrderByReservationDateAsc(
                            bookObj.getId(),
                            "PENDING"
                    );

            if (!pendingReservations.isEmpty()) {

                ReservedBook firstReservation = pendingReservations.get(0);

                firstReservation.setStatus("ALLOTTED");
                firstReservation.setAllottedDate(LocalDateTime.now());

                reservedBookRepository.save(firstReservation);

                if (bookObj.getReservedQuantity() > 0) {
                    bookObj.setReservedQuantity(bookObj.getReservedQuantity() - 1);
                }

                String currentBorrowedUsers = bookObj.getUserBorrowed();

                if (currentBorrowedUsers == null || currentBorrowedUsers.isEmpty()) {
                    bookObj.setUserBorrowed(firstReservation.getStudent().getEmail());
                } else {
                    bookObj.setUserBorrowed(
                            currentBorrowedUsers + "," + firstReservation.getStudent().getEmail()
                    );
                }

                String reservedUsers = bookObj.getUserReserved();

                if (reservedUsers != null && !reservedUsers.isEmpty()) {

                    List<String> reservedList = new java.util.ArrayList<>(
                            java.util.Arrays.asList(reservedUsers.split(","))
                    );

                    reservedList.remove(firstReservation.getStudent().getEmail());

                    bookObj.setUserReserved(String.join(",", reservedList));
                }

                BookHistory newHistory = new BookHistory();
                newHistory.setBookId(bookObj.getId());
                newHistory.setTitle(bookObj.getTitle());
                newHistory.setUserEmail(firstReservation.getStudent().getEmail());
                newHistory.setIssueDate(LocalDate.now());
                newHistory.setExpectedReturnDate(LocalDate.now().plusDays(7));
                newHistory.setStatus("Borrowed");
                newHistory.setFine(0);

                historyRepository.save(newHistory);

                bookObj.setStatus("Out Of Stock");

            } else {

                bookObj.setAvailableQuantity(bookObj.getAvailableQuantity() + 1);

                if (bookObj.getAvailableQuantity() > 0) {
                    bookObj.setStatus("Available");
                }
            }

            bookRepository.save(bookObj);
        }

        User student = userRepository.findByEmail(history.getUserEmail());

        if (student != null && bookObj != null) {
            notificationService.sendReturnEmail(student, bookObj);
        }

        return "Book Returned Successfully";
    }
    @PostMapping("/reserve/{bookId}")
    public String reserveBook(@PathVariable int bookId, @RequestParam("user") String email) {

        User student = userRepository.findByEmail(email);

        if (student == null) {
            return "Student not found";
        }

        book bookObj = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (bookObj.getAvailableQuantity() > 0) {
            return "Book is available. Please borrow directly.";
        }

        boolean alreadyReserved = reservedBookRepository
                .existsByStudentIdAndBookIdAndStatus(student.getId(), bookId, "PENDING");

        if (alreadyReserved) {
            return "You have already reserved this book";
        }

        ReservedBook reserve = new ReservedBook();
        reserve.setStudent(student);
        reserve.setBook(bookObj);
        reserve.setReservationDate(LocalDateTime.now());
        reserve.setStatus("PENDING");

        reservedBookRepository.save(reserve);

        bookObj.setReservedQuantity(bookObj.getReservedQuantity() + 1);

        String reservedUsers = bookObj.getUserReserved();

        if (reservedUsers == null || reservedUsers.isEmpty()) {
            bookObj.setUserReserved(email);
        } else if (!reservedUsers.contains(email)) {
            bookObj.setUserReserved(reservedUsers + "," + email);
        }

        bookObj.setStatus("Reserved");

        bookRepository.save(bookObj);

        return "Book reserved successfully";
    }

    @GetMapping("/history")
    public List<BookHistory> getUserHistory(@RequestParam String email) {
        if (email == null || email.trim().isEmpty()) {
            return List.of();
        }

        return historyRepository.findByUserEmail(email.trim());
    }

    @GetMapping("/history/all")
    public List<BookHistory> getAllHistory() {
        return historyRepository.findAll();
    }

    @DeleteMapping("/history/{id}")
    public String deleteHistory(@PathVariable int id) {
        historyRepository.deleteById(id);
        return "History Deleted Successfully";
    }
}

