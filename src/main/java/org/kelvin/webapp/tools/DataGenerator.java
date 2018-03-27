package org.kelvin.webapp.tools;


import org.kelvin.webapp.schedule.Week;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataGenerator {

    public static List<Week> createWeeksForYear(int year){
        Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.JANUARY, 1);
        Date date = cal.getTime();

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(year, Calendar.DECEMBER, 31);
        Date endDate = calEnd.getTime();

        List<Week> weeks = new ArrayList<>();

        while (endDate.after(date)){
            Week week = createWeekByStartDate(date);
            date = CommonUtils.addDays(week.getEndDate(), 1);
            weeks.add(week);
        }
        return weeks;
    }

    public static Week createWeekByStartDate(Date date){
        Date startDate = CommonUtils.removeTimeFromDate(date);
        Date endDate = CommonUtils.addDays(startDate, 6);
        return new Week(startDate, endDate, CommonUtils.getDateListForRange(startDate, endDate));
    }

}
