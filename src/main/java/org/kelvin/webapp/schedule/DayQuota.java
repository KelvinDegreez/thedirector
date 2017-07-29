package org.kelvin.webapp.schedule;


import java.util.ArrayList;
import java.util.List;

public class DayQuota {

    private Double taskTimeCount = 0.0;
    private List<LifeTask> dayTasks = new ArrayList<>();

    public DayQuota(List<LifeTask> tasks){
        addLifeTasks(tasks);
    }

    public List<LifeTask> getLifeTasks(){
        return dayTasks;
    }

    public void addLifeTask(LifeTask task){
        taskTimeCount += task.getTimeCommitment();
        dayTasks.add(task);
    }

    public void addLifeTasks(List<LifeTask> tasks){
        for(LifeTask task : tasks){
            taskTimeCount += task.getTimeCommitment();
        }
        dayTasks.addAll(tasks);
    }

    public double getTotalTaskTime(){
        return taskTimeCount;
    }
}
