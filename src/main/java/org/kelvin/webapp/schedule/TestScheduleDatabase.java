package org.kelvin.webapp.schedule;


import org.kelvin.webapp.tools.CommonUtils;
import org.kelvin.webapp.tools.DataGenerator;
import java.time.LocalDate;
import java.util.*;

public class TestScheduleDatabase implements ScheduleDatabase {

    private Map<LocalDate, DayQuota> dayQuotaMap = new HashMap<>();
    private Map<LifeTask.Type, Double> maxDailyTimeAllotmentMap = new HashMap<>();
    private Map<LifeTask.Type, Double> maxWeeklyTimeAllotmentMap = new HashMap<>();
    private Map<LifeTask.Type, Double> maxMonthlyTimeAllotmentMap = new HashMap<>();
    private Map<LifeTask.Type, Double> maxYearlyTimeAllotmentMap = new HashMap<>();

    public static TestScheduleDatabase createWithFakeData(){
        TestScheduleDatabase db = new TestScheduleDatabase();
        db.setDailyTimeAllotment(LifeTask.Type.SLEEP, 8.0);
        db.setDailyTimeAllotment(LifeTask.Type.WORK, 8.5);
        db.setDailyTimeAllotment(LifeTask.Type.REST, 1.5);
        db.setDailyTimeAllotment(LifeTask.Type.SPIRIT, 1.0);
        db.setDailyTimeAllotment(LifeTask.Type.LIFE_GOAL, 1.5);
        db.setDailyTimeAllotment(LifeTask.Type.RELATIONSHIP, 1.5);
        db.setDailyTimeAllotment(LifeTask.Type.TASKS, 2.0);
        db.setDailyTimeAllotment(LifeTask.Type.UNKNOWN, 0.0);

        db.setWeeklyTimeAllotment(LifeTask.Type.SLEEP, 56.0);
        db.setWeeklyTimeAllotment(LifeTask.Type.WORK, 42.5);
        db.setWeeklyTimeAllotment(LifeTask.Type.REST, 9.0);
        db.setWeeklyTimeAllotment(LifeTask.Type.SPIRIT, 7.0);
        db.setWeeklyTimeAllotment(LifeTask.Type.LIFE_GOAL, 6.0);
        db.setWeeklyTimeAllotment(LifeTask.Type.RELATIONSHIP, 10.5);
        db.setWeeklyTimeAllotment(LifeTask.Type.TASKS, 14.0);
        db.setWeeklyTimeAllotment(LifeTask.Type.UNKNOWN, 0.0);

        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 12, 31);
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            db.dayQuotaMap.put(date, DataGenerator.generateFakeDayQuota(date.getDayOfWeek()));
        }
        return db;
    }

    @Override
    public void clearDatabase(){
        dayQuotaMap.clear();
        maxDailyTimeAllotmentMap.clear();
        maxWeeklyTimeAllotmentMap.clear();
        maxMonthlyTimeAllotmentMap.clear();
        maxYearlyTimeAllotmentMap.clear();
    }

    @Override
    public void setDailyTimeAllotment(LifeTask.Type type, Double timeAllotment) {
        maxDailyTimeAllotmentMap.put(type, timeAllotment);
    }

    @Override
    public void setWeeklyTimeAllotment(LifeTask.Type type, Double timeAllotment) {
        maxWeeklyTimeAllotmentMap.put(type, timeAllotment);
    }

    @Override
    public void setMonthlyTimeAllotment(LifeTask.Type type, Double timeAllotment) {
        maxMonthlyTimeAllotmentMap.put(type, timeAllotment);
    }

    @Override
    public void addLifeTask(LocalDate date, LifeTask task){
        if(dayQuotaMap.get(date) != null){
            dayQuotaMap.get(date).addLifeTask(task);
        }else{
            List<LifeTask> tasks = new ArrayList<>();
            tasks.add(task);
            dayQuotaMap.put(date, new DayQuota(tasks));
        }
    }

    @Override
    public DayQuota getCurrentDayQuota() {
        LocalDate now = LocalDate.now();
        if(dayQuotaMap.containsKey(now)){
            return dayQuotaMap.get(LocalDate.now());
        }else{
            return new DayQuota(new ArrayList<>());
        }
    }

    @Override
    public List<DayQuota> getCurrentWeekDayQuotas() {
        Week week = new Week(LocalDate.now());
        List<DayQuota> dayQuotas = new ArrayList<>();
        for(LocalDate date : week.getDays()){
            DayQuota dayQuota = dayQuotaMap.get(date);
            if(dayQuota != null) dayQuotas.add(dayQuota);
        }
        return dayQuotas;
    }

    @Override
    public DayQuota getDayQuotaForDate(LocalDate date) {
        if(dayQuotaMap.containsKey(date)){
            return dayQuotaMap.get(date);
        }else{
            return new DayQuota(new ArrayList<>());
        }
    }

    @Override
    public List<DayQuota> getDayQuotasForDateRange(LocalDate startDate, LocalDate endDate) {
        List<DayQuota> dayQuotas = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DayQuota dayQuota = dayQuotaMap.get(date);
            if(dayQuota != null) dayQuotas.add(dayQuota);
        }
        return dayQuotas;
    }

    @Override
    public Double getMaxDailyAllotmentByForType(LifeTask.Type type) {
        return maxDailyTimeAllotmentMap.getOrDefault(type, 0.0);

    }

    @Override
    public Double getMaxWeeklyAllotmentForType(LifeTask.Type type) {
        if(maxWeeklyTimeAllotmentMap.containsKey(type)){
            return maxWeeklyTimeAllotmentMap.get(type);
        }else if(maxDailyTimeAllotmentMap.containsKey(type)){
            return getMaxDailyAllotmentByForType(type)*7;
        }else{
            return 0.0;
        }
    }

    @Override
    public Double getRemainingTimeForDay(LocalDate date) {
        DayQuota quota = getDayQuotaForDate(date);
        Double remainingTime = 24.0;
        if(quota != null){
            remainingTime = remainingTime - quota.getTotalTaskTime();
        }
        return remainingTime;
    }

    @Override
    public Double getRemainingTimeForDayByLifeTaskType(LocalDate date, LifeTask.Type type) {
        DayQuota quota = getDayQuotaForDate(date);
        Double maxTime = maxDailyTimeAllotmentMap.get(type);
        Double timeUsed = 0.0;
        if (quota != null) {
            timeUsed = CommonUtils.getTotalTimeForLifeTasks_FilterByType(quota.getLifeTasks(), type);
        }
        return maxTime - timeUsed;
    }

    @Override
    public Double getRemainingTimeForWeek(Week week) {
        List<DayQuota> quotas = getDayQuotasForDateRange(week.getStartDate(), week.getEndDate());
        Double remainingTime = 24.0 * week.getDays().size();
        for(DayQuota quota : quotas){
            remainingTime = remainingTime - quota.getTotalTaskTime();
        }
        return remainingTime;
    }

    @Override
    public Double getRemainingTimeForWeekByLifeTaskType(Week week, LifeTask.Type type) {
        List<DayQuota> quotas = getDayQuotasForDateRange(week.getStartDate(), week.getEndDate());
        Double maxTime = maxWeeklyTimeAllotmentMap.get(type);
        Double timeUsed = 0.0;
        for(DayQuota quota : quotas){
            timeUsed += CommonUtils.getTotalTimeForLifeTasks_FilterByType(quota.getLifeTasks(), type);
        }
        if(maxTime - timeUsed < 0.0){
            int pause=0;
            pause++;
        }
        return maxTime - timeUsed;
    }

}
