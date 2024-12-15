package com.example.quanly_vlxd.help;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class DateConvert {

    private static Date convertDate(int year, int month, int day) {
        Calendar cal= Calendar.getInstance();
        cal.set(year, month-1, day);
        return cal.getTime();
    }
    public static Date getDate(int year, int quarter, String point) {
        int month = switch (quarter) {
            case 1 -> point.equals("start") ? 1 : 3;
            case 2 -> point.equals("start") ? 4 : 6;
            case 3 -> point.equals("start") ? 7 : 9;
            case 4 -> point.equals("start") ? 10 : 12;
            default -> 0;
        };
        int day= point.equals("start") ? 1 : LocalDate.of(year, month, 1).lengthOfMonth();
        return convertDate(year, month, day);
    }
    public static int getMonth(Date date) {
        Calendar cal= Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH)+1;
    }
    public static int getYear(Date date) {
        Calendar cal= Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
}
