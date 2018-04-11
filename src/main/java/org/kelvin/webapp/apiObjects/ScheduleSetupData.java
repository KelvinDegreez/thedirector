package org.kelvin.webapp.apiObjects;

import org.kelvin.webapp.schedule.LifeTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleSetupData {

    private Map<LifeTask.Type, Double> maxDailyTimeAllotmentMap = new HashMap<>();
    private Map<LifeTask.Type, Double> maxWeeklyTimeAllotmentMap = new HashMap<>();
    private Map<LifeTask.Type, Double> maxMonthlyTimeAllotmentMap = new HashMap<>();
    private Map<LifeTask.Type, Double> maxYearlyTimeAllotmentMap = new HashMap<>();
    private List<LifeTask> taskList = new ArrayList<>();

    public ScheduleSetupData(){

    }

    public ScheduleSetupData(Map<LifeTask.Type, Double> maxDailyTimeAllotmentMap,
                             Map<LifeTask.Type, Double> maxWeeklyTimeAllotmentMap,
                             Map<LifeTask.Type, Double> maxMonthlyTimeAllotmentMap,
                             Map<LifeTask.Type, Double> maxYearlyTimeAllotmentMap,
                             List<LifeTask> taskList){
        this.maxDailyTimeAllotmentMap = maxDailyTimeAllotmentMap;
        this.maxWeeklyTimeAllotmentMap = maxWeeklyTimeAllotmentMap;
        this.maxMonthlyTimeAllotmentMap = maxMonthlyTimeAllotmentMap;
        this.maxYearlyTimeAllotmentMap = maxYearlyTimeAllotmentMap;
        this.taskList = taskList;
    }

    public Map<LifeTask.Type, Double> getMaxDailyTimeAllotmentMap() {
        return maxDailyTimeAllotmentMap;
    }

    public Map<LifeTask.Type, Double> getMaxWeeklyTimeAllotmentMap() {
        return maxWeeklyTimeAllotmentMap;
    }

    public Map<LifeTask.Type, Double> getMaxMonthlyTimeAllotmentMap() {
        return maxMonthlyTimeAllotmentMap;
    }

    public Map<LifeTask.Type, Double> getMaxYearlyTimeAllotmentMap() {
        return maxYearlyTimeAllotmentMap;
    }

    public List<LifeTask> getTaskList() {
        return taskList;
    }
}
