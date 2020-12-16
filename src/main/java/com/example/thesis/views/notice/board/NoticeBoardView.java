package com.example.thesis.views.notice.board;

import com.example.thesis.backend.notice.Notice;
import com.example.thesis.backend.notice.NoticeBoard;
import com.example.thesis.backend.notice.NoticeBoardRepository;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;


@Route(value = NoticeBoardView.ROUTE, layout = MainView.class)
@PageTitle("Notice board")
@CssImport("./styles/views/notice/board/notice-board.css")
@Secured(NoticeBoardView.PRIVILEGE)
public class NoticeBoardView extends VerticalLayout implements HasUrlParameter<String> {
    public static final String PRIVILEGE = "NOTICE_BOARD_VIEW_PRIVILEGE";
    public static final String ROUTE = "/notice-board";

    private NoticeBoardRepository noticeBoardRepository;

    @Autowired
    public NoticeBoardView(NoticeBoardRepository noticeBoardRepository) {
        setId("notice-board");
        this.noticeBoardRepository = noticeBoardRepository;

//        Pageable firstPage = PageRequest.of(0, 2);  //TODO zrobić inny konstruktor dla kolejnych stron?
        //np. NoticeBoardView(int page)

        this.setAlignItems(Alignment.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String boardName) {
        NoticeBoard noticeBoard = noticeBoardRepository.findByName(boardName.replace("%20", " "));

        removeAll();

//        Page page = UI.getCurrent().getPage();        //Trying to get last page visited without sending it as a state every time
//        page.executeJs("document.referrer").then(String.class, result -> {
//            if (lastPageWasBoard(result)) {
//                UI.getCurrent().getPage().reload();
//            }
//        });

        for (Notice notice : noticeBoard.getNotices()) {
            add(new NoticeComponent(notice));
        }
    }
}