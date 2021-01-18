package com.example.thesis.views.floor;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.floor.FloorService;
import com.example.thesis.backend.notice.NoticeBoard;
import com.example.thesis.backend.notice.NoticeService;
import com.example.thesis.backend.security.auth.User;
import com.example.thesis.backend.security.auth.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class FloorCreationLayout extends VerticalLayout {

    private final FloorService floorService;
    private final NoticeService noticeService;
    private final UserService userService;

    private final Button confirm;
    private final TextField floorName;

    private Floor floor;

    public FloorCreationLayout(FloorService floorService, NoticeService noticeService, UserService userService) {
        this.floorService = floorService;
        this.noticeService = noticeService;
        this.userService = userService;

        floorName = new TextField("Enter floor name");
        confirm = new Button("Confirm");
        confirm.addClickListener(e -> {
            createFloor();
            createNoticeBoard();
            assignNewFloorToAdmin();
            Notification.show("Floor created successfully"); //TODO refresh tab menu on left
        });

        add(floorName, confirm);
    }

    private void createNoticeBoard() {
        NoticeBoard board = new NoticeBoard(floorName.getValue(), floor);
        noticeService.save(board);
    }

    private void createFloor() {
        floor = Floor.builder().name(floorName.getValue()).build();
        floorService.save(floor);
    }

    private void assignNewFloorToAdmin() {  //TODO change to assingNewFloorToAllAdministrators
        User admin = userService.findByUsername("admin").orElseThrow(RuntimeException::new);
        admin.addFloor(floor);
        userService.save(admin);
    }
}
