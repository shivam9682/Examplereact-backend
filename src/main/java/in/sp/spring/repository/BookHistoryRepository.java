package in.sp.spring.repository;

import in.sp.spring.Entity.BookHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookHistoryRepository extends JpaRepository<BookHistory, Integer> {
    List<BookHistory> findByUserEmail(String userEmail);
    
    @Query("SELECT COUNT(bh) FROM BookHistory bh WHERE bh.issueDate = :date")
    Long getDailyIssuedBooks(@Param("date") LocalDate date);

    @Query("SELECT COUNT(bh) FROM BookHistory bh WHERE bh.actualReturnDate = :date")
    Long getDailyReturnedBooks(@Param("date") LocalDate date);

    @Query("SELECT SUM(bh.fine) FROM BookHistory bh")
    Integer getTotalFineCollected();

    @Query("""
        SELECT bh.title, COUNT(bh.bookId)
        FROM BookHistory bh
        GROUP BY bh.title
        ORDER BY COUNT(bh.bookId) DESC
    """)
    List<Object[]> getMostIssuedBooks();

    @Query("""
        SELECT bh.userEmail, COUNT(bh.id)
        FROM BookHistory bh
        GROUP BY bh.userEmail
        ORDER BY COUNT(bh.id) DESC
    """)
    List<Object[]> getMostActiveStudents();
    
    
}