package com.example.thesis.backend.emails;

import com.example.thesis.backend.security.auth.Token;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.net.URI;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private final TemplateEngine templateEngine;

    @Autowired
    private final JavaMailSenderImpl javaMailSender;

    private MimeMessageHelper helper;

    public EmailService(TemplateEngine templateEngine, JavaMailSenderImpl javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    private void sendEmail(String to, String title, String content) {
        MimeMessage mail = javaMailSender.createMimeMessage();

        log.info("Creating email object");

        Try.of(() -> {
            helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setFrom("shopfinderappemailsender@gmail.com");
            helper.setSubject(title);
            helper.setText(content, true);
            return helper;
        });
        javaMailSender.send(mail);
        Notification.show("Email has been sent");
    }

    public void sendRegistrationEmail(String email, Token token) {
        String title = "Dorm administrator has invited you to dorm app!";
        String description = "Register at: \n" + getBasePath() + "/token-registration/" + token.getUuid();

        Context context = new Context();
        context.setVariable("title", title);
        context.setVariable("description", description);
        String body = templateEngine.process("emailNotificationTemplate", context);

        //TODO zapewnić wykonanie
        sendEmail(email, title, body);
    }

    private String getBasePath() {
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();
        return uri.toString();
    }
}
