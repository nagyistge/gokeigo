package com.id11236662.gokeigo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Helper class in utilising strings and dates, converting booleans into ints and converting ints into booleans.
 */

public class TypeUtility {

    /**
     * @param strings a List of strings to be concatenated together.
     * @return the list of strings in one concatenated string with a comma (,) delimiter
     */

    public static String join(List<String> strings) {
        String result = "";
        for (int i = 0; i < strings.size(); i++) {
            result += strings.get(i);

            // Add comma to the list if it's not the last element.
            if (i < strings.size() - 1) {
                result += ", ";
            }
        }
        return result;
    }

    public static String getDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return formatter.format(date);
    }

    public static int getIntFromBoolean(boolean value) {
        return value ? 1 : 0;
    }

    public static boolean getBooleanFromInt(int value) {
        return value == 1;
    }
}
