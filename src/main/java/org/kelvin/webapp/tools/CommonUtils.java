package org.kelvin.webapp.tools;



import org.kelvin.webapp.director.DataValues;
import org.kelvin.webapp.schedule.LifeTask;
import org.kelvin.webapp.schedule.Week;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;


public class CommonUtils {

    public static Date getToday() {
        return removeTimeFromDate(Calendar.getInstance().getTime());
    }

    public static Date getWeekStartDay(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return removeTimeFromDate(c.getTime());
    }

    public static Date getWeekEndDay(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return removeTimeFromDate(c.getTime());
    }

    public static boolean onSameDate(Date date1, Date date2) {
        return removeTimeFromDate(date1).equals(removeTimeFromDate(date2));
    }

    public static Week getThisWeek() {
        //TODO: Fix This
        return new Week(getToday(), getToday(), new ArrayList<Date>());
    }

    public static List<Date> getDateListForRange(Date startDate, Date endDate) {
        List<Date> dateList = new ArrayList<>();
        Date date = removeTimeFromDate(startDate);
        while (!date.after(endDate)) {
            dateList.add(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, 1);
            date = removeTimeFromDate(cal.getTime());
        }
        return dateList;
    }

    public static Date addHoursMins(Date date, int hours, int mins) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hours); //minus number would decrement the hours
        cal.add(Calendar.MINUTE, mins); //minus number would decrement the mins
        return cal.getTime();
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(CommonUtils.removeTimeFromDate(date));
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months); //minus number would decrement the months
        return cal.getTime();
    }

    public enum DateType {
        API_DATE(new SimpleDateFormat("MM-dd-yyyy", Locale.US));
        public SimpleDateFormat dateFormat;

        DateType(SimpleDateFormat dateFormat) {
            this.dateFormat = dateFormat;
        }

    }

    public static boolean isParsableToDouble(String doubleString) {
        final String Digits     = "(\\p{Digit}+)";
        final String HexDigits  = "(\\p{XDigit}+)";
        // an exponent is 'e' or 'E' followed by an optionally
        // signed decimal integer.
        final String Exp        = "[eE][+-]?"+Digits;
        final String fpRegex    =
        ("[\\x00-\\x20]*"+ // Optional leading "whitespace"
        "[+-]?(" +         // Optional sign character
        "NaN|" +           // "NaN" string
        "Infinity|" +      // "Infinity" string

        // A decimal floating-point string representing a finite positive
        // number without a leading sign has at most five basic pieces:
        // Digits . Digits ExponentPart FloatTypeSuffix
        //
        // Since this method allows integer-only strings as input
        // in addition to strings of floating-point literals, the
        // two sub-patterns below are simplifications of the grammar
        // productions from the Java Language Specification, 2nd
        // edition, section 3.10.2.

        // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
        "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

        // . Digits ExponentPart_opt FloatTypeSuffix_opt
        "(\\.("+Digits+")("+Exp+")?)|"+

        // Hexadecimal strings
        "((" +
        // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
        "(0[xX]" + HexDigits + "(\\.)?)|" +

        // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
        "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

        ")[pP][+-]?" + Digits + "))" +
        "[fFdD]?))" +
        "[\\x00-\\x20]*");// Optional trailing "whitespace"

        return (Pattern.matches(fpRegex, doubleString));
    }

    public static String formatCurrencyString(Double amount) {
        if(amount != null){
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            format.setMinimumFractionDigits(2);
            return format.format(amount);
        }else{
            return "";
        }
    }

    public static String formatCurrencyString(String amount) {
        Double money;
        if (amount == null) {
            return "";
        }
        try {
            money = Double.valueOf(amount);
        } catch (NumberFormatException e) {
            return amount;
        }
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        format.setMinimumFractionDigits(2);
        return format.format(money);
    }

    public static String formatDateByDateType(Date date, DateType type) {
        return type.dateFormat.format(date);
    }

    public static Date parseDateByDateType(String date, DateType type) {
        try {
            return type.dateFormat.parse(date);
        } catch (Exception e) {
            int pause=0;
            pause++;
            return null;
        }
    }

    public static String convertDateStringToDateType(String dateString, DateType dateStringType, DateType conversionType) {
        try {
            Date date = dateStringType.dateFormat.parse(dateString);
            return conversionType.dateFormat.format(date);
        } catch (Exception e) {
            return dateString;
        }
    }


    public static Pair<Integer, Integer> getHoursMinutesPairFromHoursDecimal(Double total) {

        double fractionalPart = total % 1;
        fractionalPart = 0.02 * (Math.round(fractionalPart / 0.02));

        int minutes = decimalMinutesConversionMap.containsKey(fractionalPart) ?
                    decimalMinutesConversionMap.get(fractionalPart) :
                    (int) (60 * fractionalPart);
        int hours = (int) Math.round(total - fractionalPart);


        return new Pair<>(hours, minutes);
    }

    public static String getHoursMinsStringFromHoursDecimal(Double total) {
        if (total != null) {
            Pair<Integer, Integer> hoursMins = CommonUtils.getHoursMinutesPairFromHoursDecimal(total);
            int hours = hoursMins.first;
            int mins = hoursMins.second;
            return hours + " hrs" + (mins > 0 ? " " + mins + " mins" : "");
        } else {
            return "";
        }
    }

    public static Double getHoursDecimalFromHoursAndMinutes(int hours, int minutes) {
        double hoursTime = (double) hours;
        double minutesTime = (double) minutes;
        return hoursTime + (minutesTime / 60);
    }

    public static Double getHoursDecimalBetweenTwoDates(Date startDate, Date endDate) {
        long ms = Math.abs(startDate.getTime() - endDate.getTime());
        return getHoursDecimalFromHoursAndMinutes((int) (ms / (1000 * 60)) / 60, (int) (ms / (1000 * 60)) % 60);
    }

    public static Date removeTimeFromDate(Date date) {
        SimpleDateFormat justDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        String dateString = justDateFormat.format(date);
        try {
            return justDateFormat.parse(dateString);
        } catch (ParseException e) {
            return date;
        }
    }

    public static Date getFirstDayOfMonthForDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return CommonUtils.removeTimeFromDate(cal.getTime());
    }

    public static Date getLastTimeInDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(removeTimeFromDate(date));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        return cal.getTime();
    }

    private static Map<Double, Integer> decimalMinutesConversionMap = new HashMap<>();
    static {
        decimalMinutesConversionMap.put(.02, 1);
        decimalMinutesConversionMap.put(.03, 2);
        decimalMinutesConversionMap.put(.05, 3);
        decimalMinutesConversionMap.put(.07, 4);
        decimalMinutesConversionMap.put(.08, 5);
        decimalMinutesConversionMap.put(.10, 6);
        decimalMinutesConversionMap.put(.12, 7);
        decimalMinutesConversionMap.put(.13, 8);
        decimalMinutesConversionMap.put(.15, 9);
        decimalMinutesConversionMap.put(.17, 10);
        decimalMinutesConversionMap.put(.18, 11);
        decimalMinutesConversionMap.put(.20, 12);
        decimalMinutesConversionMap.put(.22, 13);
        decimalMinutesConversionMap.put(.23, 14);
        decimalMinutesConversionMap.put(.25, 15);
        decimalMinutesConversionMap.put(.27, 16);
        decimalMinutesConversionMap.put(.28, 17);
        decimalMinutesConversionMap.put(.30, 18);
        decimalMinutesConversionMap.put(.32, 19);
        decimalMinutesConversionMap.put(.33, 20);
        decimalMinutesConversionMap.put(.35, 21);
        decimalMinutesConversionMap.put(.37, 22);
        decimalMinutesConversionMap.put(.38, 23);
        decimalMinutesConversionMap.put(.40, 24);
        decimalMinutesConversionMap.put(.42, 25);
        decimalMinutesConversionMap.put(.43, 26);
        decimalMinutesConversionMap.put(.45, 27);
        decimalMinutesConversionMap.put(.47, 28);
        decimalMinutesConversionMap.put(.48, 29);
        decimalMinutesConversionMap.put(.50, 30);
        decimalMinutesConversionMap.put(.52, 31);
        decimalMinutesConversionMap.put(.53, 32);
        decimalMinutesConversionMap.put(.55, 33);
        decimalMinutesConversionMap.put(.57, 34);
        decimalMinutesConversionMap.put(.58, 35);
        decimalMinutesConversionMap.put(.60, 36);
        decimalMinutesConversionMap.put(.62, 37);
        decimalMinutesConversionMap.put(.63, 38);
        decimalMinutesConversionMap.put(.65, 39);
        decimalMinutesConversionMap.put(.67, 40);
        decimalMinutesConversionMap.put(.68, 41);
        decimalMinutesConversionMap.put(.70, 42);
        decimalMinutesConversionMap.put(.72, 43);
        decimalMinutesConversionMap.put(.73, 44);
        decimalMinutesConversionMap.put(.75, 45);
        decimalMinutesConversionMap.put(.77, 46);
        decimalMinutesConversionMap.put(.78, 47);
        decimalMinutesConversionMap.put(.80, 48);
        decimalMinutesConversionMap.put(.82, 49);
        decimalMinutesConversionMap.put(.83, 50);
        decimalMinutesConversionMap.put(.85, 51);
        decimalMinutesConversionMap.put(.87, 52);
        decimalMinutesConversionMap.put(.88, 53);
        decimalMinutesConversionMap.put(.90, 54);
        decimalMinutesConversionMap.put(.92, 55);
        decimalMinutesConversionMap.put(.93, 56);
        decimalMinutesConversionMap.put(.95, 57);
        decimalMinutesConversionMap.put(.97, 58);
        decimalMinutesConversionMap.put(.98, 59);
    }

    //==================
    public static Double getTotalTimeForLifeTasks(List<LifeTask> tasks){
        double sum = 0.0;
        for(LifeTask task : tasks){
            sum += task.getTimeCommitment();
        }
        return sum;
    }

    public static Double getTotalTimeForLifeTasks_FilterByType(List<LifeTask> tasks, LifeTask.Type type){
        double sumAllotment = 0.0;
        for(LifeTask task : tasks) {
            if(task.getType() == type){
                sumAllotment += task.getTimeCommitment();
            }
        }
        return sumAllotment;
    }

    public static DataValues.DayOfWeek getDayOfWeekForDate(Date date){
        switch (date.getDay()){
            case 0: return DataValues.DayOfWeek.SUNDAY;
            case 1: return DataValues.DayOfWeek.MONDAY;
            case 2: return DataValues.DayOfWeek.TUESDAY;
            case 3: return DataValues.DayOfWeek.WEDNESDAY;
            case 4: return DataValues.DayOfWeek.THURSDAY;
            case 5: return DataValues.DayOfWeek.FRIDAY;
            case 6: return DataValues.DayOfWeek.SATURDAY;
            default:  return DataValues.DayOfWeek.SUNDAY;
        }
    }
}
