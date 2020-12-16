package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.Comment;
import com.example.thesis.backend.notice.CommentRepository;
import com.example.thesis.backend.notice.ParentComment;
import com.example.thesis.backend.security.auth.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.Date;

@CssImport("./styles/views/notice/board/comment.css")
@Configurable
public class CommentComponent extends VerticalLayout {

    private final Comment comment;

    public CommentComponent(Comment comment) {  //TODO - zrobić rozwijanie odpowiedzi (rozdzielić komponent odpowiedzi i parentCommenta?)
        this.comment = comment;
        setId("comment-component");

        if(!(comment instanceof ParentComment)) {
            setId("reply-comment-component");
        }

        HorizontalLayout nameAndDate = new HorizontalLayout();

//        Paragraph fullName = new Paragraph(comment.getUser().getFullName());
        Paragraph fullName = new Paragraph(comment instanceof ParentComment ? "PARENT COMMENT" : "REPLY");
        fullName.setId("full-name");
        nameAndDate.add(fullName);

//        Paragraph parentCommentUsername;
//        if (comment.getParentComment() != null) {
//            parentCommentUsername = new Paragraph(comment.getParentComment().getUser().getFullName());
//            nameAndDate.add(parentCommentUsername);
//        }

        Paragraph creationDate = new Paragraph(getFormattedTime());
        creationDate.setId("creation-date");
        nameAndDate.add(creationDate);

        Paragraph content = new Paragraph(comment.getBody());
        content.setId("comment-content");

        Button reply = new Button("Reply");
        reply.setId("reply-button");
        reply.addClickListener(e -> {
//            User user = new User();
//            user.setFirstName("Test");
//            user.setLastName("Test");

//            Comment commentz = new Comment(null, user, joinDiscussion.getValue());
//            addCommentToRepository(commentz);
        });

        add(nameAndDate, content);

        if(comment instanceof ParentComment) {
            add(reply);
        }
    }

    private String getFormattedTime() {
        return new PrettyTime().format(Date.from(comment.getCreationDate()));
    }
}
