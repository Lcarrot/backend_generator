package ru.tyshchenko.vkr.factory.repository.java;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.tyshchenko.vkr.dto.entity.java.ColumnTypeMapper;
import ru.tyshchenko.vkr.factory.repository.RepositoryFactory;
import ru.tyshchenko.vkr.dto.repository.enums.ReturnType;
import ru.tyshchenko.vkr.dto.repository.meta.ArgumentInfo;
import ru.tyshchenko.vkr.dto.repository.meta.ConditionInfo;
import ru.tyshchenko.vkr.dto.repository.meta.RepositoryMethodInfo;
import ru.tyshchenko.vkr.dto.repository.meta.RepositoryInfo;
import ru.tyshchenko.vkr.util.PatternUtils;

import java.nio.file.Files;
import java.util.*;

import static ru.tyshchenko.vkr.util.StringUtils.*;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "repository", name = "generator", havingValue = RepositoryDialect.DATA_JPA)
public class JpaRepositoryFactory implements RepositoryFactory {

    private final ColumnTypeMapper columnTypeMapper;

    private String repositoryPattern;

    @PostConstruct
    @SneakyThrows
    public void init() {
        repositoryPattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/Repository.pattern").toPath());
    }

    @Override
    public List<String> buildRepository(Collection<RepositoryInfo> repositoryInfos) {
        List<String> repos = new ArrayList<>();
        for (RepositoryInfo repositoryInfo : repositoryInfos) {
            Set<String> imports = new HashSet<>();
            StringBuilder repositoryBuilder = new StringBuilder(repositoryPattern);
            replaceByRegex(repositoryBuilder, PatternUtils.ENTITY,
                    toUpperCaseFirstLetter(toCamelCase(repositoryInfo.getEntityName())));
            replaceByRegex(repositoryBuilder, PatternUtils.CLASS_NAME,
                    toUpperCaseFirstLetter(toCamelCase(repositoryInfo.getName())));
            StringBuilder methods = new StringBuilder();
            for (RepositoryMethodInfo repositoryMethodInfo : repositoryInfo.getRepositoryMethodInfos().values()) {
                methods.append(buildMethod(repositoryMethodInfo, repositoryInfo.getEntityName(), imports));
            }
            replaceByRegex(repositoryBuilder, PatternUtils.METHODS, methods.toString());
            replaceByRegex(repositoryBuilder, PatternUtils.IMPORTS, buildImports(imports));
            repos.add(repositoryBuilder.toString());
        }
        return repos;
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
                ColumnTypeMapper.Type type = columnTypeMapper.mapType(argumentInfo.getType());
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