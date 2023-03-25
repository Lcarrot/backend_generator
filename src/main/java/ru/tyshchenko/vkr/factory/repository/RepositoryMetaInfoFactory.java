package ru.tyshchenko.vkr.factory.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.entity.Column;
import ru.tyshchenko.vkr.entity.EntityInfo;
import ru.tyshchenko.vkr.repository.enums.OperationCondition;
import ru.tyshchenko.vkr.repository.enums.ReturnType;
import ru.tyshchenko.vkr.repository.meta.ArgumentInfo;
import ru.tyshchenko.vkr.repository.meta.ConditionInfo;
import ru.tyshchenko.vkr.repository.meta.MethodInfo;
import ru.tyshchenko.vkr.repository.meta.RepositoryInfo;
import ru.tyshchenko.vkr.repository.source.RepositorySource;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public class RepositoryMetaInfoFactory {

    public List<RepositoryInfo> buildRepositoryInfo(List<RepositorySource> repositorySources,
                                                    Map<String, EntityInfo> entityInfoMap) {
        return repositorySources.stream().map(repositorySource -> {
            var repInfo = RepositoryInfo.builder()
                    .name(repositorySource.getName())
                    .entityName(repositorySource.getEntityName())
                    .build();
            Map<String, Column> columnMap = entityInfoMap.get(repositorySource.getEntityName()).getColumns()
                    .stream().collect(toMap(Column::getName, Function.identity()));
            repInfo.setMethodInfos(repositorySource.getMethodSources().stream().map(methodSource ->
                    MethodInfo.builder()
                            .returnType(ReturnType.valueOf(methodSource.returnType.toUpperCase()))
                            .conditionInfos(methodSource.condition.stream().map(condition ->
                                    ConditionInfo.builder()
                                            .column(columnMap.get(condition.getField()))
                                            .linkCondition(condition.getLink())
                                            .argumentInfos(buildArguments(columnMap.get(condition.getField()),
                                                    condition.getOperationCondition()))
                                            .operationCondition(condition.getOperationCondition())
                                            .build()
                            ).collect(Collectors.toList()))
                            .build()).collect(Collectors.toList())
            );
            return repInfo;
        }).collect(Collectors.toList());
    }

    private List<ArgumentInfo> buildArguments(Column column, OperationCondition operationCondition) {
        if (operationCondition == OperationCondition.BETWEEN) {
            return List.of(ArgumentInfo.builder().type(column.getType())
                            .name(column.getName() + operationCondition.name() + "Less").build(),
                    ArgumentInfo.builder().type(column.getType())
                            .name(column.getName() + operationCondition.name() + "Greater").build()
            );
        }
        return List.of(ArgumentInfo.builder().type(column.getType())
                .name(column.getName() + operationCondition.name()).build());
    }
}