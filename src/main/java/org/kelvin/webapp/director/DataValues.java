package org.kelvin.webapp.director;


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
        YEARS(1, "Minimal"),
        MONTHS(2, "Low"),
        WEEKS(3, "Normal"),
        DAYS(4, "High"),
        HOURS(5, "Extreme"),
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
        THRUSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }
}
