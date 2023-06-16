package ru.tyshchenko.vkr.engine.resolver.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.engine.api.models.entity.meta.Column;
import ru.tyshchenko.vkr.engine.api.models.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.enums.LinkCondition;
import ru.tyshchenko.vkr.engine.api.models.repository.enums.OperationCondition;
import ru.tyshchenko.vkr.engine.api.models.repository.enums.ReturnType;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.ArgumentInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.ConditionInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.RepositoryInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.RepositoryMethodInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.source.RepositoryMethodCondition;
import ru.tyshchenko.vkr.engine.api.models.repository.source.RepositorySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static ru.tyshchenko.vkr.util.ClassNameUtils.REPOSITORY;
import static ru.tyshchenko.vkr.util.StringUtils.toCamelCase;
import static ru.tyshchenko.vkr.util.StringUtils.toUpperCaseFirstLetter;

@Component
@RequiredArgsConstructor
public class RepositoryMetaInfoFactory {

    public Map<String, RepositoryInfo> buildRepositoryInfo(List<RepositorySource> repositorySources,
                                                           Map<String, EntityInfo> entityInfoMap) {
        Map<String, RepositoryInfo> result = new HashMap<>();
        for (var repositorySource : repositorySources) {
            var repositoryInfo = RepositoryInfo.builder()
                    .name(repositorySource.getName() + REPOSITORY)
                    .entityName(repositorySource.getEntityName())
                    .build();
            Map<String, Column> columnMap = entityInfoMap.get(repositorySource.getEntityName()).getColumns()
                    .stream().collect(toMap(Column::getName, Function.identity()));
            for (var methodSource : repositorySource.getMethods()) {
                var repMethodInfo = RepositoryMethodInfo.builder()
                        .returnType(ReturnType.valueOf(methodSource.returnType.toUpperCase()))
                        .conditionInfos(methodSource.conditions.stream()
                                .map(condition -> getConditionInfo(condition, columnMap))
                                .collect(Collectors.toList()))
                        .build();
                repMethodInfo.setName(buildMethodName(repMethodInfo.getConditionInfos()));
                repositoryInfo.getRepositoryMethodInfos().put(repMethodInfo.getName(), repMethodInfo);
            }
            result.put(repositoryInfo.getName(), repositoryInfo);
        }
        return result;
    }

    private String buildMethodName(List<ConditionInfo> conditionInfos) {
        StringBuilder methodNameBuilder = new StringBuilder("findBy");
        for (ConditionInfo conditionInfo : conditionInfos) {
            methodNameBuilder.append(buildConditionName(conditionInfo));
        }
        return methodNameBuilder.toString();
    }

    private String buildConditionName(ConditionInfo conditionInfo) {
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

    private ConditionInfo getConditionInfo(RepositoryMethodCondition condition, Map<String, Column> columnMap) {
        return ConditionInfo.builder()
                .column(columnMap.get(condition.getField()))
                .linkCondition(condition.getLink() == null ? null :
                        LinkCondition.valueOf(condition.getLink().toUpperCase()))
                .argumentInfos(buildArguments(columnMap.get(condition.getField()),
                        OperationCondition.valueOf(condition.getOperationCondition().toUpperCase())))
                .operationCondition(OperationCondition.valueOf(condition.getOperationCondition()))
                .build();
    }

    private List<ArgumentInfo> buildArguments(Column column, OperationCondition operationCondition) {
        if (operationCondition == OperationCondition.BETWEEN) {
            return List.of(ArgumentInfo.builder().type(column.getType())
                            .name(column.getName() + operationCondition.name() + "LESS").build(),
                    ArgumentInfo.builder().type(column.getType())
                            .name(column.getName() + operationCondition.name() + "GREATER").build()
            );
        }
        return List.of(ArgumentInfo.builder().type(column.getType())
                .name(column.getName() + operationCondition.name()).build());
    }
}