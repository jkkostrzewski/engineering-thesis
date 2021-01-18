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
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

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

        HorizontalLayout boardHeader = new HorizontalLayout();
        boardHeader.setId("board-header");

        Paragraph boardNameText = new Paragraph(noticeBoard.getName());
        boardNameText.setId("board-name");

        boardHeader.add(boardNameText);
        boardHeader.expand(boardNameText);

        if (userHasRole(AddNoticeView.PRIVILEGE)) {
            Button addNotice = new Button("Add notice");
            addNotice.setId("add-notice-button");
            addNotice.addClickListener(e -> UI.getCurrent().navigate(AddNoticeView.class, boardName));
            boardHeader.add(addNotice);
        }

        add(boardHeader);

        for (Notice notice : noticeBoard.getNotices()) {
            add(new NoticeComponent(notice));
        }
    }
}