package com.casestudy.credit.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DateUtil {

    private DateUtil(){ throw new IllegalStateException("Utility Class");}

    private static final List<Integer> WEEKENDS = Arrays.asList(Calendar.SATURDAY, Calendar.SUNDAY);
    public static final String TIME_ZONE_ISTANBUL = "Europe/Istanbul";

    public static Integer calculateLateDays(Date installmentDate){
        return getLateDayTwoDates(resetHour(installmentDate), resetHour(DateUtil.getCurrentDate()) );
    }

    public static Date resetHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    public static Integer getLateDayTwoDates(Date installmentDate, Date secondDate) {
        if (installmentDate != null && secondDate != null) {
            LocalDate firstDateLocal = fromDateToLocalDate(findBusinessDay(installmentDate));
            LocalDate secondDateLocal = fromDateToLocalDate(secondDate);
            int lateDay = Math.toIntExact(ChronoUnit.DAYS.between(firstDateLocal, secondDateLocal));
            return Math.max(lateDay,0);
        }
        return 0;
    }

    public static LocalDate fromDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public static Date findBusinessDay(Date date){
        while (isWeekend(date)){
            date = addDays(date,1);
        }
        return date;
    }

    public static Date addDays(Date d, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static boolean isWeekend(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return WEEKENDS.stream().anyMatch(x -> x== c.get(Calendar.DAY_OF_WEEK));
    }

    public static Date getCurrentDate(){
        return Calendar.getInstance().getTime();
    }
}
