package org.kelvin.webapp.tools;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.kelvin.webapp.apiObjects.ScheduleSetupData;
import org.kelvin.webapp.director.DataValues;
import org.kelvin.webapp.schedule.DayQuota;
import org.kelvin.webapp.schedule.LifeTask;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Stream;

public class DataGenerator {

    public static DayQuota generateFakeDayQuota(DayOfWeek dayOfWeek){
        List<LifeTask> tasks = new ArrayList<>();
        tasks.add(new LifeTask("Daily Sleep", LifeTask.Type.SLEEP, 7.0, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
        switch (dayOfWeek){
            case SATURDAY:
            case SUNDAY:
                tasks.add(new LifeTask("Weekend Relationship", LifeTask.Type.RELATIONSHIP, 1.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
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

    public static ScheduleSetupData generateFakeScheduleData(){
        try {
            ClassLoader classLoader = ScheduleSetupData.class.getClassLoader();
            File file = new File(classLoader.getResource("schedule_data.json").getFile());
            return new Gson().fromJson(new FileReader(file), ScheduleSetupData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

//        Map<LifeTask.Type, Double> daily = new HashMap<>();
//        Map<LifeTask.Type, Double> weekly = new HashMap<>();
//        Map<LifeTask.Type, Double> monthly = new HashMap<>();
//        Map<LifeTask.Type, Double> yearly = new HashMap<>();
//        List<LifeTask> tasks = new ArrayList<>();
//
//        daily.put(LifeTask.Type.SLEEP, 8.0);
//        daily.put(LifeTask.Type.WORK, 8.5);
//        daily.put(LifeTask.Type.REST, 1.5);
//        daily.put(LifeTask.Type.SPIRIT, 1.0);
//        daily.put(LifeTask.Type.LIFE_GOAL, 1.5);
//        daily.put(LifeTask.Type.RELATIONSHIP, 1.5);
//        daily.put(LifeTask.Type.TASKS, 2.0);
//        daily.put(LifeTask.Type.UNKNOWN, 0.0);
//
//        weekly.put(LifeTask.Type.SLEEP, 56.0);
//        weekly.put(LifeTask.Type.WORK, 42.5);
//        weekly.put(LifeTask.Type.REST, 9.0);
//        weekly.put(LifeTask.Type.SPIRIT, 7.0);
//        weekly.put(LifeTask.Type.LIFE_GOAL, 6.0);
//        weekly.put(LifeTask.Type.RELATIONSHIP, 10.5);
//        weekly.put(LifeTask.Type.TASKS, 14.0);
//        weekly.put(LifeTask.Type.UNKNOWN, 0.0);
//
//
//        //====== Daily tasks
//        tasks.add(new LifeTask("Rest Time", LifeTask.Type.REST, 0.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
//        tasks.add(new LifeTask("Quiet Time", LifeTask.Type.SPIRIT, 0.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH));
//
//        //====== Weekday tasks
//        Set<DayOfWeek> weekDays = new HashSet<>(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
//        tasks.add(new LifeTask("Work", LifeTask.Type.WORK, 6.0, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH, LifeTask.RepeatType.WEEKS, 1, weekDays));
//        tasks.add(new LifeTask("Weekday Relationship", LifeTask.Type.RELATIONSHIP, 1.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH, LifeTask.RepeatType.WEEKS, 1, weekDays));
//
//        //====== Weekend tasks
//        Set<DayOfWeek> weekEnd = new HashSet<>((Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)));
//        tasks.add(new LifeTask("Weekend Relationship", LifeTask.Type.RELATIONSHIP, 1.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH, LifeTask.RepeatType.WEEKS, 1, weekEnd));
//        tasks.add(new LifeTask("Weekend Rest", LifeTask.Type.REST, 0.5, DataValues.Priority.NORMAL, DataValues.Urgency.HIGH, LifeTask.RepeatType.WEEKS, 1, weekEnd));
//
//
//        return new ScheduleSetupData(
//                daily,
//                weekly,
//                monthly,
//                yearly,
//                tasks);

    }

}
