package com.id11236662.gokeigo.util;

import java.util.List;

/**
 * Helper class in utilising strings.
 */
public class StringUtils {

    public static String join(List<String> strings) {
        String result = "";
        for (int i = 0; i < strings.size(); i++) {
            result += strings.get(i);

            // if not the last element, add a comma
            if (i < strings.size() -1) {
                result += ", ";
            }
        }
        return result;
    }
}
