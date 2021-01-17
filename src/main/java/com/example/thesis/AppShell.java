package com.example.thesis;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;

/**
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@PWA(name = "Bsc Thesis project", shortName = "bsc-thesis-project")
@Push
public class AppShell implements AppShellConfigurator {
}
