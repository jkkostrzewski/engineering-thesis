package com.example.thesis.views.exception;

import com.example.thesis.backend.security.SecurityUtils;
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
    private final String LOG_DELIMITER = "------------------------------------------------------------------";

    public InternalServerErrorView() {
        setAlignItems(Alignment.CENTER);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<Exception> parameter) {
        Exception exception = parameter.getException();
        logError(exception);

        add(new H1("Internal server error"));
        return HttpServletResponse.SC_NOT_FOUND;
    }

    private void logError(Exception exception) {
        log.error(LOG_DELIMITER);
        log.error(SecurityUtils.getLoggedUserUsername() + " - " + exception.getClass().getName() + " - " + exception.getMessage());
        printStackTrace(exception);
        log.error(LOG_DELIMITER);
    }

    private void printStackTrace(Exception exception) {
        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            log.error(stackTraceElement.toString());
        }
    }
}