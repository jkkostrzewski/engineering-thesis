package com.example.thesis.views.floor;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.floor.FloorRepository;
import com.example.thesis.backend.notice.NoticeBoard;
import com.example.thesis.backend.notice.NoticeBoardRepository;
import com.example.thesis.backend.security.auth.User;
import com.example.thesis.backend.security.auth.UserRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class FloorCreationLayout extends VerticalLayout {

    private final FloorRepository floorRepository;
    private final NoticeBoardRepository noticeBoardRepository;
    private final UserRepository userRepository;

    private final Button confirm;
    private final TextField floorName;

    private Floor floor;

    public FloorCreationLayout(FloorRepository floorRepository, NoticeBoardRepository noticeBoardRepository, UserRepository userRepository) {
        this.floorRepository = floorRepository;
        this.noticeBoardRepository = noticeBoardRepository;
        this.userRepository = userRepository;

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
        noticeBoardRepository.save(board);
    }

    private void createFloor() {
        floor = Floor.builder().name(floorName.getValue()).build();
        floorRepository.save(floor);
    }

    private void assignNewFloorToAdmin() {  //TODO change to assingNewFloorToAllAdministrators
        User admin = userRepository.findByUsername("admin").get();        //setup admin username in some configuration class?
        admin.addFloor(floor);
        userRepository.save(admin);
    }
}
