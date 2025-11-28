package com.example.projectmanageweb.email;

import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;

public abstract class AbstractEmailTemplate<C> {
	
	protected final JavaMailSender mailSender;
    protected final TemplateEngine templateEngine;

    protected AbstractEmailTemplate(JavaMailSender mailSender,
                                    TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    // 🚩 Template method
    public final void send(C context) {
        validate(context);

        String to = getTo(context);
        String subject = buildSubject(context);
        String htmlBody = buildBody(context);

        doSend(to, subject, htmlBody);
    }

    protected void validate(C context) {
    }

    protected abstract String getTo(C context);

    protected abstract String buildSubject(C context);

    protected abstract String getTemplateName();


    protected abstract Map<String, Object> buildModel(C context);


    protected String buildBody(C context) {
        Context thymeleafCtx = new Context();
        thymeleafCtx.setVariables(buildModel(context));
        return templateEngine.process(getTemplateName(), thymeleafCtx);
    }

    protected void doSend(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML

            // TODO: có thể setFrom cố định, ví dụ:
            helper.setFrom("no-reply@apt-project.com", "APT Project");

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
