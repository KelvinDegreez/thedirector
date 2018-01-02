package org.kelvin.webapp.director;

import org.junit.Before;
import org.junit.Test;
import org.kelvin.webapp.schedule.DayQuota;
import org.kelvin.webapp.schedule.LifeTask;
import org.kelvin.webapp.schedule.TestScheduleDatabase;
import org.kelvin.webapp.schedule.Week;
import org.kelvin.webapp.tools.CommonUtils;
import java.util.*;

import static org.junit.Assert.*;


public class DirectorTest {

    private Director director;
    private TestScheduleDatabase db;
    private Map<DataValues.DayOfWeek, List<LifeTask>> defaultLifeWeeklyTasksMap = createDefaultTestLifeTasks();
    private Map<LifeTask.Type, List<LifeTask>> testLifeTasksMap = createTestLifeTasksMap();

    @Before
    public void setUp() throws Exception {
        db = new TestScheduleDatabase();
        db.initDatabase(createDefaultTestLifeTasks());
        director = new TestDirector();
        director.setScheduleDatabase(db);
    }


    @Test
    public void setScheduleDatabase() throws Exception {

    }

    @Test
    public void canDoToday() throws Exception {
        Date today = db.getCurrentDay();
        DataValues.DayOfWeek dayOfWeek = CommonUtils.getDayOfWeekForDate(today);
        for(List<LifeTask> testLifeTasks : testLifeTasksMap.values()) {
            for (LifeTask task : testLifeTasks) {
                double sumDay = (task.getTimeCommitment() + CommonUtils.getTotalTimeForLifeTasks(db.getDayQuotaForDate(today).getLifeTasks()));
                boolean valAbleToDo = sumDay < DataValues.MAX_DAY_TOTAL;

                double sumTotalDay = CommonUtils.getTotalTimeForLifeTasks_FilterByType(defaultLifeWeeklyTasksMap.get(dayOfWeek), task.getType()) + task.getTimeCommitment();
                boolean valShouldDoDay = sumTotalDay <= db.getMaxAllotmentForDayByType(task.getType());

                double sumTotalWeek = task.getTimeCommitment();
                for(DataValues.DayOfWeek item : DataValues.DayOfWeek.values()){
                    sumTotalWeek += CommonUtils.getTotalTimeForLifeTasks_FilterByType(defaultLifeWeeklyTasksMap.get(item), task.getType());
                }
                boolean valShouldDoWeek = sumTotalWeek <= db.getMaxAllotmentForWeekByType(task.getType());
                System.out.println(
                        "---------------\n"+
                        "Remaining Day Time: "+db.getRemainingTimeForDay(today)+"\n"+
                        "Remaining Day Time for "+task.getType()+": "+db.getRemainingTimeForDayByLifeTaskType(today, task.getType())+"\n"+
                        "Task Time: "+task.getTimeCommitment()+"\n"+
                        "Able to Do: "+valAbleToDo+"\n"+
                        "Should Do: "+valShouldDoWeek+"\n"+
                        "Task"+task+"\n"+
                        "---------------\n");
                assertEquals(
                        task + "\nval_AbleDay?: " +valAbleToDo+ "\nval_ShouldDay?: "+valShouldDoDay,
                        valAbleToDo && valShouldDoDay && valShouldDoWeek, director.canDoToday(task));
            }
        }
    }

    @Test
    public void canDoThisWeek() throws Exception {
        Week week = db.getCurrentWeek();
        for (List<LifeTask> testLifeTasks : testLifeTasksMap.values()) {
            for (LifeTask task : testLifeTasks) {
                List<LifeTask> weeklyLifeTasks = new ArrayList<>();
                for(DayQuota quota : db.getDayQuotasForWeek(week)){
                    weeklyLifeTasks.addAll(quota.getLifeTasks());
                }
                double weekTimeRemaining = CommonUtils.getTotalTimeForLifeTasks(weeklyLifeTasks);
                double sumWeek = task.getTimeCommitment() + weekTimeRemaining;
                boolean valAbleToDo = sumWeek < DataValues.MAX_WEEK_TOTAL;

                double sumTotalWeek = task.getTimeCommitment();
                for (DataValues.DayOfWeek item : DataValues.DayOfWeek.values()) {
                    sumTotalWeek += CommonUtils.getTotalTimeForLifeTasks_FilterByType(defaultLifeWeeklyTasksMap.get(item), task.getType());
                }
                boolean valShouldDoWeek = sumTotalWeek <= db.getMaxAllotmentForWeekByType(task.getType());
                System.out.println(
                        "---------------\n"+
                        "Remaining Week Time: "+db.getRemainingTimeForWeek(week)+"\n"+
                        "Remaining Week Time for "+task.getType()+": "+db.getRemainingTimeForWeekByLifeTaskType(week, task.getType())+"\n"+
                        "Task Time: "+task.getTimeCommitment()+"\n"+
                        "Able to Do: "+valAbleToDo+"\n"+
                        "Should Do: "+valShouldDoWeek+"\n"+
                        "Task"+task+"\n"+
                        "---------------\n");
                assertEquals(
                        task + "\nval_AbleWeek?: " + valAbleToDo + "\nval_ShouldWeek?: " + valShouldDoWeek,
                        valAbleToDo && valShouldDoWeek, director.canDoThisWeek(task));
            }
        }
    }

    @Test
    public void canDoNextWeek() throws Exception {
    }

    @Test
    public void canDoThisMonth() throws Exception {
    }

    @Test
    public void canDoNextMonth() throws Exception {
    }

    private Map<LifeTask.Type, List<LifeTask>> createTestLifeTasksMap(){
        Map<LifeTask.Type, List<LifeTask>> map = new HashMap<>();
        int count=0;
        for(LifeTask.Type type : LifeTask.Type.values()){
            List<LifeTask> list = new ArrayList<>();
            for(DataValues.Priority priority : DataValues.Priority.values()){
                for(DataValues.Urgency urgency : DataValues.Urgency.values()){
                    double timeCommitment = 0.0;
                    while(timeCommitment < 24.0){
                        count++;
                        timeCommitment += CommonUtils.getHoursDecimalFromHoursAndMinutes(0, 30);
                        list.add(new LifeTask("Task #"+count, type, timeCommitment, priority, urgency));
                    }
                }
            }
            map.put(type, list);
        }
        return map;
    }

    private Map<DataValues.DayOfWeek, List<LifeTask>> createDefaultTestLifeTasks(){
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
                case THRUSDAY:
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