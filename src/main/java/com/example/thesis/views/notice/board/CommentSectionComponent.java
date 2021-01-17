package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.*;
import com.example.thesis.views.utilities.CommentBroadcaster;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./styles/views/notice/board/comment-section.css")
public class CommentSectionComponent extends VerticalLayout {

    private final Div commentsBox;
    private final NoticeView noticeView;

    private final long noticeId;

    public CommentSectionComponent(NoticeView noticeView) {
        setId("comment-section");

        this.noticeView = noticeView;

        LeaveCommentComponent leaveCommentComponent = new LeaveCommentComponent(noticeView);
        add(leaveCommentComponent);

        commentsBox = new Div();
        commentsBox.setId("comments-box");

        noticeId = this.noticeView.getNotice().getId();
        loadParentComments(this.noticeView.getNoticeService().findById(noticeId).getContent());
        add(commentsBox);
    }

    public void refreshCommentSection() {
        loadParentComments(this.noticeView.getNoticeService().findById(this.noticeId).getContent());
    }

    private void loadParentComments(Notice notice) {
        commentsBox.removeAll();
        for (ParentComment parentComment : notice.getParentComments()) {
            createAndAddParentComment(parentComment);
            loadReplies(parentComment);
        }
    }

    private void createAndAddParentComment(ParentComment parentComment) {
        CommentComponent commentComponent = new CommentComponent(noticeView, parentComment);
        commentsBox.add(commentComponent);
    }

    private void loadReplies(ParentComment parentComment) {
        for (Comment reply : parentComment.getReplies()) {
            CommentComponent replyComponent = new CommentComponent(reply);
            commentsBox.add(replyComponent);
        }
    }
}
