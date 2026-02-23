package com.anik.library_management.serviceImpl;

import com.anik.library_management.dto.BookDTO;
import com.anik.library_management.entity.Book;
import com.anik.library_management.mapper.BookMapper;
import com.anik.library_management.repository.BookRepository;
import com.anik.library_management.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository
                .findAll()
                .stream().map(BookMapper::bookToBookDTO)
                .toList();
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book Not Found!"));
        return BookMapper.bookToBookDTO(book);
    }

    @Override
    public BookDTO addBook(BookDTO bookDTO) {
        Book book = BookMapper.bookDTOToBook(bookDTO);
        book = bookRepository.save(book);
        return BookMapper.bookToBookDTO(book);
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book Not Found!"));
        BookMapper.updateBookFromBookDTO(bookDTO, book);
        Book updateBook = bookRepository.save(book);
        return BookMapper.bookToBookDTO(updateBook);
    }

    @Override
    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book Not Found!"));
        bookRepository.deleteById(id);
    }
}
