package com.anik.library_management.mapper;

import com.anik.library_management.dto.BookDTO;
import com.anik.library_management.entity.Book;
import org.springframework.beans.BeanUtils;

public class BookMapper {

    public static BookDTO bookToBookDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setQuantity(book.getQuantity());
        bookDTO.setIsAvailable(book.getIsAvailable());
        return bookDTO;
    }

    public static Book bookDTOToBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setQuantity(bookDTO.getQuantity());
        book.setIsAvailable(bookDTO.getIsAvailable());
        return book;
    }

    public static void updateBookFromBookDTO(BookDTO bookDTO, Book book) {
        if(bookDTO.getTitle() != null){
            book.setTitle(bookDTO.getTitle());
        }
        if(bookDTO.getAuthor() != null){
            book.setAuthor(bookDTO.getAuthor());
        }
        if(bookDTO.getIsbn() != null){
            book.setIsbn(bookDTO.getIsbn());
        }
        if(bookDTO.getQuantity() != null){
            book.setQuantity(bookDTO.getQuantity());
        }
        if(bookDTO.getIsAvailable() != null){
            book.setIsAvailable(bookDTO.getIsAvailable());
        }
    }
}
