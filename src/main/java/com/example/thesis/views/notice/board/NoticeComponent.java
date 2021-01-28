package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.Notice;
import com.example.thesis.views.utilities.HtmlUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import static com.example.thesis.views.utilities.DateFormatters.STANDARD_DATE_TIME;

@CssImport("./styles/views/notice/board/notice.css")
public class NoticeComponent extends VerticalLayout {

    public static int BODY_MAX_LENGTH = 350;

    private Notice notice;

    @Id("notice-title")
    private final Paragraph title;

    @Id("notice-date")
    private final Paragraph date;

    @Id("body")
    private Div body;

    @Id("read-more")
    private final Button readMore;

    public NoticeComponent(Notice notice) {
        setId("notice");
        this.notice = notice;

        title = new Paragraph(notice.getTitle());
        title.setSizeUndefined();

        date = new Paragraph(STANDARD_DATE_TIME.format(notice.getCreationDate()));
        date.setSizeUndefined();

        add(title, date);

        if (!Objects.isNull(notice.getImage())) {
            StreamResource imageResource = new StreamResource("noticeImage.jpg", () -> new ByteArrayInputStream(notice
                    .getImage()));
            Image image = new Image(imageResource, "NoticeImage");
            image.setId("image");
            image.setSizeUndefined();
            add(image);
        }
        body = new Div();
        body.setClassName("truncate-text");
        HtmlUtil.setInnerHtml(body.getElement(), notice.getBody());

        readMore = new Button("Read More");
        readMore.addClickListener(event -> navigateToNoticeView());

        title.addClickListener(event -> navigateToNoticeView());

        setSizeUndefined();
        add(body, readMore);
    }

    private void navigateToNoticeView() {
        UI.getCurrent().navigate(NoticeView.class, notice.getId());
    }
}
