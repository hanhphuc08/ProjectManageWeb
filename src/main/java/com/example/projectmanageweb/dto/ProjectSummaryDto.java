package com.example.projectmanageweb.dto;

import java.util.List;

public class ProjectSummaryDto {

	private MetricDto metrics;
    private List<CountDto> statusCounts;
    private List<CountDto> priorityCounts;
    private List<CountDto> typeCounts;
    private List<RecentActivityDto> recentActivities;
	public MetricDto getMetrics() {
		return metrics;
	}
	public void setMetrics(MetricDto metrics) {
		this.metrics = metrics;
	}
	public List<CountDto> getStatusCounts() {
		return statusCounts;
	}
	public void setStatusCounts(List<CountDto> statusCounts) {
		this.statusCounts = statusCounts;
	}
	public List<CountDto> getPriorityCounts() {
		return priorityCounts;
	}
	public void setPriorityCounts(List<CountDto> priorityCounts) {
		this.priorityCounts = priorityCounts;
	}
	public List<CountDto> getTypeCounts() {
		return typeCounts;
	}
	public void setTypeCounts(List<CountDto> typeCounts) {
		this.typeCounts = typeCounts;
	}
	public List<RecentActivityDto> getRecentActivities() {
		return recentActivities;
	}
	public void setRecentActivities(List<RecentActivityDto> recentActivities) {
		this.recentActivities = recentActivities;
	}
    
}
