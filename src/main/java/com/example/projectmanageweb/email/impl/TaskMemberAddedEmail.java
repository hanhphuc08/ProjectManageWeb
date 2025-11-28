package com.example.projectmanageweb.email.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import com.example.projectmanageweb.email.AbstractEmailTemplate;
import com.example.projectmanageweb.email.dto.TaskMemberAddedContext;

@Component
public class TaskMemberAddedEmail extends AbstractEmailTemplate<TaskMemberAddedContext> {

    public TaskMemberAddedEmail(JavaMailSender mailSender,
                                TemplateEngine templateEngine) {
        super(mailSender, templateEngine);
    }

    @Override
    protected String getTo(TaskMemberAddedContext ctx) {
        return ctx.getToEmail();
    }

    @Override
    protected String buildSubject(TaskMemberAddedContext ctx) {
        return "[APT Project] Bạn được gán vào task: " + ctx.getTaskTitle();
    }

    @Override
    protected String getTemplateName() {
        return "mail/task-member-added";
    }

    @Override
    protected Map<String, Object> buildModel(TaskMemberAddedContext ctx) {
        Map<String, Object> model = new HashMap<>();
        model.put("memberName", ctx.getMemberName());
        model.put("projectName", ctx.getProjectName());
        model.put("taskTitle", ctx.getTaskTitle());
        model.put("assignedBy", ctx.getAssignedByName());
        return model;
    }
}
