package ru.tyshchenko.vkr.engine.resolver.entity.sql;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.engine.api.models.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.engine.api.factory.entity.sql.SqlCodeFactory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SqlResolver {

    private final List<SqlCodeFactory> sqlCodeFactories;

    public String buildSql(List<EntityInfo> entityInfos, String dialect) {
        SqlCodeFactory factory = sqlCodeFactories.stream()
                .filter(sqlCodeFactory -> sqlCodeFactory.dialect().equals(dialect))
                .findAny().orElseThrow();
        return factory.generate(entityInfos);
    }
}