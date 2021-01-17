package com.example.thesis.backend.notice;

import com.example.thesis.backend.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final NoticeService noticeService;

    @Autowired
    public CommentService(CommentRepository commentRepository, NoticeService noticeService) {
        this.commentRepository = commentRepository;
        this.noticeService = noticeService;
    }

    public ServiceResponse<Comment> addComment(Long noticeId, ParentComment parentComment, Comment comment) {
        Notice notice = noticeService.findById(noticeId).getContent();
        commentRepository.save(comment);
        notice.addReply(parentComment, comment);
        noticeService.saveNotice(notice);

        return new ServiceResponse<>(HttpStatus.OK, comment);
    }

    public ServiceResponse<ParentComment> addParentComment(Long noticeId, ParentComment parentComment) {
        Notice notice = noticeService.findById(noticeId).getContent();
        commentRepository.save(parentComment);
        notice.addParentComment(parentComment);
        noticeService.saveNotice(notice);

        return new ServiceResponse<>(HttpStatus.OK, parentComment);
    }
}
