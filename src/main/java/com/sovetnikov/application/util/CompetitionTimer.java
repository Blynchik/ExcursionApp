package com.sovetnikov.application.util;

import java.time.LocalDate;

public class CompetitionTimer {

    private static final LocalDate START_COMPETITION = LocalDate.of(2022, 12, 31);
    private static final int COMPETITION_PERIOD = 1;

    public static boolean timeToCompare() {
        return (LocalDate.now().toEpochDay() - START_COMPETITION.toEpochDay()) % COMPETITION_PERIOD == 0;
    }
}
