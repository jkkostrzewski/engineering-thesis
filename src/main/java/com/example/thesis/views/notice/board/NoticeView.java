package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.CommentService;
import com.example.thesis.backend.notice.Notice;
import com.example.thesis.backend.notice.NoticeService;
import com.example.thesis.backend.security.SecurityUtils;
import com.example.thesis.backend.security.auth.User;
import com.example.thesis.backend.security.auth.UserService;
import com.example.thesis.views.main.MainView;
import com.example.thesis.views.utilities.CommentBroadcaster;
import com.example.thesis.views.utilities.HtmlUtil;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.io.ByteArrayInputStream;

import static com.example.thesis.views.utilities.DateFormatters.STANDARD_DATE_TIME;

@Route(value = NoticeView.ROUTE, layout = MainView.class)
@PageTitle("Notice view")
@CssImport("./styles/views/notice/notice-view.css")
@Secured(NoticeView.PRIVILEGE)
public class NoticeView extends VerticalLayout implements HasUrlParameter<Long> {

    public static final String PRIVILEGE = "NOTICE_VIEW_PRIVILEGE";
    public static final String ROUTE = "notice-view";

    private Registration broadcasterRegistration;

    private final NoticeService noticeService;
    private final UserService userService;
    private final CommentService commentService;

    private Notice notice;
    private User currentUser;
    private CommentSectionComponent commentSection;

    @Autowired
    public NoticeView(NoticeService noticeService, UserService userService, CommentService commentService) {
        this.noticeService = noticeService;
        this.userService = userService;
        this.commentService = commentService;
        setId("notice-view");
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        notice = noticeService.findById(parameter).getContent();

        Paragraph title = new Paragraph(notice.getTitle());
        title.setId("notice-view-title");
        title.setSizeUndefined();

        Paragraph date = new Paragraph(STANDARD_DATE_TIME.format(notice.getCreationDate()));
        date.setId("notice-view-date");
        date.setSizeUndefined();

        StreamResource imageResource = new StreamResource("noticeImage.jpg", () -> new ByteArrayInputStream(notice
                .getImage()));
        Image image = new Image(imageResource, "NoticeImage");
        image.setId("notice-view-image");
        image.setSizeUndefined();
        Div body = new Div();
        HtmlUtil.setInnerHtml(body.getElement(), notice.getBody());

        String username = SecurityUtils.getLoggedUserUsername();
        currentUser = userService.findByUsername(username).orElseThrow(RuntimeException::new);

        commentSection = new CommentSectionComponent(this);

        setSizeUndefined();
        add(title, date, image, body, commentSection);
    }

    public Notice getNotice() {
        return notice;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public CommentService getCommentService() {
        return commentService;
    }

    public NoticeService getNoticeService() {
        return noticeService;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();
        broadcasterRegistration = CommentBroadcaster.register(noticeId ->
                ui.access(() -> commentSection.refreshCommentSection(noticeId)));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        broadcasterRegistration.remove();
        broadcasterRegistration = null;
    }
}
