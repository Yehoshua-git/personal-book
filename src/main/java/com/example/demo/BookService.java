package com.example.demo;

import com.example.demo.db.Book;
import com.example.demo.db.BookRepository;
import com.example.demo.google.GoogleBookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookService {

    private final BookRepository repository;
    private final GoogleBookService googleBookService;

    public BookService(BookRepository repository,
                       GoogleBookService googleBookService) {
        this.repository = repository;
        this.googleBookService = googleBookService;
    }

    public Book addBookFromGoogle(String googleId) {

        Map response = googleBookService.getBookById(googleId);

        if (response == null || !response.containsKey("volumeInfo")) {
            throw new RuntimeException("Invalid Google Book ID");
        }

        Map volumeInfo = (Map) response.get("volumeInfo");

        String title = (String) volumeInfo.get("title");

        List authors = (List) volumeInfo.get("authors");
        String author = authors != null ? authors.get(0).toString() : null;

        Integer pageCount = (Integer) volumeInfo.get("pageCount");

        Book book = new Book(googleId, title, author, pageCount);

        return repository.save(book);
    }
}
