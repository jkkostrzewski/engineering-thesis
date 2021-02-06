package com.example.thesis.backend.security.utilities;

import com.example.thesis.backend.security.auth.Privilege;
import com.example.thesis.backend.security.auth.PrivilegeRepository;
import com.example.thesis.backend.security.auth.Role;
import com.example.thesis.backend.security.auth.RoleRepository;
import com.example.thesis.views.auth.UserManagementView;
import com.example.thesis.views.floor.FloorManagementView;
import com.example.thesis.views.notice.board.EditNoticeView;
import com.example.thesis.views.notice.board.NoticeBoardView;
import com.example.thesis.views.notice.board.NoticeView;
import com.example.thesis.views.property.PropertyManagementView;
import com.example.thesis.views.reservation.ReservationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Component
public class PrivilegeProvider {

    public static final String ADMIN_PRIVILEGE = "ADMIN_PRIVILEGE";
    public static final String FLOOR_ADMIN_PRIVILEGE = "FLOOR_ADMIN_PRIVILEGE";
    public static final String USER_PRIVILEGE = "USER_PRIVILEGE";

    @Autowired
    private final PrivilegeRepository privilegeRepository;

    @Autowired
    private final RoleRepository roleRepository;

    private final Privilege noticeView;
    private final Privilege noticeBoardView;
    private final Privilege editNoticeView;
    private final Privilege reservationView;
    private final Privilege userManagementView;
    private final Privilege floorManagementView;
    private final Privilege propertyManagementView;
    private final Privilege adminPrivilege;
    private final Privilege userPrivilege;
    private final Privilege floorAdminPrivilege;

    public PrivilegeProvider(PrivilegeRepository privilegeRepository,
                             RoleRepository roleRepository) {
        this.privilegeRepository = privilegeRepository;
        this.roleRepository = roleRepository;

        adminPrivilege = createPrivilegeIfNotFound(ADMIN_PRIVILEGE);
        floorAdminPrivilege = createPrivilegeIfNotFound(FLOOR_ADMIN_PRIVILEGE);
        userPrivilege = createPrivilegeIfNotFound(USER_PRIVILEGE);

        noticeView = createPrivilegeIfNotFound(NoticeView.PRIVILEGE);
        noticeBoardView = createPrivilegeIfNotFound(NoticeBoardView.PRIVILEGE);
        editNoticeView = createPrivilegeIfNotFound(EditNoticeView.PRIVILEGE);
        reservationView = createPrivilegeIfNotFound(ReservationView.PRIVILEGE);
        userManagementView = createPrivilegeIfNotFound(UserManagementView.PRIVILEGE);
        floorManagementView = createPrivilegeIfNotFound(FloorManagementView.PRIVILEGE);
        propertyManagementView = createPrivilegeIfNotFound(PropertyManagementView.PRIVILEGE);
    }

    public Role user(String username) {
        List<Privilege> privileges = Arrays.asList(userPrivilege, noticeBoardView, noticeView, reservationView);
        return createRoleIfNotFound(username, privileges);
    }

    public Role floorAdmin(String username) {
        List<Privilege> privileges = Arrays.asList(floorAdminPrivilege, noticeBoardView, noticeView,
                reservationView, editNoticeView, propertyManagementView);
        return createRoleIfNotFound(username, privileges);
    }

    public Role admin(String username) {
        List<Privilege> privileges = Arrays.asList(adminPrivilege, noticeBoardView, noticeView, reservationView,
                editNoticeView, userManagementView, floorManagementView, propertyManagementView);
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
