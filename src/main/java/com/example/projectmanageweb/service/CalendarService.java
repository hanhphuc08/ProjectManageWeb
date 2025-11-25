package com.example.projectmanageweb.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.projectmanageweb.dto.calendar.CalendarDayDto;
import com.example.projectmanageweb.dto.calendar.CalendarTaskDto;
import com.example.projectmanageweb.dto.calendar.CalendarViewDto;
import com.example.projectmanageweb.repository.CalendarRepository;

@Service
public class CalendarService {

	 private final CalendarRepository repo;

	    public CalendarService(CalendarRepository repo) {
	        this.repo = repo;
	    }

	    public CalendarViewDto build(int projectId, String monthParam) {

	        YearMonth ym;
	        if (monthParam == null) {
	            LocalDate now = LocalDate.now();
	            ym = YearMonth.of(now.getYear(), now.getMonthValue());
	        } else {
	            String[] parts = monthParam.split("-");
	            ym = YearMonth.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
	        }

	        List<CalendarTaskDto> tasks = repo.getTasksByMonth(projectId, ym);

	        CalendarViewDto view = new CalendarViewDto();
	        view.setYear(ym.getYear());
	        view.setMonth(ym.getMonthValue());

	        LocalDate firstDay = ym.atDay(1);
	        DayOfWeek firstDow = firstDay.getDayOfWeek();

	        int shift = firstDow.getValue() == 7 ? 6 : firstDow.getValue() - 1;
	        LocalDate startGrid = firstDay.minusDays(shift);

	        for (int i = 0; i < 42; i++) {
	            LocalDate date = startGrid.plusDays(i);

	            CalendarDayDto day = new CalendarDayDto();
	            day.setDate(date);
	            
	            day.setDayOfMonth(date.getDayOfMonth());
	            day.setInCurrentMonth(date.getMonthValue() == ym.getMonthValue());
	            day.setToday(date.equals(LocalDate.now()));

	            // gán task đúng ngày
	            for (CalendarTaskDto t : tasks) {
	                if (t.getDueDate() != null && t.getDueDate().equals(date)) {
	                    day.getTasks().add(t);
	                }
	            }

	            view.getDays().add(day);
	        }

	        return view;
	    }

	    public String getMonthLabel(int year, int month) {
	        return Month.of(month).name().substring(0,1) 
	               + Month.of(month).name().substring(1).toLowerCase()
	               + " " + year;
	    }
}
