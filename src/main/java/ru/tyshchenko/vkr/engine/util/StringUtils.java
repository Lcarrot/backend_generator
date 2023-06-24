package ru.tyshchenko.vkr.engine.util;

public final class StringUtils {

    public static final String TAB = "    ";

    public static String toClassName(String source) {
        return toUpperCaseFirstLetter(toCamelCase(source));
    }

    public static String toCamelCase(String source) {
        StringBuilder builder = new StringBuilder(source);
        int index = builder.indexOf("_");
        while (index != -1) {
           builder.replace(index, index + 2, builder.substring(index + 1, index + 2).toUpperCase());
           index = builder.indexOf("_");
        }
        return builder.toString();
    }

    public static String toUpperCaseFirstLetter(String source) {
        return source.substring(0, 1).toUpperCase() + source.substring(1);
    }

    public static String toLowerCaseFirstLetter(String source) {
        return source.substring(0, 1).toLowerCase() + source.substring(1);
    }

    public static void replaceByRegex(StringBuilder builder, String pattern, String value) {
        int start = builder.indexOf(pattern);
        while (start != -1) {
            builder.replace(start, start + pattern.length(), value);
            start = builder.indexOf(pattern);
        }
    }
}
