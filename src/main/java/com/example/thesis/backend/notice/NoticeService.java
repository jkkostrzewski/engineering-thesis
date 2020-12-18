package com.example.thesis.backend.notice;

import com.example.thesis.backend.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
public class NoticeService {

    private NoticeRepository noticeRepository;
    private NoticeBoardRepository noticeBoardRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository, NoticeBoardRepository noticeBoardRepository) {
        this.noticeRepository = noticeRepository;
        this.noticeBoardRepository = noticeBoardRepository;
    }

    public ServiceResponse<Notice> saveNotice(Notice notice, NoticeBoard noticeBoard) {
        noticeRepository.save(notice);
        noticeBoard.addNotice(notice);
        noticeBoardRepository.save(noticeBoard);
        return new ServiceResponse<>(HttpStatus.OK, noticeRepository.save(notice));
    }

    public ServiceResponse<Notice> saveNotice(Notice notice) {
        return new ServiceResponse<>(HttpStatus.OK, noticeRepository.save(notice));
    }

    public ServiceResponse<Notice> addComment(Notice notice, ParentComment comment) {
        notice.addParentComment(comment);
        return saveNotice(notice);
    }

    public ServiceResponse<Notice> findById(long id) {
        return new ServiceResponse<>(HttpStatus.OK, noticeRepository.findById(id).orElseThrow(InvalidParameterException::new));
    }
}
