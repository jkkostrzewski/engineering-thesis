package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.Notice;
import com.example.thesis.backend.security.SecurityUtils;
import com.example.thesis.backend.security.utilities.PrivilegeProvider;
import com.example.thesis.views.utilities.HtmlUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.thesis.views.utilities.DateFormatters.STANDARD_DATE_TIME;

@CssImport("./styles/views/notice/board/notice.css")
public class NoticeComponent extends VerticalLayout {

    private final Notice notice;

    @Id("notice-title")
    private final Paragraph title;

    @Id("notice-date")
    private final Paragraph date;

    @Id("body")
    private final Div body;

    @Id("read-more")
    private final Button readMore;

    @Id("header")
    private final HorizontalLayout header;

    @Id("edit-button")
    private final Button editButton;

    public NoticeComponent(Notice notice, String boardName) {
        setId("notice");
        this.notice = notice;

        header = new HorizontalLayout();
        header.setId("header");

        title = new Paragraph(notice.getTitle());
        title.setId("notice-title");

        editButton = new Button(new Icon(VaadinIcon.EDIT));
        editButton.setId("edit-button");
        if(userNotEligibleToEdit()) {
            editButton.setVisible(false);
        }
        editButton.addClickListener(event -> {
            String route = RouteConfiguration.forSessionScope().getUrl(EditNoticeView.class);
            List<String> boardNameParameter = new ArrayList<>();
            boardNameParameter.add(boardName.replace(" ", "%20"));

            List<String> noticeParameter = new ArrayList<>();
            noticeParameter.add(String.valueOf(notice.getId()));

            Map<String, List<String>> parameterMap = new HashMap<>();
            parameterMap.put("boardName", boardNameParameter);
            parameterMap.put("noticeId", noticeParameter);

            QueryParameters parameters = new QueryParameters(parameterMap);
            UI.getCurrent().navigate(route, parameters);
        });
        header.add(title, editButton);

        date = new Paragraph(STANDARD_DATE_TIME.format(notice.getCreationDate()));

        add(header, date);

        if (Objects.nonNull(notice.getImage())) {
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

    private boolean userNotEligibleToEdit() {
        String loggedUsername = SecurityUtils.getLoggedUserUsername();

        if (SecurityUtils.userHasPrivilege(PrivilegeProvider.ADMIN_PRIVILEGE)) {
            return false;
        }

        //TODO user is floor admin and this is his floor -> return false

        if (loggedUsername.equals(notice.getCreatedByUsername())) {
            return false;
        }

        return true;
    }

    private void navigateToNoticeView() {
        UI.getCurrent().navigate(NoticeView.class, notice.getId());
    }
}
