package me.sammc19.betteroregen.util;

public class StringUtils {
    public static String toSnakeCase(String string) {
        return string.toLowerCase().replace(" ", "_");
    }
}
