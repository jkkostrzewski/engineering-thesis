package com.example.thesis.views.notice.board;

import com.example.thesis.backend.ServiceResponse;
import com.example.thesis.backend.notice.Notice;
import com.example.thesis.backend.notice.NoticeBoardRepository;
import com.example.thesis.backend.notice.NoticeService;
import com.example.thesis.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
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


@Route(value = AddNoticeView.ROUTE, layout = MainView.class)
@PageTitle("Add notice")
@CssImport("./styles/views/notice/board/add-notice.css")
@Secured(AddNoticeView.PRIVILEGE)
public class AddNoticeView extends VerticalLayout {

    public static final String PRIVILEGE = "ADD_NOTICE_VIEW_PRIVILEGE";
    public static final String ROUTE = "add-notice";

    @Autowired
    private final NoticeBoardRepository noticeBoardRepository;

    private byte[] imageBytes;
    private TextField title;
    private TextArea body;
    private Button confirm;

    @Autowired
    private NoticeService noticeService;

    public AddNoticeView(NoticeBoardRepository noticeBoardRepository) {
        this.noticeBoardRepository = noticeBoardRepository;
        setId("add-notice");

        title = new TextField("Title");
        title.setId("title");
        title.setSizeUndefined();
        add(title);

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        Div output = new Div();

        upload.addSucceededListener(event -> {  //max 10 MB
            Component component = createComponent(event.getMIMEType(),
                    event.getFileName(), buffer.getInputStream());
            showOutput(event.getFileName(), component, output);
        });

        add(upload, output);

        body = new TextArea("Body");
        body.setId("body");
        body.setSizeUndefined();

        confirm = new Button("Confirm");
        confirm.addClickListener(e -> {
            ServiceResponse<Notice> response = noticeService.saveNotice(Notice.builder()
                                                          .creationDate(Instant.now())
                                                          .title(title.getValue())
                                                          .body(body.getValue())
                                                          .image(imageBytes).build());

//            NoticeBoard noticeBoard = noticeBoardRepository.      //TODO Add to specific notice board

            if (response.getStatus() == HttpStatus.OK) {
                Notification.show("Upload has been successful");
            } else {
                Notification.show("Something went wrong with the upload. Try again.");
            }

        });

        add(body, confirm);

        //TODO add logic to fields not being complete
        this.setAlignItems(Alignment.CENTER);
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
