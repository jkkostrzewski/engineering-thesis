package com.example.thesis.views.exception;

import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;

@PageTitle("Internal Server Error")
@Route(value = InternalServerErrorView.ROUTE, layout = MainView.class)
@Slf4j
public class InternalServerErrorView extends VerticalLayout implements HasErrorParameter<Exception> {

    public static final String ROUTE = "/internal-server-error";

    public InternalServerErrorView() {
        setAlignItems(Alignment.CENTER);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<Exception> parameter) {
        log.error(parameter.getException().getMessage());
        add(new H1("Internal server error"));
        return HttpServletResponse.SC_NOT_FOUND;
    }
}