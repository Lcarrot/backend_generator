package ru.tyshchenko.vkr.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.tyshchenko.vkr.context.SimpleBuilderContext;
import ru.tyshchenko.vkr.engine.api.models.general.GeneralInfoDto;
import ru.tyshchenko.vkr.engine.api.factory.DefaultPlaceholder;
import ru.tyshchenko.vkr.util.PathUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static ru.tyshchenko.vkr.util.StringUtils.*;

@Component
@RequiredArgsConstructor
public class MainSpringMavenService {

    private static final String PATH = "project";
    private static final String POM_FILE = "pom.xml";

    private final SimpleBuilderContext context;

    private String mainClassPattern;
    private String testMainPattern;

    @PostConstruct
    @SneakyThrows
    public void init() {
        mainClassPattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/default/Application.pattern").toPath());
        testMainPattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/default/Test.pattern").toPath());
    }

    @SneakyThrows
    public void createEmptyProject(String destinationDirectory) {
        Path sourcePath = Path.of(System.getProperty("user.dir")).resolve(PathUtils.SOURCE_PATH)
                .resolve("resources").resolve(PATH);
        Path distPath = Path.of(destinationDirectory);
        if (!Files.exists(distPath)) {
            Files.createDirectories(distPath);
        }
        try (Stream<Path> pathStream = Files.walk(sourcePath)) {
            pathStream.forEach(source -> {
                Path destination = Paths.get(destinationDirectory, source.toString().substring(sourcePath.toString().length()));
                try {
                    if (!Files.exists(destination)) {
                        Files.copy(source, destination);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @SneakyThrows
    public void addDefaultInformation(GeneralInfoDto generalInfoDto) {
        context.setPacket(generalInfoDto.getProjectPacket() + "." + generalInfoDto.getProjectName());
        Path dirPath = Paths.get(generalInfoDto.getProjectPath());
        Path javaCodePath = dirPath.resolve(PathUtils.SOURCE_CODE_PATH)
                .resolve(String.join(File.separator, generalInfoDto.getProjectPacket().split("\\.")))
                .resolve(generalInfoDto.getProjectName());
        Files.createDirectories(javaCodePath);
        Path testCodePath = dirPath.resolve(PathUtils.TEST_PATH)
                .resolve(String.join(File.separator, generalInfoDto.getProjectPacket().split("\\.")))
                .resolve(generalInfoDto.getProjectName());
        Files.createDirectories(testCodePath);
        context.setProjectPath(dirPath);
        context.setSourceCodePath(javaCodePath);
        addInfoInPom(generalInfoDto, dirPath);
        addInfoInAppProperties(generalInfoDto, dirPath);
        createMainClass(generalInfoDto, javaCodePath);
        createTestClass(generalInfoDto, testCodePath);
    }

    @SneakyThrows
    private void addInfoInPom(GeneralInfoDto generalInfoDto, Path root) {
        Path pomPath = root.resolve(POM_FILE);
        StringBuilder contentBuilder = new StringBuilder(Files.readString(pomPath));
        replaceDbConfig(contentBuilder, generalInfoDto);
        replaceByRegex(contentBuilder, "${project_packet}", generalInfoDto.getProjectPacket());
        replaceByRegex(contentBuilder, "${project}", generalInfoDto.getProjectName());
        Files.write(pomPath, contentBuilder.toString().getBytes());
    }

    @SneakyThrows
    private void addInfoInAppProperties(GeneralInfoDto generalInfoDto, Path root) {
        Path appPropPath = root.resolve(PathUtils.APP_PROPERTIES);
        StringBuilder contentBuilder = new StringBuilder(Files.readString(appPropPath));
        replaceDbConfig(contentBuilder, generalInfoDto);
        Files.write(appPropPath, contentBuilder.toString().getBytes());
    }

    private void replaceDbConfig(StringBuilder builder, GeneralInfoDto generalInfoDto) {
        replaceByRegex(builder, "${spring.datasource.username}", generalInfoDto.getDb_user());
        replaceByRegex(builder, "${spring.datasource.password}", generalInfoDto.getDb_password());
        replaceByRegex(builder, "${spring.datasource.url}", generalInfoDto.getDb_url());
    }

    @SneakyThrows
    private void createMainClass(GeneralInfoDto generalInfoDto, Path javaCodePath) {
        String projectName = toUpperCaseFirstLetter(toCamelCase(generalInfoDto.getProjectName()));
        Path mainClass = javaCodePath.resolve(projectName + "Application" + PathUtils.EXTENSION);
        StringBuilder content = new StringBuilder(mainClassPattern);
        replaceByRegex(content, "${project}", projectName);
        replaceByRegex(content, DefaultPlaceholder.PROJECT_PACKET, context.getPacket());
        Files.write(mainClass, content.toString().getBytes());
    }

    @SneakyThrows
    private void createTestClass(GeneralInfoDto generalInfoDto, Path testPath) {
        String projectName = toUpperCaseFirstLetter(toCamelCase(generalInfoDto.getProjectName()));
        Path mainClass = testPath.resolve(projectName + "Tests" + PathUtils.EXTENSION);
        StringBuilder content = new StringBuilder(testMainPattern);
        replaceByRegex(content, "${project}", projectName);
        replaceByRegex(content, DefaultPlaceholder.PROJECT_PACKET, context.getPacket());
        Files.write(mainClass, content.toString().getBytes());
    }
}