package org.kelvin.webapp.director;


import java.util.Calendar;

public class DataValues {

    public static final double MAX_DAY_TOTAL = 24.0;
    public static final double MAX_WEEK_TOTAL = 168.0;

    public enum Priority {
        LOW(1, "Trival"),
        NORMAL(2, "Everyday - Normal"),
        IMPORTANT(3, "Everyday - Important"),
        HIGH(4, "Life Events - High"),
        CRUCIAL(5, "Life or Death - Crucial"),
        UNKNOWN(0, "Unknown");
        int weight;
        public String name;
        private Priority(int weight, String name){
            this.weight = weight;
            this.name = name;
        }
    }

    public enum Urgency {
        MINIMAL(1, "Minimal"), // Years before consequences
        LOW(2, "Low"),         // Months before ..
        NORMAL(3, "Normal"),   // Weeks before ..
        HIGH(4, "High"),       // Days before ..
        EXTREME(5, "Extreme"), // Hours before ..
        UNKNOWN(0, "Unknown");
        int weight;
        public String name;
        private Urgency(int weight, String name){
            this.weight = weight;
            this.name = name;
        }
    }

    public enum DayOfWeek {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY;
        public static DayOfWeek fromCalendarDay(int calendarDay){
            switch (calendarDay){
                case Calendar.MONDAY: return MONDAY;
                case Calendar.TUESDAY: return TUESDAY;
                case Calendar.WEDNESDAY: return WEDNESDAY;
                case Calendar.THURSDAY: return THURSDAY;
                case Calendar.FRIDAY: return FRIDAY;
                case Calendar.SATURDAY: return SATURDAY;
                case Calendar.SUNDAY: return SUNDAY;
                default:
                    return MONDAY;
            }
        }
    }
}
