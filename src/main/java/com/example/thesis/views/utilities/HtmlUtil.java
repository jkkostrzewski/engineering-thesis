package com.example.thesis.views.utilities;

import com.vaadin.flow.dom.Element;

import java.util.Objects;

public class HtmlUtil {

    public static void setInnerHtml(Element element, String htmlText) {
        String currentValue = element.getProperty("innerHTML");

        if (Objects.isNull(currentValue) || !currentValue.equals(htmlText)) {
            element.setProperty("innerHTML", htmlText);
        }
    }
}
