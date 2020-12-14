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

    @Autowired
    private CommentRepository commentRepository;

    public CommentComponent(Comment comment) {
        this.comment = comment;
        setId("comment-component");

        if(!(comment instanceof ParentComment)) {
            this.getStyle().set("margin-left", "10em auto");
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
        nameAndDate.add(creationDate);

        Paragraph content = new Paragraph(comment.getBody());

        Button reply = new Button("reply");
        reply.addClickListener(e -> {
//            User user = new User();         //TODO TESTOWO - WYCIAGAC Z SYSTEMU JAKOS
//            user.setFirstName("Test");
//            user.setLastName("Test");

//            Comment commentz = new Comment(null, user, joinDiscussion.getValue());
//            addCommentToRepository(commentz);
        });

        add(nameAndDate, content, reply);
    }

    private String getFormattedTime() {
        return new PrettyTime().format(Date.from(comment.getCreationDate()));
    }

    private void addCommentToRepository(Comment comment) {
        commentRepository.save(comment);
    }
}
