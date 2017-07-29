package org.kelvin.webapp.director;


import org.kelvin.webapp.schedule.*;
import org.kelvin.webapp.tools.CommonUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TestDirector implements Director {

    private ScheduleDatabase db = new TestScheduleDatabase();

    @Override
    public void setScheduleDatabase(ScheduleDatabase db) {
        this.db = db;
    }

    @Override
    public boolean canDoToday(LifeTask newTask) {
        return isPossible(db.getCurrentDayQuota(), newTask) &&
               doesTaskFitLifeBalance(db.getCurrentDay(), newTask);
    }

    @Override
    public boolean canDoThisWeek(LifeTask newTask) {
//        Double maxPercentage = db.getMaxAllotmentForDayByType(newTask.getType());
//        Map<LifeTask.Type, Double > map = db.getCurrentWeekPercentageMap();
//        for(DayQuota quota : db.getCurrentWeekDayQuotas()) {
//
//        }
        return false;
    }

    @Override
    public boolean canDoNextWeek(LifeTask newTask) {
        return false;
    }

    @Override
    public boolean canDoThisMonth(LifeTask newTask) {
        return false;
    }

    @Override
    public boolean canDoNextMonth(LifeTask newTask) {
        return false;
    }

    private boolean isPossible(DayQuota quota, LifeTask newTask){
        return (quota.getTotalTaskTime() + newTask.getTimeCommitment() <= DataValues.MAX_DAY_TOTAL);
    }

    private boolean doesTaskFitLifeBalance(Date date, LifeTask task){
        return doesTaskFitDayBalance(date, task) &&
               doesTaskFitWeekBalance(db.getWeekByDate(date), task);
    }

    private boolean doesTaskFitDayBalance(Date date, LifeTask task){
        LifeTask.Type type = task.getType();
        DayQuota quota = db.getDayQuotaForDate(date);
        double sumAllotment = CommonUtils.getTotalTimeForLifeTasks_FilterByType(quota.getLifeTasks(), type) + task.getTimeCommitment();
        return sumAllotment <= db.getMaxAllotmentForDayByType(type);
    }

    private boolean doesTaskFitWeekBalance(Week week, LifeTask task){
        LifeTask.Type type = task.getType();
        List<DayQuota> quotas = db.getDayQuotasForWeek(week);
        List<LifeTask> weeklyTasks = new ArrayList<>();
        for(DayQuota quota : quotas){
            weeklyTasks.addAll(quota.getLifeTasks());
        }
        double sumAllotment = CommonUtils.getTotalTimeForLifeTasks_FilterByType(weeklyTasks, type) + task.getTimeCommitment();
        return sumAllotment <= db.getMaxAllotmentForWeekByType(type);
    }
//
//    private boolean doesTaskFitMonthBalance()

}


