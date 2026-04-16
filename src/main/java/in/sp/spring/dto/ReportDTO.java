package in.sp.spring.dto;

public class ReportDTO {

    private Long dailyIssuedBooks;
    private Long dailyReturnedBooks;
    private Long monthlyStudentRegistrations;
    private Integer totalFineCollected;

    public Long getDailyIssuedBooks() {
        return dailyIssuedBooks;
    }

    public void setDailyIssuedBooks(Long dailyIssuedBooks) {
        this.dailyIssuedBooks = dailyIssuedBooks;
    }

    public Long getDailyReturnedBooks() {
        return dailyReturnedBooks;
    }

    public void setDailyReturnedBooks(Long dailyReturnedBooks) {
        this.dailyReturnedBooks = dailyReturnedBooks;
    }

    public Long getMonthlyStudentRegistrations() {
        return monthlyStudentRegistrations;
    }

    public void setMonthlyStudentRegistrations(Long monthlyStudentRegistrations) {
        this.monthlyStudentRegistrations = monthlyStudentRegistrations;
    }

    public Integer getTotalFineCollected() {
        return totalFineCollected;
    }

    public void setTotalFineCollected(Integer totalFineCollected) {
        this.totalFineCollected = totalFineCollected;
    }
}