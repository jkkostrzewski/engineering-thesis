package com.example.thesis.backend.security.utilities;

import com.example.thesis.backend.security.auth.Privilege;
import com.example.thesis.backend.security.auth.PrivilegeRepository;
import com.example.thesis.backend.security.auth.Role;
import com.example.thesis.backend.security.auth.RoleRepository;
import com.example.thesis.views.auth.UserManagementView;
import com.example.thesis.views.floor.FloorManagementView;
import com.example.thesis.views.notice.board.AddNoticeView;
import com.example.thesis.views.notice.board.NoticeBoardView;
import com.example.thesis.views.notice.board.NoticeView;
import com.example.thesis.views.reservation.ReservationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class DefaultPrivilegeProvider {

    @Autowired
    private final PrivilegeRepository privilegeRepository;

    @Autowired
    private final RoleRepository roleRepository;

    private final Privilege noticeView;
    private final Privilege noticeBoardView;
    private final Privilege addNoticeView;
    private final Privilege reservationView;
    private final Privilege userRegistrationView;
    private final Privilege addFloorView;

    public DefaultPrivilegeProvider(PrivilegeRepository privilegeRepository,
                                    RoleRepository roleRepository) {
        this.privilegeRepository = privilegeRepository;
        this.roleRepository = roleRepository;

        noticeView = createPrivilegeIfNotFound(NoticeView.PRIVILEGE);
        noticeBoardView = createPrivilegeIfNotFound(NoticeBoardView.PRIVILEGE);
        addNoticeView = createPrivilegeIfNotFound(AddNoticeView.PRIVILEGE);
        reservationView = createPrivilegeIfNotFound(ReservationView.PRIVILEGE);
        userRegistrationView = createPrivilegeIfNotFound(UserManagementView.PRIVILEGE);
        addFloorView = createPrivilegeIfNotFound(FloorManagementView.PRIVILEGE);
    }

    public Role user(String username) {
        List<Privilege> privileges = Arrays.asList(noticeBoardView, noticeView, reservationView);
        return createRoleIfNotFound(username, privileges);
    }

    public Role floorAdmin(String username) {
        List<Privilege> privileges = Arrays.asList(noticeBoardView, noticeView, reservationView);
        return createRoleIfNotFound(username, privileges);
    }

    public Role admin(String username) {
        List<Privilege> privileges = Arrays.asList(noticeBoardView, noticeView, reservationView, addNoticeView,
                userRegistrationView, addFloorView);
        return createRoleIfNotFound(username, privileges);
    }

    @Transactional
    public Role createRoleIfNotFound(String name, List<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }
}
