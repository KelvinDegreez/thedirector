package org.kelvin.webapp.schedule;

import org.kelvin.webapp.director.DataValues;
import org.kelvin.webapp.tools.CommonUtils;
import org.kelvin.webapp.tools.DataGenerator;
import java.util.*;

public class TestScheduleDatabase implements ScheduleDatabase {

    private static Map<LifeTask.Type, Double> dailyAllotmentMap = new HashMap<>();
    private static Map<LifeTask.Type, Double> weeklyAllotmentMap = new HashMap<>();

    static {
        dailyAllotmentMap.put(LifeTask.Type.SLEEP, 8.0);
        dailyAllotmentMap.put(LifeTask.Type.WORK, 8.5);
        dailyAllotmentMap.put(LifeTask.Type.REST, 1.5);
        dailyAllotmentMap.put(LifeTask.Type.SPIRIT, 1.0);
        dailyAllotmentMap.put(LifeTask.Type.LIFE_GOAL, 1.5);
        dailyAllotmentMap.put(LifeTask.Type.RELATIONSHIP, 1.5);
        dailyAllotmentMap.put(LifeTask.Type.TASKS, 2.0);
        dailyAllotmentMap.put(LifeTask.Type.UNKNOWN, 0.0);

        weeklyAllotmentMap.put(LifeTask.Type.SLEEP, 56.0);
        weeklyAllotmentMap.put(LifeTask.Type.WORK, 42.5);
        weeklyAllotmentMap.put(LifeTask.Type.REST, 9.0);
        weeklyAllotmentMap.put(LifeTask.Type.SPIRIT, 7.0);
        weeklyAllotmentMap.put(LifeTask.Type.LIFE_GOAL, 6.0);
        weeklyAllotmentMap.put(LifeTask.Type.RELATIONSHIP, 7.5);
        weeklyAllotmentMap.put(LifeTask.Type.TASKS, 14.0);
        weeklyAllotmentMap.put(LifeTask.Type.UNKNOWN, 0.0);
    }

    public List<Week> weeks = DataGenerator.createWeeksForYear(2018);

    private Map<Date, DayQuota> dayQuotaMap = new HashMap<>();

    public TestScheduleDatabase(){

    }

    @Override
    public Date getCurrentDay() {
        return CommonUtils.getToday();
    }

    @Override
    public Week getCurrentWeek() {
        return getWeekByDate(getCurrentDay());
    }

    @Override
    public Week getWeekByDate(Date date) {
        for(Week week : weeks){
            if(!date.before(week.getStartDate()) && !date.after(week.getEndDate())){
                return week;
            }
        }
        return weeks.get(0);
    }

    @Override
    public DayQuota getCurrentDayQuota() {
        return dayQuotaMap.get(getCurrentDay());
    }

    @Override
    public List<DayQuota> getCurrentWeekDayQuotas() {
        List<DayQuota> list = new ArrayList<>();
        for(Date date : getCurrentWeek().getDays()){
            list.add(dayQuotaMap.get(date));
        }
        return list;
    }

    @Override
    public DayQuota getDayQuotaForDate(Date date) {
        return dayQuotaMap.get(date);
    }

    @Override
    public List<DayQuota> getDayQuotasForWeek(Week week) {
        List<DayQuota> list = new ArrayList<>();
        for(Date date : week.getDays()){
            list.add(dayQuotaMap.get(date));
        }
        return list;
    }

    @Override
    public Double getMaxAllotmentForDayByType(LifeTask.Type type) {
        return dailyAllotmentMap.get(type);
    }

    @Override
    public Double getMaxAllotmentForWeekByType(LifeTask.Type type) {
        return weeklyAllotmentMap.get(type);
    }

    @Override
    public Double getRemainingTimeForDay(Date date) {
        return DataValues.MAX_DAY_TOTAL - CommonUtils.getTotalTimeForLifeTasks(getDayQuotaForDate(date).getLifeTasks());
    }

