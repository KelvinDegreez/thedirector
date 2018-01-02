package org.kelvin.webapp.googleAPIs;

import com.google.api.client.util.DateTime;
import org.kelvin.webapp.director.DataValues;
import org.kelvin.webapp.schedule.LifeTask;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

public class GoogleApiConverter {


    public static List<LifeTask> convertEventsToLifeTasks(Events events){
        List<LifeTask> tasks = new ArrayList<>();

        for (Event event : events.getItems()) {

            LifeTask.Type type = LifeTask.Type.UNKNOWN;
            DataValues.Priority priority = DataValues.Priority.UNKNOWN;
            DataValues.Urgency urgency = DataValues.Urgency.UNKNOWN;

            if(event.getDescription() != null){
                //TODO: Turn into generic parser
                String description = event.getDescription();
                Pattern p_type = Pattern.compile("<director-type>(.*?)</director-type>");
                Pattern p_priority = Pattern.compile("<director-priority>(.*?)</director-priority>");
                Pattern p_urgency = Pattern.compile("<director-urgency>(.*?)</director-urgency>");

                Matcher m_type = p_type.matcher(description);
                Matcher m_priority = p_priority.matcher(description);
                Matcher m_urgency = p_urgency.matcher(description);

                String type1 = m_type.group(1);
                String priority1 = m_priority.group(1);
                String urgency1 = m_urgency.group(1);

                int pause=0;
                pause++;
            }

            tasks.add(new LifeTask(
                        event.getSummary(),
                        type,
                        getEventTimeCommitment(event),
                        priority,
                        urgency));
        }
        return tasks;
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
