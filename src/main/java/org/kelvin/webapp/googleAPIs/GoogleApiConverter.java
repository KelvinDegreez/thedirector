package org.kelvin.webapp.googleAPIs;

import java.time.DayOfWeek;
import java.util.*;

import org.kelvin.webapp.director.DataValues;
import org.kelvin.webapp.schedule.LifeTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;


public class GoogleApiConverter {
    private static String typeHeader = "director-type";
    private static String priorityHeader = "director-priority";
    private static String urgencyHeader = "director-urgency";

    public static Map<DayOfWeek, List<LifeTask>> convertEventsToLifeTasks(Events events){
        //TODO: only converts events of current week. need to convert events from any date range
        Map<DayOfWeek, List<LifeTask>> weeklyTasks = new HashMap<>();
        for(DayOfWeek day : DayOfWeek.values()){
            weeklyTasks.put(day, new ArrayList<LifeTask>());
        }

        Calendar cal = Calendar.getInstance();
        for (Event event : events.getItems()) {

            LifeTask.Type type = LifeTask.Type.UNKNOWN;
            DataValues.Priority priority = DataValues.Priority.UNKNOWN;
            DataValues.Urgency urgency = DataValues.Urgency.UNKNOWN;

            if(event.getDescription() != null){
                //TODO: Turn into generic parser
                String description = event.getDescription();
                Pattern p_type = Pattern.compile("&lt;"+typeHeader+"&gt;(.*?)&lt;/"+typeHeader+"&gt;");
                Pattern p_priority = Pattern.compile("&lt;"+priorityHeader+"&gt;(.*?)&lt;/"+priorityHeader+"&gt;");
                Pattern p_urgency = Pattern.compile("&lt;"+urgencyHeader+"&gt;(.*?)&lt;/"+urgencyHeader+"&gt;");

                Matcher m_type = p_type.matcher(description);
                Matcher m_priority = p_priority.matcher(description);
                Matcher m_urgency = p_urgency.matcher(description);


                if (m_type.find()) {
                    type = LifeTask.Type.valueOf(m_type.group(1));
                }
                if(m_priority.find()){
                    priority = DataValues.Priority.valueOf(m_priority.group(1));
                }
                if(m_urgency.find()){
                    urgency = DataValues.Urgency.valueOf(m_urgency.group(1));
                }
            }

            if(event.getStart() != null && event.getStart().getDateTime() != null) {
                cal.setTimeInMillis(event.getStart().getDateTime().getValue());
                DayOfWeek day = DayOfWeek.of(cal.get(Calendar.DAY_OF_WEEK));

                weeklyTasks.get(day).add(
                        new LifeTask(
                                event.getSummary(),
                                type,
                                getEventTimeCommitment(event),
                                priority,
                                urgency));
            }
        }
        return weeklyTasks;
    }

    public static Double getEventTimeCommitment(Event event){
        Double timeCommitment = 0.0;
        if (event.getStart() != null && event.getEnd() != null) {
                long startTime = event.getStart().getDateTime().getValue();
                long endTime = event.getEnd().getDateTime().getValue();
                timeCommitment = new Long(((endTime - startTime) / (1000 * 60 * 60)) % 24).doubleValue();
        }
        return timeCommitment;
    }

}
