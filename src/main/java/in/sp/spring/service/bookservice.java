package in.sp.spring.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.spring.Entity.book;
import in.sp.spring.repository.BookRepository;

import java.util.List;

@Service
public class bookservice {

    @Autowired
    private BookRepository repo;

    public List<book> getAllBooks() {
        return repo.findAll();
    }

    public book saveBook(book book) {
        return repo.save(book);
    }

    public void deleteBook(int id) {
        repo.deleteById(id);
    }

    // ✅ ADD THIS
    public book getBookById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }
    
    
}