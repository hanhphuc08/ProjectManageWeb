package com.example.projectmanageweb.email.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import com.example.projectmanageweb.email.AbstractEmailTemplate;
import com.example.projectmanageweb.email.dto.ProjectMemberAddedContext;

@Component
public class ProjectMemberAddedEmail extends AbstractEmailTemplate<ProjectMemberAddedContext> {

	public ProjectMemberAddedEmail(JavaMailSender mailSender, TemplateEngine templateEngine) {
		super(mailSender, templateEngine);
	}

	@Override
	protected String getTo(ProjectMemberAddedContext ctx) {
		return ctx.getToEmail();
	}

	@Override
	protected String buildSubject(ProjectMemberAddedContext ctx) {
		return "[APT Project] Bạn vừa được thêm vào dự án " + ctx.getProjectName();
	}

	@Override
	protected String getTemplateName() {
		return "mail/project-member-added";
	}

	@Override
	protected Map<String, Object> buildModel(ProjectMemberAddedContext ctx) {
		Map<String, Object> model = new HashMap<>();
		model.put("memberName", ctx.getMemberName());
		model.put("projectName", ctx.getProjectName());
		model.put("invitedBy", ctx.getInvitedByName());
		return model;
	}

}
