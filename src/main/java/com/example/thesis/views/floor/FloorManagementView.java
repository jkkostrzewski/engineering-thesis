package com.example.thesis.views.floor;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.floor.FloorRepository;
import com.example.thesis.backend.notice.NoticeBoard;
import com.example.thesis.backend.notice.NoticeBoardRepository;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = FloorManagementView.ROUTE, layout = MainView.class)
@PageTitle("Floor management")
@CssImport("./styles/views/floor/floor-management-view.css")
@Secured(FloorManagementView.PRIVILEGE)
public class FloorManagementView extends VerticalLayout {

    public static final String ROUTE = "/add-floor";
    public static final String PRIVILEGE = "ADD_FLOOR_VIEW_PRIVILEGE";

    @Autowired
    private final FloorRepository floorRepository;

    @Autowired
    private final NoticeBoardRepository noticeBoardRepository;

    private final Button confirm;
    private final TextField floorName;
    private final Label label;

    private Floor floor;

    public FloorManagementView(FloorRepository floorRepository, NoticeBoardRepository noticeBoardRepository) {
        this.floorRepository = floorRepository;
        this.noticeBoardRepository = noticeBoardRepository;

        label = new Label("Create floor");
        floorName = new TextField("Enter floor name");
        confirm = new Button("Confirm");
        confirm.addClickListener(e -> {
            createFloor();
            createNoticeBoard();
            Notification.show("Floor created successfully"); //TODO refresh tab menu on left
        });

        add(label, floorName, confirm);
        this.setAlignItems(Alignment.CENTER);
    }

    private void createNoticeBoard() {
        NoticeBoard board = new NoticeBoard(floorName.getValue(), floor);
        noticeBoardRepository.save(board);
    }

    private void createFloor() {
        floor = Floor.builder().name(floorName.getValue()).build();
        floorRepository.save(floor);
    }
}
