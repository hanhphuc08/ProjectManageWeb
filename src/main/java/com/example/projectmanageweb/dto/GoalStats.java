package com.example.projectmanageweb.dto;

public class GoalStats {
    private int activeGoals;
    private int completedGoals;
    private String nextGoalTitle;
    private String nextGoalDate; // dd/MM/yyyy

    public int getActiveGoals() { return activeGoals; }
    public void setActiveGoals(int activeGoals) { this.activeGoals = activeGoals; }

    public int getCompletedGoals() { return completedGoals; }
    public void setCompletedGoals(int completedGoals) { this.completedGoals = completedGoals; }

    public String getNextGoalTitle() { return nextGoalTitle; }
    public void setNextGoalTitle(String nextGoalTitle) { this.nextGoalTitle = nextGoalTitle; }

    public String getNextGoalDate() { return nextGoalDate; }
    public void setNextGoalDate(String nextGoalDate) { this.nextGoalDate = nextGoalDate; }
}