    @Override
    public Double getRemainingTimeForDayByLifeTaskType(Date date, LifeTask.Type type) {
        return dailyAllotmentMap.get(type) - CommonUtils.getTotalTimeForLifeTasks_FilterByType(getDayQuotaForDate(date).getLifeTasks(), type);
    }

    @Override
    public Double getRemainingTimeForWeek(Week week) {
        List<LifeTask> lifeTasksForWeek = new ArrayList<>();
        for(DayQuota item : getDayQuotasForWeek(week)){
            lifeTasksForWeek.addAll(item.getLifeTasks());
        }
        return DataValues.MAX_WEEK_TOTAL - CommonUtils.getTotalTimeForLifeTasks(lifeTasksForWeek);
    }

    @Override
    public Double getRemainingTimeForWeekByLifeTaskType(Week week, LifeTask.Type type) {
        List<LifeTask> lifeTasksForWeek = new ArrayList<>();
        for(DayQuota item : getDayQuotasForWeek(week)){
            lifeTasksForWeek.addAll(item.getLifeTasks());
        }
        return DataValues.MAX_WEEK_TOTAL - CommonUtils.getTotalTimeForLifeTasks_FilterByType(lifeTasksForWeek, type);
    }

    //=========== Fake Data Setup

    public void intWithDefaultData(){
        initDatabase(createDefaultTestLifeTasks());
    }

    public void initDatabase(Map<DataValues.DayOfWeek, List<LifeTask>> weeklyTasks){
        for (Week week : weeks) {
            for (Date day : week.getDays()) {
                dayQuotaMap.put(day, new DayQuota(weeklyTasks.get(CommonUtils.getDayOfWeekForDate(day))));
            }
        }
    }

    public void initWeek(Week week, Map<DataValues.DayOfWeek, List<LifeTask>> weeklyTasks){
        for(Date day : week.getDays()){
            dayQuotaMap.put(day, new DayQuota(weeklyTasks.get(CommonUtils.getDayOfWeekForDate(day))));
        }
    }

    public static Map<DataValues.DayOfWeek, List<LifeTask>> createDefaultTestLifeTasks(){
        Map<DataValues.DayOfWeek, List<LifeTask>> map = new HashMap<>();
        List<LifeTask> workDayTasks = new ArrayList<>();
        workDayTasks.add(new LifeTask("Daily Sleep", LifeTask.Type.SLEEP, 6.0, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
        workDayTasks.add(new LifeTask("Daily Work", LifeTask.Type.WORK, 6.0, DataValues.Priority.NORMAL, DataValues.Urgency.EXTREME));
        workDayTasks.add(new LifeTask("Daily Rest", LifeTask.Type.REST, 0.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
        workDayTasks.add(new LifeTask("Quiet Time", LifeTask.Type.SPIRIT, 0.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));

        List<LifeTask> weekEndTasks = new ArrayList<>();
        weekEndTasks.add(new LifeTask("Daily Sleep", LifeTask.Type.SLEEP, 7.0, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
        weekEndTasks.add(new LifeTask("Weekend Relationship", LifeTask.Type.RELATIONSHIP, 6.0, DataValues.Priority.NORMAL, DataValues.Urgency.EXTREME));
        weekEndTasks.add(new LifeTask("Weekend Rest", LifeTask.Type.REST, 0.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
        weekEndTasks.add(new LifeTask("Quiet Time", LifeTask.Type.SPIRIT, 0.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));

        for(DataValues.DayOfWeek day : DataValues.DayOfWeek.values()) {
            switch (day) {
                case MONDAY:
                case TUESDAY:
                case WEDNESDAY:
                case THURSDAY:
                case FRIDAY:
                    map.put(day, workDayTasks);
                    break;
                case SATURDAY:
                case SUNDAY:
                    map.put(day, weekEndTasks);
                    break;
            }
        }
        return map;
    }

}
