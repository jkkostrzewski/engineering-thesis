package com.example.thesis.views.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.notice.NoticeBoard;
import com.example.thesis.backend.notice.NoticeBoardRepository;
import com.example.thesis.backend.security.SecurityUtils;
import com.example.thesis.backend.security.auth.User;
import com.example.thesis.backend.security.auth.UserRepository;
import com.example.thesis.views.auth.UserManagementView;
import com.example.thesis.views.floor.FloorManagementView;
import com.example.thesis.views.notice.board.NoticeBoardView;
import com.example.thesis.views.property.PropertyManagementView;
import com.example.thesis.views.reservation.ReservationView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.example.thesis.views.auth.LoginView;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.example.thesis.backend.security.SecurityUtils.userHasRole;

/**
 * The main view is a top-level placeholder for other views.
 */
@JsModule("./styles/shared-styles.js")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@CssImport("./styles/views/main/main-view.css")
public class MainView extends AppLayout {

    @Autowired
    private final NoticeBoardRepository noticeBoardRepository;

    @Autowired
    private final UserRepository userRepository;

    private final Tabs menu;
    private H1 viewTitle;

    public MainView(NoticeBoardRepository noticeBoardRepository, UserRepository userRepository) {
        this.noticeBoardRepository = noticeBoardRepository;
        this.userRepository = userRepository;

        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        viewTitle.setId("view-title");
        layout.add(viewTitle);
        HorizontalLayout userInfo = new HorizontalLayout();
        userInfo.setId("user-info");
        if (SecurityUtils.isUserLoggedIn()) {
            userInfo.add(new Button("Logout", event -> {
                getUI().get().getPage().executeJs("window.location.href='logout'");
                getUI().get().getSession().close();
            }));
        }
        userInfo.add(new Paragraph(SecurityContextHolder.getContext().getAuthentication().getName()));
        userInfo.add(new Image("images/user.svg", "Avatar"));
        layout.add(userInfo);
        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "Bsc Thesis project logo"));
        logoLayout.add(new H1("Bsc Thesis project"));
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        List<Component> links = new ArrayList<>();
        if (!SecurityUtils.isUserLoggedIn()) {
            links.add(new RouterLink("Log in", LoginView.class));
        }

        if (userHasRole(NoticeBoardView.PRIVILEGE)) {
            addBoardLinks(links);
        }

        if (userHasRole(ReservationView.PRIVILEGE)) {
            links.add(new RouterLink("Reservations", ReservationView.class));
        }

        if (userHasRole(UserManagementView.PRIVILEGE)) {
            links.add(new RouterLink("User management", UserManagementView.class));
        }

        if (userHasRole(FloorManagementView.PRIVILEGE)) {
            links.add(new RouterLink("Floor management", FloorManagementView.class));
        }

        if (userHasRole(PropertyManagementView.PRIVILEGE)) {
            links.add(new RouterLink("Property management", PropertyManagementView.class));
        }

        return links.stream().map(MainView::createTab).toArray(Tab[]::new);
    }

    private void addBoardLinks(List<Component> links) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(RuntimeException::new);

        for (Floor floor : currentUser.getFloors()) {
            NoticeBoard noticeBoard = noticeBoardRepository.findByOwner(floor);
            String boardName = noticeBoard.getName();
            links.add(new RouterLink(boardName, NoticeBoardView.class, boardName));
        }
    }

    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.add(content);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        updateChrome();
    }

    private void updateChrome() {
        Optional<Tab> tab = getTabWithCurrentRoute();
        tab.ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabWithCurrentRoute() {
        String currentRoute = RouteConfiguration.forSessionScope().getUrl(getContent().getClass());
        return menu.getChildren().filter(tab -> hasLink(tab, currentRoute)).findFirst().map(Tab.class::cast);
    }

    private boolean hasLink(Component tab, String currentRoute) {
        return tab.getChildren().filter(RouterLink.class::isInstance).map(RouterLink.class::cast)
                .map(RouterLink::getHref).anyMatch(currentRoute::equals);
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}
