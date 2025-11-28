package com.example.projectmanageweb.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.projectmanageweb.email.dto.DeadlineReminderContext;
import com.example.projectmanageweb.email.dto.ProjectMemberAddedContext;
import com.example.projectmanageweb.email.dto.TaskMemberAddedContext;
import com.example.projectmanageweb.email.impl.DeadlineReminderEmail;
import com.example.projectmanageweb.email.impl.ProjectMemberAddedEmail;
import com.example.projectmanageweb.email.impl.TaskMemberAddedEmail;

@Service
public class NotificationEmailService {
	
	private final ProjectMemberAddedEmail projectMemberAddedEmail;
    private final TaskMemberAddedEmail taskMemberAddedEmail;
    private final DeadlineReminderEmail deadlineReminderEmail;

    public NotificationEmailService(ProjectMemberAddedEmail projectMemberAddedEmail,
                                    TaskMemberAddedEmail taskMemberAddedEmail,
                                    DeadlineReminderEmail deadlineReminderEmail) {
        this.projectMemberAddedEmail = projectMemberAddedEmail;
        this.taskMemberAddedEmail = taskMemberAddedEmail;
        this.deadlineReminderEmail = deadlineReminderEmail;
    }

    public void sendProjectMemberAdded(String toEmail, String memberName,
                                       String projectName, String invitedBy) {
        var ctx = new ProjectMemberAddedContext(toEmail, memberName, projectName, invitedBy);
        projectMemberAddedEmail.send(ctx);
    }

    public void sendTaskMemberAdded(String toEmail, String memberName,
                                    String projectName, String taskTitle,
                                    String assignedBy) {
        var ctx = new TaskMemberAddedContext(toEmail, memberName, projectName, taskTitle, assignedBy);
        taskMemberAddedEmail.send(ctx);
    }

    public void sendDeadlineReminder(String toEmail, String memberName,
                                     String projectName, String taskTitle,
                                     LocalDate dueDate) {
        var ctx = new DeadlineReminderContext(toEmail, memberName, projectName, taskTitle, dueDate);
        deadlineReminderEmail.send(ctx);
    }

}
