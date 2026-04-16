package in.sp.spring.service;

import in.sp.spring.Entity.book;
import in.sp.spring.dto.ReportDTO;
import in.sp.spring.repository.BookHistoryRepository;
import in.sp.spring.repository.BookRepository;
import in.sp.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private BookHistoryRepository bookHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public ReportDTO getDashboardReport() {

        ReportDTO dto = new ReportDTO();

        dto.setDailyIssuedBooks(
                bookHistoryRepository.getDailyIssuedBooks(LocalDate.now())
        );

        dto.setDailyReturnedBooks(
                bookHistoryRepository.getDailyReturnedBooks(LocalDate.now())
        );

        dto.setMonthlyStudentRegistrations(
                userRepository.getMonthlyStudentRegistrations(
                        LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear()
                )
        );

        Integer fine = bookHistoryRepository.getTotalFineCollected();
        dto.setTotalFineCollected(fine != null ? fine : 0);

        return dto;
    }

    public List<Object[]> getMostIssuedBooks() {
        return bookHistoryRepository.getMostIssuedBooks();
    }

    public List<Object[]> getMostActiveStudents() {
        return bookHistoryRepository.getMostActiveStudents();
    }

    public List<book> getLowStockBooks() {
        return bookRepository.findByQuantityLessThan(5);
    }
}