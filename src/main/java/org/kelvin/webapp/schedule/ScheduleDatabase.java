package org.kelvin.webapp.schedule;


import java.time.LocalDate;
import java.util.List;

public interface ScheduleDatabase {

    void setDailyTimeAllotment(LifeTask.Type type, Double timeAllotment);

    void setWeeklyTimeAllotment(LifeTask.Type type, Double timeAllotment);

    void setMonthlyTimeAllotment(LifeTask.Type type, Double timeAllotment);

    DayQuota getCurrentDayQuota();

    List<DayQuota> getCurrentWeekDayQuotas();

    DayQuota getDayQuotaForDate(LocalDate date);

    List<DayQuota> getDayQuotasForDateRange(LocalDate startDate, LocalDate endDate);

    Double getMaxDailyAllotmentByForType(LifeTask.Type type);

    Double getMaxWeeklyAllotmentForType(LifeTask.Type type);

    Double getRemainingTimeForDay(LocalDate date);

    Double getRemainingTimeForDayByLifeTaskType(LocalDate date, LifeTask.Type type);

    Double getRemainingTimeForWeek(Week week);

    Double getRemainingTimeForWeekByLifeTaskType(Week week, LifeTask.Type type);

}