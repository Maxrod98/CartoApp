package com.ecarto.cartoapp.utils;

import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StringUtils {
    public static DecimalFormat decimalFormat;

    public static String validateLength(String original, Integer max_size){
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
        return (new SimpleDateFormat("dd/MM/yyyy")).format(new Date(date));
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


}
