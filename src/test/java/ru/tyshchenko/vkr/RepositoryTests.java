package ru.tyshchenko.vkr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tyshchenko.vkr.entity.Column;
import ru.tyshchenko.vkr.entity.EntityInfo;
import ru.tyshchenko.vkr.entity.source.Entity;
import ru.tyshchenko.vkr.entity.source.ReferenceColumn;
import ru.tyshchenko.vkr.entity.types.ColumnType;
import ru.tyshchenko.vkr.entity.types.ConstraintType;
import ru.tyshchenko.vkr.entity.types.ReferenceType;
import ru.tyshchenko.vkr.factory.entity.EntityInfoMetaInfoFactory;
import ru.tyshchenko.vkr.factory.repository.RepositoryMetaInfoFactory;
import ru.tyshchenko.vkr.factory.repository.java.JpaRepositoryFactory;
import ru.tyshchenko.vkr.repository.enums.LinkCondition;
import ru.tyshchenko.vkr.repository.enums.OperationCondition;
import ru.tyshchenko.vkr.repository.enums.ReturnType;
import ru.tyshchenko.vkr.repository.meta.RepositoryInfo;
import ru.tyshchenko.vkr.repository.source.MethodCondition;
import ru.tyshchenko.vkr.repository.source.MethodSource;
import ru.tyshchenko.vkr.repository.source.RepositorySource;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@SpringBootTest
class RepositoryTests {

    @Autowired
    private EntityInfoMetaInfoFactory entityInfoMetaInfoFactory;
    @Autowired
    private RepositoryMetaInfoFactory repositoryMetaInfoFactory;
    @Autowired
    private JpaRepositoryFactory repositoryFactory;

    private List<RepositoryInfo> repositoryInfos;

    @BeforeEach
    public void initVal() {
        Column column = Column.builder()
                .name("test_field")
                .type(ColumnType.TEXT)
                .constraintTypes(Stream.of(ConstraintType.NOT_NULL).collect(Collectors.toSet()))
                .build();
        Column secColumn = Column.builder()
                .name("date_field")
                .type(ColumnType.TIMESTAMP)
                .constraintTypes(Stream.of(ConstraintType.NOT_NULL).collect(Collectors.toSet()))
                .build();
        Entity firstEntity = Entity.builder()
                .entityName("test_entity")
                .columns(List.of(column, secColumn))
                .primaryKey("pr_key")
                .build();
        Entity secondEntity = Entity.builder()
                .entityName("new_entity")
                .columns(List.of(column))
                .primaryKey("pr_key")
                .build();
        ReferenceColumn referenceColumn = ReferenceColumn.builder()
                .entityTo(firstEntity.getEntityName())
                .entityFrom(secondEntity.getEntityName())
                .referenceType(ReferenceType.ONE_TO_MANY)
                .build();
        List<EntityInfo> entityInfos =
                entityInfoMetaInfoFactory.getMetaInfo(List.of(firstEntity, secondEntity), List.of(referenceColumn));

        MethodSource methodSource = MethodSource.builder().returnType(ReturnType.ENTITY.name().toLowerCase())
                .condition(List.of(
                        MethodCondition.builder()
                                .operationCondition(OperationCondition.EQUAL_IGNORE_CASE)
                                .field("test_field")
                                .link(LinkCondition.AND)
                                .build(),
                        MethodCondition.builder()
                                .operationCondition(OperationCondition.BETWEEN)
                                .field("date_field")
                                .build())
                ).build();

        RepositorySource source = RepositorySource.builder()
                .entityName(firstEntity.getEntityName())
                .name("test_rep")
                .methodSources(List.of(methodSource))
                .build();
        repositoryInfos = repositoryMetaInfoFactory.buildRepositoryInfo(List.of(source),
                entityInfos.stream().collect(toMap(EntityInfo::getEntityName, Function.identity())));
    }

    @Test
    void testRepBuild() {
        System.out.println(repositoryFactory.buildRepository(repositoryInfos));
    }
}