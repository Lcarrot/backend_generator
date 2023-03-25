package ru.tyshchenko.vkr.factory.repository.java;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.tyshchenko.vkr.entity.java.ColumnTypeMapper;
import ru.tyshchenko.vkr.factory.repository.RepositoryFactory;
import ru.tyshchenko.vkr.repository.enums.ReturnType;
import ru.tyshchenko.vkr.repository.meta.ArgumentInfo;
import ru.tyshchenko.vkr.repository.meta.ConditionInfo;
import ru.tyshchenko.vkr.repository.meta.MethodInfo;
import ru.tyshchenko.vkr.repository.meta.RepositoryInfo;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public List<String> buildRepository(List<RepositoryInfo> repositoryInfos) {
        List<String> repos = new ArrayList<>();
        for (RepositoryInfo repositoryInfo : repositoryInfos) {
            Set<String> imports = new HashSet<>();
            StringBuilder repositoryBuilder = new StringBuilder(repositoryPattern);
            replaceByRegex(repositoryBuilder, "${entity_name}",
                    toUpperCaseFirstLetter(toCamelCase(repositoryInfo.getEntityName())));
            replaceByRegex(repositoryBuilder, "${repository_name}",
                    toUpperCaseFirstLetter(toCamelCase(repositoryInfo.getName())));
            StringBuilder methods = new StringBuilder();
            for (MethodInfo methodInfo : repositoryInfo.getMethodInfos()) {
                methods.append(buildMethod(methodInfo, repositoryInfo.getEntityName(), imports));
            }
            replaceByRegex(repositoryBuilder, "${methods}", methods.toString());
            replaceByRegex(repositoryBuilder, "${imports}", buildImports(imports));
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

    private String buildMethod(MethodInfo methodInfo, String entityName, Set<String> imports) {
        StringBuilder methodBuilder = new StringBuilder();
        StringBuilder argumentBuilder = new StringBuilder();
        if (methodInfo.getReturnType() == ReturnType.ENTITY_LIST) {
            imports.add("java.util.List");
            methodBuilder.append(TAB).append("List<")
                    .append(toUpperCaseFirstLetter(toCamelCase(entityName))).append("> ");
        } else {
            imports.add("java.util.Optional");
            methodBuilder.append(TAB).append("Optional<")
                    .append(toUpperCaseFirstLetter(toCamelCase(entityName))).append("> ");
        }
        methodBuilder.append("findBy");
        for (ConditionInfo conditionInfo : methodInfo.getConditionInfos()) {
            methodBuilder.append(buildCondition(conditionInfo));
            for (ArgumentInfo argumentInfo : conditionInfo.getArgumentInfos()) {
                String type = columnTypeMapper.mapType(argumentInfo.getType());
                if (type.contains(".")) {
                    imports.add(type);
                    String[] typesPart = type.split("\\.");
                    type = typesPart[typesPart.length - 1];
                }
                argumentBuilder.append(type).append(" ").append(toCamelCase(argumentInfo.getName())).append(", ");
            }
        }
        argumentBuilder.replace(argumentBuilder.length() - 2, argumentBuilder.length() - 1, "");
        methodBuilder.append("(").append(argumentBuilder).append(");\n");
        return methodBuilder.toString();
    }

    private String buildCondition(ConditionInfo conditionInfo) {
        StringBuilder conditionBuilder = new StringBuilder();
        conditionBuilder.append(toUpperCaseFirstLetter(toCamelCase(conditionInfo.getColumn().getName())));
        switch (conditionInfo.getOperationCondition()) {
            case EQUAL_IGNORE_CASE, EQUAL -> conditionBuilder.append(
                    toUpperCaseFirstLetter(toCamelCase(conditionInfo.getOperationCondition().name().toLowerCase()
                            .replace("equal", ""))));
            default -> conditionBuilder.append(
                    toUpperCaseFirstLetter(toCamelCase(conditionInfo.getOperationCondition().name().toLowerCase())));
        }
        if (conditionInfo.getLinkCondition() != null) {
            conditionBuilder.append(toUpperCaseFirstLetter(conditionInfo.getLinkCondition().name().toLowerCase()));
        }
        return conditionBuilder.toString();
    }

}