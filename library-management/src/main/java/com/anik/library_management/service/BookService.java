package com.anik.library_management.service;

import com.anik.library_management.dto.BookDTO;
import com.anik.library_management.entity.Book;
import org.springframework.stereotype.Component;

import java.util.List;

public interface BookService {

    List<BookDTO> getAllBooks();
    BookDTO getBookById(Long id);
    BookDTO addBook(BookDTO bookDTO);
    BookDTO updateBook(Long id, BookDTO bookDTO);
    void deleteBookById(Long id);
}
