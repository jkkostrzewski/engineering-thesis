package com.example.thesis.views;

import com.example.thesis.backend.security.SecurityConfiguration;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = RootView.ROUTE, layout = MainView.class)
@PageTitle("Main page")
public class RootView extends VerticalLayout {
    public static final String ROUTE = "";

    public RootView() {
        UI.getCurrent().navigate(SecurityConfiguration.LOGIN_SUCCESS_URL);
    }
}
