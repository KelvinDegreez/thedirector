package org.kelvin.webapp.director;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.kelvin.webapp.schedule.DayQuota;
import org.kelvin.webapp.schedule.LifeTask;
import org.kelvin.webapp.schedule.TestScheduleDatabase;
import org.kelvin.webapp.schedule.Week;
import org.kelvin.webapp.tools.CommonUtils;
import org.kelvin.webapp.tools.DataGenerator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;


public class DirectorTest {

    private Director director;
    private TestScheduleDatabase db;
    private Map<DayOfWeek, List<LifeTask>> defaultLifeWeeklyTasksMap = createDefaultTestLifeTasks();
    private Map<LifeTask.Type, List<LifeTask>> testLifeTasksMap = createTestLifeTasksMap();


    public static Map<DayOfWeek, List<LifeTask>> createDefaultTestLifeTasks() {
        Map<DayOfWeek, List<LifeTask>> map = new HashMap<>();
        for(DayOfWeek dayOfWeek : DayOfWeek.values()){
            map.put(dayOfWeek, DataGenerator.generateFakeDayQuota(dayOfWeek).getLifeTasks());
        }
        return map;
    }

   @Before
    public void setUp() throws Exception {
        db = TestScheduleDatabase.createWithFakeData();
        director = new TestDirector();
        director.setScheduleDatabase(db);
    }

    @Test
    public void testSandbox() throws Exception {

    }

   @Test
    public void setScheduleDatabase() throws Exception {
       for(List<LifeTask> testLifeTasks : testLifeTasksMap.values()) {
           for (LifeTask task : testLifeTasks) {
                String json = new Gson().toJson(task);
                int pause=0;
                pause++;
                break;
           }
       }
   }

   @Test
    public void canDoToday() throws Exception {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        for(List<LifeTask> testLifeTasks : testLifeTasksMap.values()) {
            for (LifeTask task : testLifeTasks) {
                double sumDay = (task.getTimeCommitment() + CommonUtils.getTotalTimeForLifeTasks(db.getDayQuotaForDate(today).getLifeTasks()));
                boolean valAbleToDo = sumDay < DataValues.MAX_DAY_TOTAL;

                double sumTotalDay = CommonUtils.getTotalTimeForLifeTasks_FilterByType(defaultLifeWeeklyTasksMap.get(dayOfWeek), task.getType()) + task.getTimeCommitment();
                boolean valShouldDoDay = sumTotalDay <= db.getMaxDailyAllotmentByForType(task.getType());

                double sumTotalWeek = task.getTimeCommitment();
                for(DayOfWeek item : DayOfWeek.values()){
                    sumTotalWeek += CommonUtils.getTotalTimeForLifeTasks_FilterByType(defaultLifeWeeklyTasksMap.get(item), task.getType());
                }
                boolean valShouldDoWeek = sumTotalWeek <= db.getMaxWeeklyAllotmentForType(task.getType());
                System.out.println(
                        "---------------\n"+
                        "Remaining Day Time: "+db.getRemainingTimeForDay(today)+"\n"+
                        "Remaining Day Time for "+task.getType()+": "+db.getRemainingTimeForDayByLifeTaskType(today, task.getType())+"\n"+
                        "Task Time: "+task.getTimeCommitment()+"\n"+
                        "Able to Do: "+valAbleToDo+"\n"+
                        "Should Do: "+valShouldDoDay+"\n"+
                        "Task"+task+"\n"+
                        "---------------\n");

                if(valShouldDoDay){
                    assertTrue("valAbleToDo can't be false if valShouldDoToday is true", valAbleToDo);
                }

                assertEquals(
                        task + "\nval_AbleDay?: " +valAbleToDo+ "\nval_ShouldDay?: "+valShouldDoDay,
                        valAbleToDo && valShouldDoDay && valShouldDoWeek, director.canDoToday(task));
            }
        }
    }

   @Test
    public void canDoThisWeek() throws Exception {
        Week week = new Week(LocalDate.now());
        for (List<LifeTask> testLifeTasks : testLifeTasksMap.values()) {
            for (LifeTask task : testLifeTasks) {
                List<LifeTask> weeklyLifeTasks = new ArrayList<>();
                for(DayQuota quota : db.getDayQuotasForDateRange(week.getStartDate(), week.getEndDate())){
                    weeklyLifeTasks.addAll(quota.getLifeTasks());
                }
                double weekTimeRemaining = CommonUtils.getTotalTimeForLifeTasks(weeklyLifeTasks);
                double sumWeek = task.getTimeCommitment() + weekTimeRemaining;
                boolean valAbleToDo = sumWeek < DataValues.MAX_WEEK_TOTAL;

                double sumTotalWeek = task.getTimeCommitment();
                for (DayOfWeek item : DayOfWeek.values()) {
                    sumTotalWeek += CommonUtils.getTotalTimeForLifeTasks_FilterByType(defaultLifeWeeklyTasksMap.get(item), task.getType());
                }
                boolean valShouldDoWeek = sumTotalWeek <= db.getMaxWeeklyAllotmentForType(task.getType());
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

}