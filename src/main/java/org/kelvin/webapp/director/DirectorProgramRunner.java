package org.kelvin.webapp.director;

import org.kelvin.webapp.googleAPIs.GoogleCalendarApi;
import org.kelvin.webapp.schedule.TestScheduleDatabase;
import java.io.IOException;

public class DirectorProgramRunner {

    private Director director;
    private TestScheduleDatabase db;

    private DirectorProgramRunner(){
        db = new TestScheduleDatabase();
        db.initDatabase(GoogleCalendarApi.getGoogleCalendarLifeTasks());
        director = new TestDirector();
        director.setScheduleDatabase(db);
    }

    private void run(){
        int pause=0;
        pause++;
    }

    public static void main(String[] args) throws IOException {
        DirectorProgramRunner runner = new DirectorProgramRunner();
        runner.run();
    }
}
