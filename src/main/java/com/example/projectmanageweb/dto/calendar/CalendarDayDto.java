package com.example.projectmanageweb.dto.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CalendarDayDto {
	private LocalDate date;              // ngày đầy đủ yyyy-MM-dd
    private int dayOfMonth;              // số ngày: 1..31 để hiển thị
    private boolean inCurrentMonth;      // có thuộc tháng đang xem không
    private boolean today;               // có phải ngày hiện tại không
    private List<CalendarTaskDto> tasks = new ArrayList<>();
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public int getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	public boolean isInCurrentMonth() {
		return inCurrentMonth;
	}
	public void setInCurrentMonth(boolean inCurrentMonth) {
		this.inCurrentMonth = inCurrentMonth;
	}
	public boolean isToday() {
		return today;
	}
	public void setToday(boolean today) {
		this.today = today;
	}
	public List<CalendarTaskDto> getTasks() {
		return tasks;
	}
	public void setTasks(List<CalendarTaskDto> tasks) {
		this.tasks = tasks;
	}
    
    
    

}
