package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.Notice;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@CssImport("./styles/views/notice/board/notice.css")
public class NoticeComponent extends VerticalLayout {

    public static int BODY_MAX_LENGTH = 770;
    public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                                                           .withZone(ZoneId
                                                                                   .systemDefault()); //TODO wyrzucić to do klasy utilities?

    private Notice notice;

    public NoticeComponent(Notice notice) {
        setId("notice");
        this.notice = notice;

        Paragraph title = new Paragraph(notice.getTitle());
        title.setId("notice-title");
        title.setSizeUndefined();

        Paragraph date = new Paragraph(DATE_TIME_FORMATTER.format(notice.getCreationDate()));
        date.setId("notice-date");
        date.setSizeUndefined();

        StreamResource imageResource = new StreamResource("noticeImage.jpg", () -> new ByteArrayInputStream(notice
                .getImage()));
        Image image = new Image(imageResource, "NoticeImage");
        image.setId("image");
        image.setSizeUndefined();
        Paragraph body = new Paragraph(notice.getBody());

        Button readMore = new Button("Read More");

        readMore.addClickListener(e -> navigateToNoticeView());

        if (body.getText().length() > BODY_MAX_LENGTH) {        //TODO skalowanie
            body.setText(body.getText().substring(0, BODY_MAX_LENGTH + 1) + "...");
            readMore.setVisible(true);
        } else {
            readMore.setVisible(false);
        }

        title.addClickListener(event -> navigateToNoticeView());

        setSizeUndefined();
        add(title, date, image, body, readMore);
    }

    private void navigateToNoticeView() {
        UI.getCurrent().navigate("notice-view/" + notice.getId());
    }
}
