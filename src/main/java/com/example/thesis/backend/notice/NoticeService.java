package com.example.thesis.backend.notice;

import com.example.thesis.backend.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
public class NoticeService {

    private NoticeRepository noticeRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public ServiceResponse<Notice> addNotice(Notice notice) {
        return new ServiceResponse<>(HttpStatus.OK, noticeRepository.save(notice));
    }

    public ServiceResponse<Notice> addComment(Notice notice, Comment comment) {
        notice.addComment(comment);
        return addNotice(notice);
    }

    public ServiceResponse<Notice> findById(long id) {
        return new ServiceResponse<>(HttpStatus.OK, noticeRepository.findById(id).orElseThrow(InvalidParameterException::new));
    }
}