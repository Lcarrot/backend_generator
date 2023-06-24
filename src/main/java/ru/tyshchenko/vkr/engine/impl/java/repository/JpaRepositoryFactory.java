package ru.tyshchenko.vkr.engine.impl.java.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.tyshchenko.vkr.engine.api.models.repository.enums.ReturnType;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.ArgumentInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.ConditionInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.RepositoryInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.RepositoryMethodInfo;
import ru.tyshchenko.vkr.engine.api.factory.DefaultPlaceholder;
import ru.tyshchenko.vkr.engine.api.factory.repository.RepositoryFactory;
import ru.tyshchenko.vkr.engine.impl.SupportedRepositoryLanguage;
import ru.tyshchenko.vkr.engine.resolver.type.mapper.JavaColumnTypeMapper;
import ru.tyshchenko.vkr.engine.resolver.type.mapper.Type;

import java.nio.file.Files;
import java.util.*;

import static ru.tyshchenko.vkr.engine.util.StringUtils.*;

@Component
@RequiredArgsConstructor
public class JpaRepositoryFactory implements RepositoryFactory {

    private final JavaColumnTypeMapper columnTypeMapper;

    private String repositoryPattern;

    @PostConstruct
    @SneakyThrows
    public void init() {
        repositoryPattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/Repository.pattern").toPath());
    }

    @Override
    public Map<String, String> buildRepository(Collection<RepositoryInfo> repositoryInfos) {
        Map<String, String> repos = new HashMap<>();
        for (RepositoryInfo repositoryInfo : repositoryInfos) {
            Set<String> imports = new HashSet<>();
            StringBuilder repositoryBuilder = new StringBuilder(repositoryPattern);
            replaceByRegex(repositoryBuilder, DefaultPlaceholder.ENTITY,
                    toUpperCaseFirstLetter(toCamelCase(repositoryInfo.getEntityName())));
            replaceByRegex(repositoryBuilder, DefaultPlaceholder.CLASS_NAME,
                    toUpperCaseFirstLetter(toCamelCase(repositoryInfo.getName())));
            StringBuilder methods = new StringBuilder();
            for (RepositoryMethodInfo repositoryMethodInfo : repositoryInfo.getRepositoryMethodInfos().values()) {
                methods.append(buildMethod(repositoryMethodInfo, repositoryInfo.getEntityName(), imports));
            }
            replaceByRegex(repositoryBuilder, DefaultPlaceholder.METHODS, methods.toString());
            replaceByRegex(repositoryBuilder, DefaultPlaceholder.IMPORTS, buildImports(imports));
            repos.put(repositoryInfo.getName(), repositoryBuilder.toString());
        }
        return repos;
    }

    @Override
    public String language() {
        return SupportedRepositoryLanguage.JAVA_SPRING_DATA.name();
    }

    private String buildImports(Set<String> imports) {
        StringBuilder importString = new StringBuilder();
        for (String anImport : imports) {
            importString.append("import ").append(anImport).append(";\n");
        }
        return importString.toString();
    }

    private String buildMethod(RepositoryMethodInfo repositoryMethodInfo, String entityName, Set<String> imports) {
        StringBuilder methodBuilder = new StringBuilder();
        StringBuilder argumentBuilder = new StringBuilder();
        if (repositoryMethodInfo.getReturnType() == ReturnType.ENTITY_LIST) {
            imports.add("java.util.List");
            methodBuilder.append(TAB).append("List<")
                    .append(toUpperCaseFirstLetter(toCamelCase(entityName))).append("> ");
        } else {
            imports.add("java.util.Optional");
            methodBuilder.append(TAB).append("Optional<")
                    .append(toUpperCaseFirstLetter(toCamelCase(entityName))).append("> ");
        }
        methodBuilder.append(repositoryMethodInfo.getName());
        for (ConditionInfo conditionInfo : repositoryMethodInfo.getConditionInfos()) {
            for (ArgumentInfo argumentInfo : conditionInfo.getArgumentInfos()) {
                Type type = columnTypeMapper.mapType(argumentInfo.getType());
                if (type.packet() != null) {
                    imports.add(type.packet() + "." + type.type());
                }
                argumentBuilder.append(type.type()).append(" ").append(toCamelCase(argumentInfo.getName())).append(", ");
            }
        }
        argumentBuilder.replace(argumentBuilder.length() - 2, argumentBuilder.length() - 1, "");
        methodBuilder.append("(").append(argumentBuilder).append(");\n");
        return methodBuilder.toString();
    }
}