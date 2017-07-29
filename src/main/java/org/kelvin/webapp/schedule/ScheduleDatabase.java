package org.kelvin.webapp.schedule;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ScheduleDatabase {

    Date getCurrentDay();

    Week getCurrentWeek();

    Week getWeekByDate(Date date);

    DayQuota getCurrentDayQuota();

    List<DayQuota> getCurrentWeekDayQuotas();

    DayQuota getDayQuotaForDate(Date date);

    List<DayQuota> getDayQuotasForWeek(Week week);

    Double getMaxAllotmentForDayByType(LifeTask.Type type);

    Double getMaxAllotmentForWeekByType(LifeTask.Type type);
}
