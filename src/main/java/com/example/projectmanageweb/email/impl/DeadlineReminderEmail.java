package com.example.projectmanageweb.email.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import com.example.projectmanageweb.email.AbstractEmailTemplate;
import com.example.projectmanageweb.email.dto.DeadlineReminderContext;

@Component
public class DeadlineReminderEmail extends AbstractEmailTemplate<DeadlineReminderContext> {
	public DeadlineReminderEmail(JavaMailSender mailSender, TemplateEngine templateEngine) {
		super(mailSender, templateEngine);
	}

	@Override
	protected String getTo(DeadlineReminderContext ctx) {
		return ctx.getToEmail();
	}

	@Override
	protected String buildSubject(DeadlineReminderContext ctx) {
		return "[APT Project] Nhắc deadline: " + ctx.getTaskTitle();
	}

	@Override
	protected String getTemplateName() {
		return "mail/deadline-reminder";
	}

	@Override
	protected Map<String, Object> buildModel(DeadlineReminderContext ctx) {
		Map<String, Object> model = new HashMap<>();
		model.put("memberName", ctx.getMemberName());
		model.put("projectName", ctx.getProjectName());
		model.put("taskTitle", ctx.getTaskTitle());
		model.put("dueDate", ctx.getDueDate());
		return model;
	}

}
