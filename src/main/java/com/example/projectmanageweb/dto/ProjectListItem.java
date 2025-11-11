package com.example.projectmanageweb.dto;

import java.time.LocalDate;

public class ProjectListItem {
	
	 	private Integer projectId;
	    private String projectName;
	    private String status;
	    private LocalDate startDate;
	    private LocalDate endDate;

	    private String pmName;

	    // fallback nếu muốn dùng leader / creator
	    private String leaderName;

	    private String description;

	    private String typeName;
	    private String typeCode;

	    private Integer memberCount;

		public Integer getProjectId() {
			return projectId;
		}

		public void setProjectId(Integer projectId) {
			this.projectId = projectId;
		}

		public String getProjectName() {
			return projectName;
		}

		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public LocalDate getStartDate() {
			return startDate;
		}

		public void setStartDate(LocalDate startDate) {
			this.startDate = startDate;
		}

		public LocalDate getEndDate() {
			return endDate;
		}

		public void setEndDate(LocalDate endDate) {
			this.endDate = endDate;
		}

		public String getPmName() {
			return pmName;
		}

		public void setPmName(String pmName) {
			this.pmName = pmName;
		}

		public String getLeaderName() {
			return leaderName;
		}

		public void setLeaderName(String leaderName) {
			this.leaderName = leaderName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public String getTypeCode() {
			return typeCode;
		}

		public void setTypeCode(String typeCode) {
			this.typeCode = typeCode;
		}

		public Integer getMemberCount() {
			return memberCount;
		}

		public void setMemberCount(Integer memberCount) {
			this.memberCount = memberCount;
		}
	    
	

}
