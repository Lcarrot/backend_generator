package ru.tyshchenko.vkr.engine.util;

import lombok.SneakyThrows;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;
import java.util.Set;

public final class PatternUtils {

    @SneakyThrows
    public static String getPatternFromResource(String path) {
        return Files.readString(
                ResourceUtils.getFile("classpath:" + path).toPath());
    }

    public static String buildImports(Set<String> imports) {
        StringBuilder importString = new StringBuilder();
        for (String imp : imports) {
            importString.append("import ").append(imp).append(";\n");
        }
        return importString.toString();
    }
}
