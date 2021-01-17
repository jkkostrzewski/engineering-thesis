package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.Comment;
import com.example.thesis.backend.notice.Notice;
import com.example.thesis.backend.notice.ParentComment;
import com.example.thesis.backend.security.auth.User;
import com.example.thesis.views.utilities.CommentBroadcaster;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import java.time.Instant;

@CssImport("./styles/views/notice/board/leave-comment-component.css")
public class LeaveCommentComponent extends VerticalLayout {

    private final NoticeView noticeView;
    private TextArea commentBox;
    private Button submit;
    private Div innerBox;

    public LeaveCommentComponent(NoticeView noticeView) {   //TODO check if notice persists after adding parent comment
        innerBox = new Div();
        innerBox.setId("inner-box-parent");

        constructUnchangeableParts();

        this.noticeView = noticeView;
        submit.addClickListener(e -> {
            addParentComment();
            CommentBroadcaster.broadcast();
        });
    }

    public LeaveCommentComponent(NoticeView noticeView, ParentComment parent) {   //TODO check if notice persists after adding parent comment
        innerBox = new Div();
        innerBox.setId("inner-box-reply");

        constructUnchangeableParts();

        this.noticeView = noticeView;

        submit.addClickListener(e ->  {
            addReply(parent);
            CommentBroadcaster.broadcast();
        });
    }

    private void constructUnchangeableParts() {
        setId("leave-comment-component");

        commentBox = new TextArea();
        commentBox.setPlaceholder("Leave a comment...");
        commentBox.setId("comment-box");

        submit = new Button("Submit");
        submit.setId("submit-button");

        innerBox.add(commentBox, submit);
        add(innerBox);
    }

    private void addParentComment() {
        ParentComment comment = new ParentComment(noticeView.getCurrentUser(), Instant.now(), commentBox.getValue());
        noticeView.getCommentService().addParentComment(noticeView.getNotice().getId(), comment);
    }

    private void addReply(ParentComment parent) {
        Comment comment = new Comment(noticeView.getCurrentUser(), Instant.now(), commentBox.getValue());
        noticeView.getCommentService().addComment(noticeView.getNotice().getId(), parent, comment);
    }
}
