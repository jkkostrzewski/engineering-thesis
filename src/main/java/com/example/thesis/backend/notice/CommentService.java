package com.example.thesis.backend.notice;

import com.example.thesis.backend.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public ServiceResponse<Comment> addComment(Comment comment) {
        return new ServiceResponse<>(HttpStatus.OK, commentRepository.save(comment));
    }
}
