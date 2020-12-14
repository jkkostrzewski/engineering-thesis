package com.example.thesis.views.notice.board;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

@CssImport("./styles/views/notice/board/leave-comment-component.css")
public class LeaveCommentComponent extends VerticalLayout {

    private final TextArea commentBox;
    private final Button submit;

    public LeaveCommentComponent() {
        setId("leave-comment-component");

        Div innerBox = new Div();
        innerBox.setId("inner-box");

        commentBox = new TextArea();
        commentBox.setPlaceholder("Leave a comment...");
        commentBox.setId("comment-box");

        submit = new Button("Submit");
        submit.setId("submit-button");
        //TODO - add click listener add to repository and to commentsectioncomponent

        innerBox.add(commentBox, submit);
        add(innerBox);
    }
}
