package org.kelvin.webapp.schedule;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Week {

    private static int MAX_DATES = 7;

    private Date startDate;
    private Date endDate;
    private List<Date> days = new ArrayList<>();

    public Week(Date startDate, Date endDate, List<Date> days){
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public List<Date> getDays() {
        return days;
    }
}
