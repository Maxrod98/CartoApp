package com.ecarto.cartoapp.utils;

import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StringUtils {
    public static DecimalFormat decimalFormat;
    public static final String PATTERN = "dd/MM/yyyy";

    public static String validateLength(String original, Integer max_size){
        if (original == null) return "";
        String desc = original;
        if (desc.length() > max_size){
            desc = desc.substring(0, max_size) + "...";
        }

        return desc;
    }

    public static String formatMoney(Integer amount){
        if (amount == null) amount = 0;

        if (decimalFormat == null){
            decimalFormat = new DecimalFormat("#");
            decimalFormat.setGroupingUsed(true);
            decimalFormat.setGroupingSize(3);
        }

        return "$" + decimalFormat.format(amount);
    }

    public static String formatDateFromLong(Long date){
        return (new SimpleDateFormat(PATTERN)).format(new Date(date));
    }

    public static long formatDateFromString(String date){
        long result_date = 0;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            java.util.Date d = dateFormat.parse(date);
            result_date = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result_date;
    }

    public static String formateDateToMonthAndWhetherIsToday(Long date){
        Date dat = new Date(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);

        Calendar calToday = Calendar.getInstance();
        String timeStampStr = "";
        if ((cal.get(Calendar.DAY_OF_MONTH) == calToday.get(Calendar.DAY_OF_MONTH))  //if the same day
                && (cal.get(Calendar.MONTH) == calToday.get(Calendar.MONTH))
                && (cal.get(Calendar.YEAR) == calToday.get(Calendar.YEAR))){
            timeStampStr += " hoy ";
        }

        if ((cal.get(Calendar.WEEK_OF_MONTH) == calToday.get(Calendar.WEEK_OF_MONTH))  //if the same day
                && (cal.get(Calendar.MONTH) == calToday.get(Calendar.MONTH))
                && (cal.get(Calendar.YEAR) == calToday.get(Calendar.YEAR))){
            timeStampStr += " semana ";
        }


        Locale current = Locale.getDefault();
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG , current) + timeStampStr;
    }

    public static Long getUniqueID(){
        Calendar c = Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        return c.getTimeInMillis();
    }

    public static String getTodaysDateAsString(){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year_ = calendar.get(Calendar.YEAR);

        return day + "/" + (month + 1) + "/" + year_;
    }

}
