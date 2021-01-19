package com.example.thesis.views.exception;

import com.example.thesis.backend.security.SecurityUtils;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;

@PageTitle("Not Found")
@Route(value = NotFoundView.ROUTE, layout = MainView.class)
@Slf4j
public class NotFoundView extends VerticalLayout implements HasErrorParameter<NotFoundException> {

    public static final String ROUTE = "/not-found";

    public NotFoundView() {
        setAlignItems(Alignment.CENTER);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        log.error(SecurityUtils.getLoggedUserUsername() + " - " + parameter.getException().getMessage());
        add(new H1("Could not navigate to '" + event.getLocation().getPath() + "'"));
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
