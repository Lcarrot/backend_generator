package ru.tyshchenko.vkr.util;

import java.nio.file.Path;

public final class PathUtils {

    public static Path SOURCE_PATH = Path.of("src", "main");
    public static Path SOURCE_CODE_PATH = SOURCE_PATH.resolve("java");
    public static Path TEST_PATH = Path.of("src", "test", "java");
    public static Path POM_XML = Path.of("pom.xml");
    public static Path APP_PROPERTIES = SOURCE_PATH.resolve("resources").resolve("application.properties");
}
