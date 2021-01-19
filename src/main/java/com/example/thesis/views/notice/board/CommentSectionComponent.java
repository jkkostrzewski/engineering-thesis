package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.*;
import com.example.thesis.views.utilities.CommentBroadcaster;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

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
        loadParentComments(getCurrentNotice(noticeId));
        add(commentsBox);
    }

    private Notice getCurrentNotice(long noticeId) {
        return this.noticeView.getNoticeService().findById(noticeId).getContent();
    }

    public void refreshCommentSection(Long noticeId) {
        if (this.noticeId == noticeId) {
            List<ParentComment> parentComments = (List<ParentComment>) getCurrentNotice(noticeId).getParentComments();
            List<ParentComment> leftToAdd = new ArrayList<>(parentComments);
            commentsBox.getChildren().forEach(component -> {
                CommentComponent commentComponent = (CommentComponent) component;
                parentComments.forEach(parentComment -> {
                    if (commentComponent.hasSameParentComment(parentComment)) {
                        commentComponent.findAllNotInserted(parentComment).forEach(comment -> commentsBox.addComponentAtIndex(findLastReplyIndex(parentComment), new CommentComponent(comment)));
                        commentComponent.setParentComment(parentComment);
                        leftToAdd.remove(parentComment);
                    }
                });
            });
            leftToAdd.forEach(parentComment -> commentsBox.add(new CommentComponent(noticeView, parentComment)));
        }
    }

    private int findLastReplyIndex(ParentComment parentComment) {
        Object[] commentComponents = commentsBox.getChildren().toArray();

        for (int i=0; i<commentComponents.length; i++) {
            CommentComponent component = (CommentComponent) commentComponents[i];
            if (component.hasSameParentComment(parentComment)) {
                return i + component.getReplySize() + 1;
            }
        }
        return 0;
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
