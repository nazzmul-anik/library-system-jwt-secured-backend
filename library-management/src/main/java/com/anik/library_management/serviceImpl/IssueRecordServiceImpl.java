package com.anik.library_management.serviceImpl;

import com.anik.library_management.entity.Book;
import com.anik.library_management.entity.IssueRecord;
import com.anik.library_management.entity.User;
import com.anik.library_management.repository.BookRepository;
import com.anik.library_management.repository.IssueRecordRepository;
import com.anik.library_management.repository.UserRepository;
import com.anik.library_management.service.IssueRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class IssueRecordServiceImpl implements IssueRecordService {

    private final IssueRecordRepository issueRecordRepository;
    private final BookRepository  bookRepository;
    private final UserRepository userRepository;

    // Constructor Injection
    public IssueRecordServiceImpl(
        IssueRecordRepository issueRecordRepository,
        BookRepository bookRepository,
        UserRepository userRepository){
        this.issueRecordRepository = issueRecordRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public IssueRecord issueTheBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new RuntimeException("Book not found!"));

        if(book.getQuantity()<=0 || !book.getIsAvailable()){
            throw new RuntimeException("Book is not available!");
        }
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found!"));

        IssueRecord issueRecord = new IssueRecord();
        issueRecord.setIssueDate(LocalDate.now());
        issueRecord.setDueDate(LocalDate.now().plusDays(14));
        issueRecord.setIsReturned(false);
        issueRecord.setUser(user);
        issueRecord.setBook(book);

        book.setQuantity(book.getQuantity()-1);
        if(book.getQuantity()==0){
            book.setIsAvailable(false);
        }
        bookRepository.save(book);
        issueRecord = issueRecordRepository.save(issueRecord);
        return issueRecord;
    }

    @Override
    public IssueRecord returnTheBook(Long issueRecordId) {
        IssueRecord issueRecord = issueRecordRepository.findById(issueRecordId)
                .orElseThrow(()->new RuntimeException("Issue Record not found!"));

        if(issueRecord.getIsReturned()){
            throw new RuntimeException("Book is already returned!");
        }
        Book book = issueRecord.getBook();
        book.setQuantity(book.getQuantity()+1);
        book.setIsAvailable(true);
        bookRepository.save(book);
        issueRecord.setReturnDate(LocalDate.now());
        issueRecord.setIsReturned(true);
        issueRecord = issueRecordRepository.save(issueRecord);
        return issueRecord;
    }
}
