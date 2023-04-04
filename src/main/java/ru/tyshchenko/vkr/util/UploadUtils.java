package ru.tyshchenko.vkr.util;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static ru.tyshchenko.vkr.util.StringUtils.toCamelCase;
import static ru.tyshchenko.vkr.util.StringUtils.toUpperCaseFirstLetter;

public class UploadUtils {

    @SneakyThrows
    public static void saveFile(Path dirPath, String packet, Map<String, String> files) {
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        for (var file : files.entrySet()) {
            var value = file.getValue().replace(PatternUtils.PROJECT_PACKET, packet);
            var filePath = dirPath.resolve(toUpperCaseFirstLetter(toCamelCase(file.getKey())) + PathUtils.EXTENSION);
            Files.write(filePath, value.getBytes());
        }
    }
}
