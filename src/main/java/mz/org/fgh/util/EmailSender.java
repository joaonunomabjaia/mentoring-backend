package mz.org.fgh.util;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.user.User;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class EmailSender {

    private final EmailService emailService;

    public EmailSender(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendEmailToUser(User user, String password) throws Exception {
        String htmlTemplate = emailService.loadHtmlTemplate("emailTemplate");

        // Populate variables
        Map<String, String> variables = new HashMap<>();
        variables.put("name", user.getEmployee().getFullName());
        variables.put("user", user.getUsername());
        variables.put("password", password);

        String populatedHtml = emailService.populateTemplateVariables(htmlTemplate, variables);

        // Send email
        emailService.sendEmail(user.getEmployee().getEmail(), "Registo no Sistema Mentoria", populatedHtml);
    }
}
