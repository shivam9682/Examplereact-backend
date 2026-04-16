package in.sp.spring.controller;

import in.sp.spring.Entity.book;
import in.sp.spring.dto.ReportDTO;
import in.sp.spring.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:5173")
public class ReportController {

    private final BookController bookController;

    @Autowired
    private ReportService reportService;

    ReportController(BookController bookController) {
        this.bookController = bookController;
    }

    @GetMapping("/dashboard")
    public ReportDTO getDashboardReport() {
        return reportService.getDashboardReport();
    }

    @GetMapping("/most-issued-books")
    public List<Object[]> getMostIssuedBooks() {
        return reportService.getMostIssuedBooks();
    }

    @GetMapping("/most-active-students")
    public List<Object[]> getMostActiveStudents() {
        return reportService.getMostActiveStudents();
    }

    @GetMapping("/low-stock-books")
    public List<book> getLowStockBooks() {
        return reportService.getLowStockBooks();
    }
     
    
    
    @GetMapping("/export-pdf")
    public void exportPdf(HttpServletResponse response) throws Exception {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=library_report.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        document.add(new Paragraph("Most Issued Books Report"));

        PdfPTable table = new PdfPTable(2);
        table.addCell("Book Title");
        table.addCell("Issue Count");

        List<Object[]> books = reportService.getMostIssuedBooks();

        for (Object[] obj : books) {
            table.addCell(obj[0].toString());
            table.addCell(obj[1].toString());
        }

        document.add(table);
        document.close();
    }
    
    
}