package org.kelvin.webapp.director;

import org.junit.Before;
import org.junit.Test;
import org.kelvin.webapp.schedule.LifeTask;
import org.kelvin.webapp.schedule.TestScheduleDatabase;
import org.kelvin.webapp.tools.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class DirectorTest {

    private Director director;
    private TestScheduleDatabase db;
    private List<LifeTask> defaultLifeTasks = createDefaultTestLifeTasks();
    private List<LifeTask> testLifeTasks = createTestLifeTasks(1.0);
    private double defaultTotal = CommonUtils.getTotalTimeForLifeTasks(defaultLifeTasks);

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
        int count = 0;
        for(LifeTask task : testLifeTasks){
            boolean valAbleToDo = (task.getTimeCommitment() + defaultTotal) < DataValues.MAX_DAY_TOTAL;
            double sumTotal = CommonUtils.getTotalTimeForLifeTasks_FilterByType(defaultLifeTasks, task.getType()) + task.getTimeCommitment();
            boolean valShouldDo = sumTotal <= db.getMaxAllotmentForDayByType(task.getType());
            assertEquals(count+"",valAbleToDo && valShouldDo, director.canDoToday(task));
        }
    }

    @Test
    public void canDoThisWeek() throws Exception {
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

    private List<LifeTask> createTestLifeTasks(double timeIncrement){
        List<LifeTask> list = new ArrayList<>();
        int count=0;
        for(LifeTask.Type type : LifeTask.Type.values()){
            for(DataValues.Priority priority : DataValues.Priority.values()){
                for(DataValues.Urgency urgency : DataValues.Urgency.values()){
                    double timeCommitment = 0.0;
                    while(timeCommitment < 24.0){
                        count++;
                        timeCommitment += timeIncrement;
                        list.add(new LifeTask("Task #"+count, type, timeCommitment, priority, urgency));
                    }
                }
            }
        }


        return list;

    }

    private List<LifeTask> createDefaultTestLifeTasks(){
        List<LifeTask> list = new ArrayList<>();
        list.add(new LifeTask("Daily Sleep", LifeTask.Type.SLEEP, 8.0, DataValues.Priority.NORMAL, DataValues.Urgency.DAYS));
        list.add(new LifeTask("Daily Work", LifeTask.Type.WORK, 8.0, DataValues.Priority.NORMAL, DataValues.Urgency.HOURS));
        list.add(new LifeTask("Daily Rest", LifeTask.Type.REST, 1.5, DataValues.Priority.NORMAL, DataValues.Urgency.DAYS));
        list.add(new LifeTask("Quiet Time", LifeTask.Type.SPIRIT, 1.0, DataValues.Priority.NORMAL, DataValues.Urgency.DAYS));
        return list;
    }
}