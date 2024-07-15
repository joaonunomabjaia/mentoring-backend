package mz.org.fgh.util;


import io.micronaut.context.env.Environment;
import io.micronaut.core.io.ResourceLoader;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Singleton
public class EmailService {

    private final Environment environment;
    private final ResourceLoader resourceLoader;

    @Inject
    public EmailService(Environment environment, ResourceLoader resourceLoader) {
        this.environment = environment;
        this.resourceLoader = resourceLoader;
    }

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        String username = environment.getProperty("micronaut.email.mailer.gmail.username", String.class).orElse("");
        String password = environment.getProperty("micronaut.email.mailer.gmail.password", String.class).orElse("");
        String host = environment.getProperty("micronaut.email.mailer.gmail.host", String.class).orElse("");
        int port = environment.getProperty("micronaut.email.mailer.gmail.port", Integer.class).orElse(587);
        boolean tls = environment.getProperty("micronaut.email.mailer.gmail.tls", Boolean.class).orElse(true);


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", String.valueOf(tls));
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));

        // Create session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Create message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(text, "text/html");

        // Send message
        Transport.send(message);
    }

    public String loadHtmlTemplate(String templateName) throws Exception {
        try (InputStream inputStream = getClass().getResourceAsStream("/templates/" + templateName + ".html")) {
            if (inputStream == null) {
                throw new IOException("Template file not found: " + templateName);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    public String populateTemplateVariables(String htmlTemplate, Map<String, String> variables) {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            htmlTemplate = htmlTemplate.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return htmlTemplate;
    }
}
