package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.Notice;
import com.example.thesis.backend.notice.NoticeBoard;
import com.example.thesis.backend.notice.NoticeService;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.thesis.backend.security.SecurityUtils.userHasRole;


@Route(value = NoticeBoardView.ROUTE, layout = MainView.class)
@PageTitle("Notice board")
@CssImport("./styles/views/notice/board/notice-board.css")
@Secured(NoticeBoardView.PRIVILEGE)
public class NoticeBoardView extends VerticalLayout implements HasUrlParameter<String> {
    public static final String PRIVILEGE = "NOTICE_BOARD_VIEW_PRIVILEGE";
    public static final String ROUTE = "/notice-board";

    private final NoticeService noticeService;
    private NoticeBoard noticeBoard;

    @Id("board-header")
    private HorizontalLayout boardHeader;

    @Id("board-name")
    private Paragraph boardNameText;

    @Autowired
    public NoticeBoardView(NoticeService noticeService) {
        setId("notice-board");
        this.noticeService = noticeService;

        this.setAlignItems(Alignment.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String boardName) {
        noticeBoard = noticeService.findByName(boardName.replace("%20", " "));
        removeAll();

        boardHeader = new HorizontalLayout();
        boardNameText = new Paragraph(noticeBoard.getName());

        boardHeader.add(boardNameText);
        boardHeader.expand(boardNameText);

        if (userHasRole(EditNoticeView.PRIVILEGE)) {
            Button addNotice = new Button("Add notice");
            addNotice.setId("add-notice-button");

            String route = RouteConfiguration.forSessionScope().getUrl(EditNoticeView.class);
            List<String> boardNameParameter = new ArrayList<>();
            boardNameParameter.add(boardName);

            Map<String, List<String>> parameterMap = new HashMap<>();
            parameterMap.put("boardName", boardNameParameter);

            QueryParameters parameters = new QueryParameters(parameterMap);
            addNotice.addClickListener(e -> UI.getCurrent().navigate(route, parameters));
            boardHeader.add(addNotice);
        }

        add(boardHeader);

        for (Notice notice : noticeBoard.getNotices()) {
            add(new NoticeComponent(notice, noticeBoard.getName()));
        }
    }
}