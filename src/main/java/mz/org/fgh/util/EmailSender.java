package mz.org.fgh.util;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.user.User;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class EmailSender {

    private final EmailService emailService;

    public EmailSender(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendEmailToUser(User user, String password, String serverUrl) throws Exception {
        String htmlTemplate = emailService.loadHtmlTemplate("emailTemplate");

        // Populate variables
        Map<String, String> variables = new HashMap<>();
        variables.put("serverUrl", serverUrl);
        variables.put("name", user.getEmployee().getFullName());
        variables.put("user", user.getUsername());
        variables.put("password", password);

        String populatedHtml = emailService.populateTemplateVariables(htmlTemplate, variables);

        // Send email
        emailService.sendEmail(user.getEmployee().getEmail(), "Registo no aplicativo da Mentoria", populatedHtml);
    }

    // Password-Reset
    public void sendPasswordRecoveryEmail(
            Employee employee,
            String token,
            String platform,
            String serverUrl,
            String expiration
    ) throws Exception {

        String htmlTemplate = emailService.loadHtmlTemplate("emailPasswordResetTemplate");

        // Define visibilidade
        String webDisplay = platform.equalsIgnoreCase("WEB") ? "" : "display:none";
        String mobileDisplay = platform.equalsIgnoreCase("MOBILE") ? "" : "display:none";

        // Variáveis
        Map<String, String> variables = new HashMap<>();
        variables.put("serverUrl", serverUrl);
        variables.put("name", employee.getFullName());
        variables.put("token", token);
        variables.put("expiration", expiration);
        variables.put("webDisplay", webDisplay);
        variables.put("mobileDisplay", mobileDisplay);

        String populatedHtml = emailService.populateTemplateVariables(htmlTemplate, variables);

        emailService.sendEmail(
                employee.getEmail(),
                "Recuperação de Senha - Mentoria",
                populatedHtml
        );
    }

}
