package me.sammc19.betteroregen.util;

public class StringUtils {
    private static String toSnakeCase(String string) {
        return string.toLowerCase().replace(" ", "_");
    }
}
