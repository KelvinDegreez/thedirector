package org.kelvin.webapp.schedule;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Week {

    private int weekNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<LocalDate> days = new ArrayList<>();

    public Week(LocalDate dateInWeek) {
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
        this.startDate = dateInWeek.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
        this.endDate = dateInWeek.with(TemporalAdjusters.nextOrSame(lastDayOfWeek));
        for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
            days.add(d);
        }
        weekNumber = startDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    }

    public int getWeekNumber(){
        return weekNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<LocalDate> getDays() {
        return days;
    }

}
