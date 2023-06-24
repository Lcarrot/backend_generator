package ru.tyshchenko.vkr.engine.util;

import lombok.SneakyThrows;
import ru.tyshchenko.vkr.engine.api.factory.DefaultPlaceholder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class UploadUtils {

    @SneakyThrows
    public static void saveFile(Path dirPath, String packet, Map<String, String> files) {
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        for (var file : files.entrySet()) {
            var value = file.getValue().replace(DefaultPlaceholder.PROJECT_PACKET, packet);
            var filePath = dirPath.resolve(StringUtils.toUpperCaseFirstLetter(StringUtils.toCamelCase(file.getKey())) + PathUtils.EXTENSION);
            Files.write(filePath, value.getBytes());
        }
    }
}
