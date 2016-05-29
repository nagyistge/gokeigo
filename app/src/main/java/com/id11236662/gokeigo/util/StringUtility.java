package com.id11236662.gokeigo.util;

import java.util.List;

/**
 * Helper class in utilising strings.
 */
public class StringUtility {

    /**
     * @param strings a List of strings to be concatenated together.
     * @return the list of strings in one concatenated string with a comma (,) delimiter
     */
    public static String join(List<String> strings) {
        String result = "";
        for (int i = 0; i < strings.size(); i++) {
            result += strings.get(i);

            // Add comma to the list if it's not the last element.
            if (i < strings.size() -1) {
                result += ", ";
            }
        }
        return result;
    }
}
