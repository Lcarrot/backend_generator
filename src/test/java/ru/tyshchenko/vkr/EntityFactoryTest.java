package ru.tyshchenko.vkr;

import lombok.SneakyThrows;
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
import ru.tyshchenko.vkr.factory.entity.app.HibernateEntityFactory;
import ru.tyshchenko.vkr.factory.entity.sql.PostgresSqlFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class EntityFactoryTest {

    @Autowired
    private PostgresSqlFactory postgresSqlFactory;
    @Autowired
    private HibernateEntityFactory hibernateEntityFactory;
    @Autowired
    private EntityInfoMetaInfoFactory entityInfoMetaInfoFactory;

    private List<EntityInfo> entityInfos;

    @BeforeEach
    public void initVal() {
        Column column = Column.builder()
                .name("test_field")
                .type(ColumnType.TEXT)
                .constraintTypes(Stream.of(ConstraintType.NOT_NULL).collect(Collectors.toSet()))
                .build();
        Entity firstEntity = Entity.builder()
                .entityName("test_entity")
                .columns(List.of(column))
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
        entityInfos = entityInfoMetaInfoFactory.getMetaInfo(List.of(firstEntity, secondEntity), List.of(referenceColumn));
    }

    @Test
    @SneakyThrows
    void testSqlbuilder() {
        String result = postgresSqlFactory.buildEntities(entityInfos);
        System.out.println(result);
    }

    @Test
    void testHibernateBuilder() {
        List<String> results = hibernateEntityFactory.buildEntities(entityInfos);
        results.forEach(System.out::println);
    }
}
