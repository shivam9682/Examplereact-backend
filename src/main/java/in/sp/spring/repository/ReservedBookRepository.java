package in.sp.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sp.spring.Entity.ReservedBook;

public interface ReservedBookRepository extends JpaRepository<ReservedBook, Integer> {

    boolean existsByStudentIdAndBookIdAndStatus(
            int studentId,
            int bookId,
            String status
    );

    List<ReservedBook> findByBookIdAndStatusOrderByReservationDateAsc(
            int bookId,
            String status
    );

    List<ReservedBook> findByStudentEmailOrderByReservationDateDesc(String email);
}