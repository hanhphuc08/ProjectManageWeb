package com.example.projectmanageweb.dto;

import java.util.List;

public record AiSuggestResult(
        List<SuggestedTask> newTasks,
        int remainingDaysSequential,
        int remainingDaysParallel
) {}
