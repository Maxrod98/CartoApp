package com.ecarto.cartoapp.utils;

import java.text.DecimalFormat;

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
        if (decimalFormat == null){
            decimalFormat = new DecimalFormat("#");
            decimalFormat.setGroupingUsed(true);
            decimalFormat.setGroupingSize(3);
        }

        return decimalFormat.format(amount);
    }
}
