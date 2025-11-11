package com.example.projectmanageweb.dto;

public class SuggestedTask {
	private String title;               // map -> tasks.title
    private String description;         // map -> tasks.description
    private String priority;            // "Low" | "Medium" | "High"
    private Integer estimateOptimistic; // giờ ước lượng lạc quan (giờ)
    private Integer estimateLikely;     // giờ ước lượng khả dĩ
    private Integer estimatePessimistic;// giờ ước lượng bi quan
    private String taskType;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public Integer getEstimateOptimistic() {
		return estimateOptimistic;
	}
	public void setEstimateOptimistic(Integer estimateOptimistic) {
		this.estimateOptimistic = estimateOptimistic;
	}
	public Integer getEstimateLikely() {
		return estimateLikely;
	}
	public void setEstimateLikely(Integer estimateLikely) {
		this.estimateLikely = estimateLikely;
	}
	public Integer getEstimatePessimistic() {
		return estimatePessimistic;
	}
	public void setEstimatePessimistic(Integer estimatePessimistic) {
		this.estimatePessimistic = estimatePessimistic;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
    
    

}
