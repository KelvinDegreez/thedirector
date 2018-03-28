package org.kelvin.webapp.tools;


import org.kelvin.webapp.director.DataValues;
import org.kelvin.webapp.schedule.DayQuota;
import org.kelvin.webapp.schedule.LifeTask;
import org.kelvin.webapp.schedule.Week;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataGenerator {

    public static DayQuota generateFakeDayQuota(DayOfWeek dayOfWeek){
        List<LifeTask> tasks = new ArrayList<>();
        tasks.add(new LifeTask("Daily Sleep", LifeTask.Type.SLEEP, 7.0, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
        switch (dayOfWeek){
            case SATURDAY:
            case SUNDAY:
                tasks.add(new LifeTask("Weekend Relationship", LifeTask.Type.RELATIONSHIP, 6.0, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
                tasks.add(new LifeTask("Weekend Rest", LifeTask.Type.REST, 0.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
                tasks.add(new LifeTask("Quiet Time", LifeTask.Type.SPIRIT, 0.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
                break;
            default:
                tasks.add(new LifeTask("Weekday Relationship", LifeTask.Type.RELATIONSHIP, 1.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
                tasks.add(new LifeTask("Daily Work", LifeTask.Type.WORK, 6.0, DataValues.Priority.NORMAL, DataValues.Urgency.EXTREME));
                tasks.add(new LifeTask("Daily Rest", LifeTask.Type.REST, 0.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
                tasks.add(new LifeTask("Quiet Time", LifeTask.Type.SPIRIT, 0.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
                break;
        }


        return new DayQuota(tasks);
    }

}
