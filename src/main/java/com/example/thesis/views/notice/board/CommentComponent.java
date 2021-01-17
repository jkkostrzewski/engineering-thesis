package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.Comment;
import com.example.thesis.backend.notice.ParentComment;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.Date;

@CssImport("./styles/views/notice/board/comment.css")
public class CommentComponent extends VerticalLayout {

    private final Comment comment;
    private HorizontalLayout nameAndDate;
    private Paragraph fullName;
    private Paragraph creationDate;
    private Paragraph content;
    private Button reply;

    public CommentComponent(Comment comment) {
        this.comment = comment;
        setId("reply-comment-component");

        createUnchangeableParts();
    }

    public CommentComponent(NoticeView noticeView, ParentComment comment) {
        this.comment = comment;
        setId("comment-component");

        createUnchangeableParts();

        LeaveCommentComponent commentComponent = new LeaveCommentComponent(noticeView, comment);
        add(commentComponent);
        commentComponent.setVisible(false);

        reply = new Button("Reply");
        reply.setId("reply-button");
        reply.addClickListener(e -> {
            commentComponent.setVisible(!commentComponent.isVisible());
            if (reply.getText().equals("Reply")) {
                reply.setText("Cancel");
            } else {
                reply.setText("Reply");
            }
        });

        add(reply);
    }

    private void createUnchangeableParts() {
        nameAndDate = new HorizontalLayout();

        fullName = new Paragraph(comment.getUser().getFullName());
        fullName.setId("full-name");
        nameAndDate.add(fullName);

        creationDate = new Paragraph(getFormattedTime());
        creationDate.setId("creation-date");
        nameAndDate.add(creationDate);

        content = new Paragraph(comment.getBody());
        content.setId("comment-content");

        add(nameAndDate, content);
    }

    private String getFormattedTime() {
        return new PrettyTime().format(Date.from(comment.getCreationDate()));
    }
}
