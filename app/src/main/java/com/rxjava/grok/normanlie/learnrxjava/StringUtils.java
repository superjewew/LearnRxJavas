package com.rxjava.grok.normanlie.learnrxjava;

/**
 * Created by Norman Lie on 10/19/2017.
 */

public class StringUtils {

    public static String EMPTY = "";

    private StringUtils() {

    }

    public static boolean isNullOrEmpty(String string) {
        return !isNotNullOrEmpty(string);
    }

    public static boolean isNotNullOrEmpty(String string) {
        return string != null || !string.equals(EMPTY);
    }
}
