package com.example.projectmanageweb.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProjectCreateRequest {
	
	@NotBlank @Size(max = 200)
	  private String projectName;
		
	
	private Integer typeId; 
	 

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	@Size(max = 5000)
	  private String description;

	  @DateTimeFormat(pattern = "yyyy-MM-dd")
	  private LocalDate startDate;

	  @DateTimeFormat(pattern = "yyyy-MM-dd")
	  private LocalDate endDate;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	  
	  

}
