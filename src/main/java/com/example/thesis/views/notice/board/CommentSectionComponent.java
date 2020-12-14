package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./styles/views/notice/board/comment-section.css")
public class CommentSectionComponent extends VerticalLayout {

    private static final int MAX_DEPTH = 4;
    private final Div commentsBox;

    public CommentSectionComponent(Notice notice, CommentService commentService, NoticeService noticeService) {
        setId("comment-section");

        LeaveCommentComponent leaveCommentComponent = new LeaveCommentComponent();
        add(leaveCommentComponent);

        commentsBox = new Div();
        commentsBox.setId("comments-box");

        createCommentComponents(notice, commentsBox);
        add(commentsBox);
    }

    private void createCommentComponents(Notice notice, Div commentsBox) {
        for (ParentComment parentComment : notice.getParentComments()) {
            CommentComponent commentComponent = new CommentComponent(parentComment);
//            String margin = getCommentMargin(comment);
//            commentComponent.getStyle().set("margin-left", margin);
//            if(!comment.isParentComment()) {
////                String margin = commentComponent.getStyle().get("margin")
//                commentComponent.getStyle().set("margin-left", "20px auto");
//            }
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
//
//    private String getCommentMargin(Comment comment) {
//        int depth = comment.getDepth() <= MAX_DEPTH ? comment.getDepth() : MAX_DEPTH;
//        depth = depth * 2;
//        return depth + "em";
//    }
}
