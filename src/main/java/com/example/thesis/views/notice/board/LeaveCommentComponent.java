package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.Comment;
import com.example.thesis.backend.notice.ParentComment;
import com.example.thesis.views.utilities.CommentBroadcaster;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.time.Instant;

@CssImport("./styles/views/notice/board/leave-comment-component.css")
public class LeaveCommentComponent extends VerticalLayout {

    private final NoticeView noticeView;
    private TextArea commentBox;
    private Div innerBox;
    private HorizontalLayout footer;
    private Paragraph characterCounter;
    private Button submit;

    public LeaveCommentComponent(NoticeView noticeView) {
        innerBox = new Div();
        innerBox.setId("inner-box-parent");

        constructUnchangeableParts();

        this.noticeView = noticeView;
        submit.addClickListener(e -> {
            if(!commentBox.getValue().isEmpty()) {
                addParentComment();
                CommentBroadcaster.broadcast(this.noticeView.getNotice().getId());
                commentBox.setValue("");
            } else {
                Notification.show("You cannot add an empty comment!");
            }
        });
    }

    public LeaveCommentComponent(NoticeView noticeView, Button reply, ParentComment parent) {
        innerBox = new Div();
        innerBox.setId("inner-box-reply");

        constructUnchangeableParts();

        this.noticeView = noticeView;

        submit.addClickListener(e ->  {
            if(!commentBox.getValue().isEmpty()) {
                addReply(parent);
                CommentBroadcaster.broadcast(this.noticeView.getNotice().getId());
                this.setVisible(false);
                reply.setText("Reply");
                commentBox.setValue("");
            } else {
                Notification.show("You cannot add an empty comment!");
            }
        });
    }

    public void clearCommentBox() {
        commentBox.clear();
    }

    private void constructUnchangeableParts() {
        setId("leave-comment-component");

        commentBox = new TextArea();
        commentBox.setPlaceholder("Leave a comment...");
        commentBox.setId("comment-box");
        commentBox.setValueChangeMode(ValueChangeMode.EAGER);
        commentBox.addValueChangeListener(event -> {
            characterCounter.setText(commentBox.getValue().length() + "/350");
        });
        commentBox.setMaxLength(350);

        footer = new HorizontalLayout();
        footer.setId("footer");

        characterCounter = new Paragraph("0/350");
        footer.expand(characterCounter);

        submit = new Button("Submit");
        submit.setId("submit-button");
        footer.add(characterCounter, submit);

        innerBox.add(commentBox, footer);
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
