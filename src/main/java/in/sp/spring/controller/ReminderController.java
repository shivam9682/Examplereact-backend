package in.sp.spring.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.sp.spring.Entity.BookHistory;
import in.sp.spring.Entity.User;
import in.sp.spring.repository.BookHistoryRepository;
import in.sp.spring.repository.UserRepository;
import in.sp.spring.service.NotificationService;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    @Autowired
    private BookHistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/send")
    public String sendReminders() {

        List<BookHistory> borrowedBooks = historyRepository.findAll();

        for (BookHistory history : borrowedBooks) {

            if (!"Borrowed".equals(history.getStatus())) {
                continue;
            }

            User user = userRepository.findByEmail(history.getUserEmail());

            if (user == null) {
                continue;
            }

            LocalDate today = LocalDate.now();
            LocalDate expectedDate = history.getExpectedReturnDate();

            long daysLeft = ChronoUnit.DAYS.between(today, expectedDate);

            // Reminder before 1 day
            if (daysLeft == 1) {
                notificationService.sendDueReminderEmail(user, history);
            }

            // Late fee reminder
            if (today.isAfter(expectedDate)) {
                int fine = (int) ChronoUnit.DAYS.between(expectedDate, today) * 10;
                notificationService.sendLateFeeReminder(user, history, fine);
            }
        }

        return "Reminders Sent Successfully";
    }
}