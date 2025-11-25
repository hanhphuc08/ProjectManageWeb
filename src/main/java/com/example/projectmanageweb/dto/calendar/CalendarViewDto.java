package com.example.projectmanageweb.dto.calendar;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CalendarViewDto {
	private List<CalendarDayDto> days = new ArrayList<>(); // size 42
    private int year;
    private int month;
	public List<CalendarDayDto> getDays() {
		return days;
	}
	public void setDays(List<CalendarDayDto> days) {
		this.days = days;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
    
    

}
