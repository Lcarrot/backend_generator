package ru.tyshchenko.vkr;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tyshchenko.vkr.dto.entity.meta.Column;
import ru.tyshchenko.vkr.dto.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.dto.entity.source.ColumnSource;
import ru.tyshchenko.vkr.dto.entity.source.Entity;
import ru.tyshchenko.vkr.dto.entity.source.ReferenceColumn;
import ru.tyshchenko.vkr.dto.entity.types.ColumnType;
import ru.tyshchenko.vkr.dto.entity.types.ConstraintType;
import ru.tyshchenko.vkr.dto.entity.types.ReferenceType;
import ru.tyshchenko.vkr.factory.dto.entity.DtoFactory;
import ru.tyshchenko.vkr.factory.dto.entity.DtoMetaInfoFactory;
import ru.tyshchenko.vkr.factory.entity.EntityInfoMetaInfoFactory;
import ru.tyshchenko.vkr.factory.entity.app.HibernateEntityFactory;
import ru.tyshchenko.vkr.factory.entity.sql.PostgresSqlFactory;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@SpringBootTest
public class EntityFactoryTest {

    @Autowired
    private PostgresSqlFactory postgresSqlFactory;
    @Autowired
    private HibernateEntityFactory hibernateEntityFactory;
    @Autowired
    private EntityInfoMetaInfoFactory entityInfoMetaInfoFactory;

    @Autowired
    private DtoMetaInfoFactory dtoMetaInfoFactory;

    @Autowired
    private DtoFactory dtoFactory;

    private List<EntityInfo> entityInfos;

    @BeforeEach
    public void initVal() {
        ColumnSource column = ColumnSource.builder()
                .name("test_field")
                .type(ColumnType.TEXT.name())
                .constraintTypes(Stream.of(ConstraintType.NOT_NULL.name()).collect(Collectors.toSet()))
                .build();
        ColumnSource secColumn = ColumnSource.builder()
                .name("date_field")
                .type(ColumnType.TIMESTAMP.name())
                .constraintTypes(Stream.of(ConstraintType.NOT_NULL.name()).collect(Collectors.toSet()))
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
                .referenceType(ReferenceType.ONE_TO_MANY.name())
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


//    @Test
//    void testDto() {
//        var dtoInfo = dtoMetaInfoFactory.buildInfo(entityInfos).values();
//        System.out.println(dtoFactory.buildDto(List.of(dtoInfo), entityInfos.stream()
//                .collect(toMap(EntityInfo::getEntityName, Function.identity()))));
//    }
}
