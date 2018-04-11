package org.kelvin.webapp.director;

import org.junit.Before;
import org.junit.Test;
import org.kelvin.webapp.schedule.ScheduleDatabase;
import org.kelvin.webapp.schedule.TestScheduleDatabase;

public class ScheduleDatabaseTest {

    private ScheduleDatabase db;

    @Before
    public void setUp() throws Exception {
        db = TestScheduleDatabase.createWithFakeData();
    }

    @Test
    public void testSandbox() throws Exception {
        int pause=0;
        pause++;
    }
}
