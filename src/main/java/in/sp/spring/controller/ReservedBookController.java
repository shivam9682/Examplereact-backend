package in.sp.spring.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.sp.spring.Entity.ReservedBook;
import in.sp.spring.Entity.book;
import in.sp.spring.repository.BookRepository;
import in.sp.spring.repository.ReservedBookRepository;

@RestController
@RequestMapping("/api/reserved-books")
@CrossOrigin(origins = "*")
public class ReservedBookController {

    @Autowired
    private ReservedBookRepository reservedBookRepository;
    @Autowired
           private BookRepository bookRepository;
    @GetMapping("/user/{email}")
    public List<ReservedBook> getReservedBooksByUser(
            @PathVariable String email
    ) {
        return reservedBookRepository
                .findByStudentEmailOrderByReservationDateDesc(email);
    }
    
    @PostMapping("/returnBook/{bookId}")
    public String returnBook(@PathVariable Integer bookId) {

        book bookObj = bookRepository.findById(bookId).orElseThrow(
            () -> new RuntimeException("Book not found")
        );

        List<ReservedBook> pendingReservations =
                reservedBookRepository.findByBookIdAndStatusOrderByReservationDateAsc(
                        bookId,
                        "PENDING"
                );

        if (!pendingReservations.isEmpty()) {

            ReservedBook firstReservation = pendingReservations.get(0);

            firstReservation.setStatus("ALLOTTED");
            firstReservation.setAllottedDate(LocalDateTime.now());
            reservedBookRepository.save(firstReservation);

            int reservedQty = bookObj.getReservedQuantity();

            if (reservedQty > 0) {
                bookObj.setReservedQuantity(reservedQty - 1);
            }

        } else {
            bookObj.setAvailableQuantity(bookObj.getAvailableQuantity() + 1);
        }

        bookRepository.save(bookObj);

        return "Book returned successfully";
    }
 // ReservedBookController.java

    @DeleteMapping("/remove/{reservationId}")
    public ResponseEntity<String> removeReservation(@PathVariable int reservationId) {

        ReservedBook reservedBook = reservedBookRepository.findById(reservationId)
                .orElse(null);

        if (reservedBook == null) {
            return ResponseEntity.badRequest().body("Reservation not found");
        }

        book bookObj = reservedBook.getBook();

        if (bookObj != null) {

            int reservedQty = bookObj.getReservedQuantity();

            if (reservedQty > 0) {
                bookObj.setReservedQuantity(reservedQty - 1);
            }

            if (bookObj.getReservedQuantity() <= 0 && bookObj.getAvailableQuantity() > 0) {
                bookObj.setStatus("Available");
            }

            bookRepository.save(bookObj);
        }

        reservedBookRepository.delete(reservedBook);

        return ResponseEntity.ok("Reservation removed successfully");
    }
}
