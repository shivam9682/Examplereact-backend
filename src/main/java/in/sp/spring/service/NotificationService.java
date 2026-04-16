package in.sp.spring.service;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import in.sp.spring.Entity.BookHistory;
import in.sp.spring.Entity.Student;
import in.sp.spring.Entity.User;
import in.sp.spring.Entity.book;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;


    public void sendRegistrationEmail(User savedUser) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("ssivamyadav0123@gmail.com");
            helper.setTo(savedUser.getEmail());
            helper.setSubject("Welcome to Library Management System");

            String html = """
                <div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f4f6f9;'>
                    <div style='max-width: 600px; margin: auto; background: white; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>
                        
                        <h2 style='color: #2563eb; text-align: center;'>Registration Successful</h2>

                        <p>Hello <b>%s</b>,</p>

                        <p>You have successfully registered in the Library Management System.</p>

                        <p><b>Name:</b> %s</p>
                        <p><b>Email:</b> %s</p>
                        <p><b>Course:</b> %s</p>
                        <p><b>Membership ID:</b> %s</p>

                        <br>

                        <p>Your Library Membership Card is attached with this email.</p>

                        <p style='margin-top: 30px;'>Regards,<br>Library Management Team</p>
                    </div>
                </div>
                """.formatted(
                    savedUser.getName(),
                    savedUser.getName(),
                    savedUser.getEmail(),
                    savedUser.getStudentClass(),
                    savedUser.getMembershipCardNumber()
                );

            helper.setText(html, true);

            byte[] pdfBytes = generateMembershipCard(savedUser);

            helper.addAttachment(
                "Library-ID-Card.pdf",
                new ByteArrayResource(pdfBytes)
            );

            mailSender.send(message);

            System.out.println("Registration email with ID card sent successfully.");

        } catch (Exception e) {
            System.out.println("Error while sending registration email: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private byte[] generateMembershipCard(User savedUser) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Rectangle cardSize = new Rectangle(500, 300);
            Document document = new Document(cardSize, 20, 20, 20, 20);
            PdfWriter.getInstance(document, outputStream);

            document.open();

            // Background Table
            PdfPTable outerTable = new PdfPTable(1);
            outerTable.setWidthPercentage(100);

            PdfPCell outerCell = new PdfPCell();
            outerCell.setBackgroundColor(new Color(30, 41, 59));
            outerCell.setBorderColor(new Color(59, 130, 246));
            outerCell.setBorderWidth(3f);
            outerCell.setPadding(15);

            // Title
            com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.WHITE);
            com.lowagie.text.Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new Color(96, 165, 250));
            com.lowagie.text.Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.WHITE);
            com.lowagie.text.Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, Color.LIGHT_GRAY);

            Paragraph title = new Paragraph("LIBRARY MEMBERSHIP CARD", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15f);
            outerCell.addElement(title);

            // Divider Line
            LineSeparator line = new LineSeparator();
            line.setLineColor(new Color(96, 165, 250));
            outerCell.addElement(line);

            outerCell.addElement(new Paragraph(" "));

            // Details Table
            PdfPTable detailsTable = new PdfPTable(2);
            detailsTable.setWidthPercentage(100);
            detailsTable.setWidths(new float[]{2f, 4f});

            addStyledRow(detailsTable, "Student Name", savedUser.getName(), headingFont, textFont);
            addStyledRow(detailsTable, "Email", savedUser.getEmail(), headingFont, textFont);
            addStyledRow(detailsTable, "Course", savedUser.getStudentClass(), headingFont, textFont);
            addStyledRow(detailsTable, "Gender", savedUser.getGender(), headingFont, textFont);
            addStyledRow(detailsTable, "Address", savedUser.getAddress(), headingFont, textFont);
            addStyledRow(detailsTable, "Membership ID", savedUser.getMembershipCardNumber(), headingFont, textFont);

            outerCell.addElement(detailsTable);


            // Footer
            Paragraph footer = new Paragraph(
                    "This membership card is system generated and valid for library use.",
                    footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(10f);
            outerCell.addElement(footer);

            outerTable.addCell(outerCell);
            document.add(outerTable);

            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    private void addStyledRow(PdfPTable table, String label, String value,
                              com.lowagie.text.Font labelFont,
                              com.lowagie.text.Font valueFont) {

        PdfPCell labelCell = new PdfPCell(new Phrase(label + " :", labelFont));
        labelCell.setBackgroundColor(new Color(51, 65, 85));
        labelCell.setBorderColor(new Color(71, 85, 105));
        labelCell.setPadding(8);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "N/A", valueFont));
        valueCell.setBackgroundColor(new Color(15, 23, 42));
        valueCell.setBorderColor(new Color(71, 85, 105));
        valueCell.setPadding(8);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    public void sendBorrowEmail(User student, book book) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("ssivamyadav0123@gmail.com");
            helper.setTo(student.getEmail());
            helper.setSubject("Book Borrowed Successfully");

            LocalDate borrowDate = LocalDate.now();
            LocalDate returnDate = borrowDate.plusDays(7);

            String html = """
                <div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f4f6f9;'>
                    <div style='max-width: 600px; margin: auto; background: white; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>

                        <h2 style='color: #16a34a; text-align: center;'>Book Borrowed Successfully</h2>

                        <p>Hello <b>%s</b>,</p>

                        <p>You have borrowed the following book successfully:</p>

                        <p><b>Book Name:</b> %s</p>
                        <p><b>Author:</b> %s</p>
                        <p><b>Borrow Date:</b> %s</p>
                        <p><b>Return Date:</b> %s</p>

                        <br>

                        <p>Please return the book before the due date to avoid late fees.</p>

                        <p style='margin-top: 30px;'>Regards,<br>Library Management Team</p>
                    </div>
                </div>
                """.formatted(
                    student.getName(),
                    book.getTitle(),
                    book.getAuthor(),
                    borrowDate,
                    returnDate
                );

            helper.setText(html, true);
            mailSender.send(message);

            System.out.println("Borrow email sent successfully.");

        } catch (Exception e) {
            System.out.println("Error while sending borrow email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendReturnEmail(User student, book book) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("ssivamyadav0123@gmail.com");
            helper.setTo(student.getEmail());
            helper.setSubject("Book Returned Successfully");

            LocalDate returnDate = LocalDate.now();

            String html = """
                <div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f4f6f9;'>
                    <div style='max-width: 600px; margin: auto; background: white; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>

                        <h2 style='color: #dc2626; text-align: center;'>Book Returned Successfully</h2>

                        <p>Hello <b>%s</b>,</p>

                        <p>You have returned the following book successfully:</p>

                        <p><b>Book Name:</b> %s</p>
                        <p><b>Author:</b> %s</p>
                        <p><b>Return Date:</b> %s</p>

                        <br>

                        <p>Thank you for using the Library Management System.</p>

                        <p style='margin-top: 30px;'>Regards,<br>Library Management Team</p>
                    </div>
                </div>
                """.formatted(
                    student.getName(),
                    book.getTitle(),
                    book.getAuthor(),
                    returnDate
                );

            helper.setText(html, true);
            mailSender.send(message);

            System.out.println("Return email sent successfully.");

        } catch (Exception e) {
            System.out.println("Error while sending return email: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void sendAdminNewUserNotification(User user) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("ssivamyadav0123@gmail.com");
            helper.setTo("ssivamyadav0123@gmail.com");
            helper.setSubject("New Student Registered");

            String html = """
                <div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f4f6f9;'>
                    <div style='max-width: 600px; margin: auto; background: white; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>

                        <h2 style='color:#2563eb; text-align:center;'>New Student Registration</h2>

                        <p>A new student has registered in the Library Management System.</p>

                        <p><b>Name:</b> %s</p>
                        <p><b>Email:</b> %s</p>
                       

                        <p style='margin-top: 30px;'>Regards,<br>Library Management Team</p>
                    </div>
                </div>
                """.formatted(
                    user.getName(),
                    user.getEmail()
                   
                );

            helper.setText(html, true);
            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void sendDueReminderEmail(User user, BookHistory history) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("ssivamyadav0123@gmail.com");
            helper.setTo(user.getEmail());
            helper.setSubject("Book Return Reminder");

            String html = """
                <div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f4f6f9;'>
                    <div style='max-width: 600px; margin: auto; background: white; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>

                        <h2 style='color:#f59e0b; text-align:center;'>Return Reminder</h2>

                        <p>Hello <b>%s</b>,</p>

                        <p>Your borrowed book is due tomorrow.</p>

                        <p><b>Book Name:</b> %s</p>
                        <p><b>Expected Return Date:</b> %s</p>

                        <p>Please return the book on time to avoid late fees.</p>

                        <p style='margin-top: 30px;'>Regards,<br>Library Management Team</p>
                    </div>
                </div>
                """.formatted(
                    user.getName(),
                    history.getTitle(),
                    history.getExpectedReturnDate()
                );

            helper.setText(html, true);
            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendLateFeeReminder(User user, BookHistory history, int fine) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("ssivamyadav0123@gmail.com");
            helper.setTo(user.getEmail());
            helper.setSubject("Late Fee Reminder");

            String html = """
                <div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f4f6f9;'>
                    <div style='max-width: 600px; margin: auto; background: white; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>

                        <h2 style='color:#dc2626; text-align:center;'>Late Fee Notice</h2>

                        <p>Hello <b>%s</b>,</p>

                        <p>Your borrowed book has passed the return date.</p>

                        <p><b>Book Name:</b> %s</p>
                        <p><b>Expected Return Date:</b> %s</p>
                        <p><b>Current Fine:</b> ₹%s</p>

                        <p>Please return the book as soon as possible.</p>

                        <p style='margin-top: 30px;'>Regards,<br>Library Management Team</p>
                    </div>
                </div>
                """.formatted(
                    user.getName(),
                    history.getTitle(),
                    history.getExpectedReturnDate(),
                    fine
                );

            helper.setText(html, true);
            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}