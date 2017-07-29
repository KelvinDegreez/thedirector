package org.kelvin.webapp.director;

import org.kelvin.webapp.schedule.LifeTask;
import org.kelvin.webapp.schedule.ScheduleDatabase;

public interface Director {

    void setScheduleDatabase(ScheduleDatabase db);

    boolean canDoToday(LifeTask newTask);

    boolean canDoThisWeek(LifeTask newTask);

    boolean canDoNextWeek(LifeTask newTask);

    boolean canDoThisMonth(LifeTask newTask);

    boolean canDoNextMonth(LifeTask newTask);

}
