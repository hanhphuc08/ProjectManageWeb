package com.example.projectmanageweb.dto;

public class MetricDto {
	private int total;
    private int completed;
    private int createdToday;
    private int updatedToday;
    private int dueSoon;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getCompleted() {
		return completed;
	}
	public void setCompleted(int completed) {
		this.completed = completed;
	}
	public int getCreatedToday() {
		return createdToday;
	}
	public void setCreatedToday(int createdToday) {
		this.createdToday = createdToday;
	}
	public int getUpdatedToday() {
		return updatedToday;
	}
	public void setUpdatedToday(int updatedToday) {
		this.updatedToday = updatedToday;
	}
	public int getDueSoon() {
		return dueSoon;
	}
	public void setDueSoon(int dueSoon) {
		this.dueSoon = dueSoon;
	}
    

}
