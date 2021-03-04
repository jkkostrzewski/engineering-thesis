package com.example.thesis;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

@PWA(name = "Bsc Thesis project", shortName = "bsc-thesis-project")
@Theme(value = Material.class, variant = Material.DARK)
@Push
public class AppShell implements AppShellConfigurator {
}
