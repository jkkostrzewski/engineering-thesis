package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.CommentService;
import com.example.thesis.backend.notice.Notice;
import com.example.thesis.backend.notice.NoticeService;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.io.ByteArrayInputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Route(value = NoticeView.ROUTE, layout = MainView.class)
@PageTitle("Notice view")
@CssImport("./styles/views/notice/notice-view.css")
@Secured(NoticeView.PRIVILEGE)
public class NoticeView extends VerticalLayout implements HasUrlParameter<Long> {

    public static final String PRIVILEGE = "NOTICE_VIEW_PRIVILEGE";
    public static final String ROUTE = "notice-view";
    public static final int BODY_MAX_LENGTH = 770;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                                                           .withZone(ZoneId
                                                                                   .systemDefault()); //TODO wyrzucić to do klasy utilities?
    private Notice notice;
    private CommentService commentService;
    private NoticeService noticeService;

    @Autowired
    public NoticeView(NoticeService noticeService, CommentService commentService) {
        setId("notice-view");
        this.noticeService = noticeService;
        this.commentService = commentService;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {       //TODO ta metoda jest wywołana po konstruktorze - zastanów się nad zmianą tej metody
        this.notice = noticeService.findById(parameter).getContent();

        Paragraph title = new Paragraph(notice.getTitle());
        title.setId("notice-view-title");
        title.setSizeUndefined();

        Paragraph date = new Paragraph(DATE_TIME_FORMATTER.format(notice.getCreationDate()));
        date.setId("notice-view-date");
        date.setSizeUndefined();

        StreamResource imageResource = new StreamResource("noticeImage.jpg", () -> new ByteArrayInputStream(notice
                .getImage()));
        Image image = new Image(imageResource, "NoticeImage");
        image.setId("notice-view-image");
        image.setSizeUndefined();
        Paragraph body = new Paragraph(notice.getBody());

        CommentSectionComponent commentSection = new CommentSectionComponent(notice, commentService, noticeService);

        setSizeUndefined();
        add(title, date, image, body, commentSection);
    }
}
