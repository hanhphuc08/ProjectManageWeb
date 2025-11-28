package com.example.projectmanageweb.email.dto;

public class ProjectMemberAddedContext {
	
	private final String toEmail;
    private final String memberName;
    private final String projectName;
    private final String invitedByName;

    public ProjectMemberAddedContext(String toEmail, String memberName,
                                     String projectName, String invitedByName) {
        this.toEmail = toEmail;
        this.memberName = memberName;
        this.projectName = projectName;
        this.invitedByName = invitedByName;
    }

    public String getToEmail() { return toEmail; }
    public String getMemberName() { return memberName; }
    public String getProjectName() { return projectName; }
    public String getInvitedByName() { return invitedByName; }

}
