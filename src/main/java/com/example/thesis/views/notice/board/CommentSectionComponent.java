package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.Comment;
import com.example.thesis.backend.notice.CommentService;
import com.example.thesis.backend.notice.Notice;
import com.example.thesis.backend.notice.NoticeService;
import com.example.thesis.backend.security.auth.User;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

@CssImport("./styles/views/notice/board/comment-section.css")
public class CommentSectionComponent extends VerticalLayout {

    private static final int MAX_DEPTH = 4;

    public CommentSectionComponent(Notice notice, CommentService commentService, NoticeService noticeService) {
        setId("comment-section");

        TextField joinDiscussion = new TextField("Leave a comment");
        joinDiscussion.setId("comment-box");
        joinDiscussion.addKeyDownListener(Key.ENTER, e -> {
//            User user = new User();         //TODO TESTOWO - WYCIAGAC Z SYSTEMU JAKOS
//            user.setFirstName("Test");
//            user.setLastName("Test");

//            Comment comment = new Comment(null, 0,  user, joinDiscussion.getValue());
//            noticeService.addComment(notice, comment);
//            commentService.addComment(comment);
        });

        Button addCommentButton = new Button("Add Comment");
        addCommentButton.addClickListener(e -> {
//            User user = new User();         //TODO TESTOWO - WYCIAGAC Z SYSTEMU JAKOS
//            user.setFirstName("Test");
//            user.setLastName("Test");
//
//            Comment comment = new Comment(null, 0,  user, joinDiscussion.getValue()); //tutaj będzie dodawanie do komentarza ojca więc nie może tu być null i 0 w parametrach
//            noticeService.addComment(notice, comment);
//            commentService.addComment(comment);
        });

        add(joinDiscussion, addCommentButton);

        Div commentsBox = new Div();
        commentsBox.setId("comments-box");

        for (Comment comment : notice.getComments()) {
            CommentComponent commentComponent = new CommentComponent(comment);
//            String margin = getCommentMargin(comment);
//            commentComponent.getStyle().set("margin-left", margin);
            if(!comment.isParentComment()) {
                commentComponent.getStyle().set("margin-left", "2em");
            }
            commentsBox.add(commentComponent);
        }
        add(commentsBox);
    }
//
//    private String getCommentMargin(Comment comment) {
//        int depth = comment.getDepth() <= MAX_DEPTH ? comment.getDepth() : MAX_DEPTH;
//        depth = depth * 2;
//        return depth + "em";
//    }
}