package com.example.thesis.views.notice.board;

import com.example.thesis.backend.ServiceResponse;
import com.example.thesis.backend.notice.Notice;
import com.example.thesis.backend.notice.NoticeBoard;
import com.example.thesis.backend.notice.NoticeService;
import com.example.thesis.backend.security.SecurityUtils;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Route(value = EditNoticeView.ROUTE, layout = MainView.class)
@PageTitle("Add notice")
@CssImport("./styles/views/notice/board/add-notice.css")
@Secured(EditNoticeView.PRIVILEGE)
@Slf4j
public class EditNoticeView extends VerticalLayout implements HasUrlParameter<String>{

    public static final String PRIVILEGE = "EDIT_NOTICE_VIEW_PRIVILEGE";
    public static final String ROUTE = "edit-notice";

    private byte[] imageBytes;
    private TextField title;
    private RichTextEditor body;
    private Button confirm;

    @Autowired
    private NoticeService noticeService;

    private NoticeBoard noticeBoard;

    private Notice notice;
    private final Upload upload;

    public EditNoticeView(NoticeService noticeService) {
        this.noticeService = noticeService;
        setId("add-notice");

        title = new TextField("Title");
        title.setId("title");
        title.setSizeUndefined();
        add(title);

        MemoryBuffer buffer = new MemoryBuffer();
        upload = new Upload(buffer);
        Div output = new Div();

        upload.addSucceededListener(event -> {  //max 10 MB
            Component component = createComponent(event.getMIMEType(),
                    event.getFileName(), buffer.getInputStream());
            showOutput(event.getFileName(), component, output);
        });

        add(upload, output);

        body = new RichTextEditor();
        body.setId("body");
        body.setSizeUndefined();

        confirm = new Button("Confirm");
        confirm.addClickListener(e -> {
            if (body.isEmpty()) {
                Notification.show("You can't add a notice without content!");
                return;
            }

            ServiceResponse<Notice> response;

            if (Objects.nonNull(notice)) {
                notice.setTitle(title.getValue());
                notice.setBody(body.getHtmlValue());
                if (Objects.nonNull(imageBytes)) {
                    notice.setImage(imageBytes);
                }

                response = noticeService.saveNotice(notice);
            } else {
                response = noticeService.saveNotice(Notice.builder()
                                .creationDate(Instant.now())
                                .title(title.getValue())
                                .body(body.getHtmlValue())
                                .active(true)
                                .createdByUsername(SecurityUtils.getLoggedUserUsername())
                                .image(imageBytes).build(),
                        noticeBoard);
            }


            if (Objects.nonNull(response) && response.getStatus() == HttpStatus.OK) {
                Notification.show("Upload has been successful");
                UI.getCurrent().navigate(NoticeBoardView.class, noticeBoard.getName());
                log.info("User: " + SecurityUtils.getLoggedUserUsername() + " has created/edited notice: " + response.getContent().getId());
            } else {
                Notification.show("Something went wrong with the upload. Try again.");
            }

        });

        add(body, confirm);

        this.setAlignItems(Alignment.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        QueryParameters queryParameters = beforeEvent.getLocation().getQueryParameters();
        Map<String, List<String>> parameters = queryParameters.getParameters();

        String boardName = parameters.get("boardName").get(0);
        noticeBoard = noticeService.findByName(boardName.replace("%20", " "));

        if (Objects.nonNull(parameters.get("noticeId"))) {
            long noticeId = Long.parseLong(parameters.get("noticeId").get(0));
            notice = noticeService.findById(noticeId).getContent();

            title.setValue(notice.getTitle());
            body.asHtml().setValue(notice.getBody());
            //TODO add image?
        }
    }

    private Component createComponent(String mimeType, String fileName,
                                      InputStream stream) {
        if (mimeType.startsWith("text")) {
            return createTextComponent(stream);
        } else if (mimeType.startsWith("image")) {
            Image image = new Image();
            try {

                imageBytes = IOUtils.toByteArray(stream);
                image.getElement().setAttribute("src", new StreamResource(
                        fileName, () -> new ByteArrayInputStream(imageBytes)));
                try (ImageInputStream in = ImageIO.createImageInputStream(
                        new ByteArrayInputStream(imageBytes))) {
                    final Iterator<ImageReader> readers = ImageIO
                            .getImageReaders(in);
                    if (readers.hasNext()) {
                        ImageReader reader = readers.next();
                        try {
                            reader.setInput(in);
                            image.setWidth(reader.getWidth(0) + "px");
                            image.setHeight(reader.getHeight(0) + "px");
                        } finally {
                            reader.dispose();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return image;
        }
        Div content = new Div();
        String text = String.format("Mime type: '%s'\nSHA-256 hash: '%s'",
                mimeType, MessageDigestUtil.sha256(stream.toString()));
        content.setText(text);
        return content;

    }

    private Component createTextComponent(InputStream stream) {
        String text;
        try {
            text = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            text = "exception reading stream";
        }
        return new Text(text);
    }

    private void showOutput(String text, Component content,
                            HasComponents outputContainer) {
        outputContainer.removeAll();
        HtmlComponent uploadedImage = new HtmlComponent(Tag.P);
        uploadedImage.setId("uploaded-image");
        uploadedImage.getElement().setText(text);
        outputContainer.add(uploadedImage);
        outputContainer.add(content);
    }
}
