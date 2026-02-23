package com.anik.library_management.service;

import com.anik.library_management.entity.IssueRecord;
import org.springframework.stereotype.Service;

public interface IssueRecordService {
    IssueRecord issueTheBook(Long bookId);
    IssueRecord returnTheBook(Long issueRecordId);
}
