package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.*;
import com.example.thesis.backend.security.auth.User;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./styles/views/notice/board/comment-section.css")
public class CommentSectionComponent extends VerticalLayout {

    private final Div commentsBox;
    private final NoticeView noticeView;

    public CommentSectionComponent(NoticeView noticeView) {
        setId("comment-section");

        this.noticeView = noticeView;

        LeaveCommentComponent leaveCommentComponent = new LeaveCommentComponent(noticeView);
        add(leaveCommentComponent);

        commentsBox = new Div();
        commentsBox.setId("comments-box");

        createCommentComponents(noticeView.getNotice(), commentsBox);
        add(commentsBox);
    }

    private void createCommentComponents(Notice notice, Div commentsBox) {
        for (ParentComment parentComment : notice.getParentComments()) {
            CommentComponent commentComponent = new CommentComponent(noticeView, parentComment);
            commentsBox.add(commentComponent);
            createReplyComponents(parentComment);
        }
    }

    private void createReplyComponents(ParentComment parentComment) {
        for (Comment reply : parentComment.getReplies()) {
            CommentComponent replyComponent = new CommentComponent(reply);
            commentsBox.add(replyComponent);
        }
    }
}
