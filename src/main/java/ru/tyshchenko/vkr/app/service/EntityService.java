package ru.tyshchenko.vkr.app.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.tyshchenko.vkr.app.context.SimpleBuilderContext;
import ru.tyshchenko.vkr.engine.api.models.entity.api.EntityApi;
import ru.tyshchenko.vkr.engine.api.models.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.engine.api.models.entity.source.Entity;
import ru.tyshchenko.vkr.engine.api.models.entity.source.ReferenceColumn;
import ru.tyshchenko.vkr.engine.impl.SupportedDbLanguage;
import ru.tyshchenko.vkr.engine.impl.SupportedORMLanguage;
import ru.tyshchenko.vkr.engine.resolver.entity.EntityApiFactory;
import ru.tyshchenko.vkr.engine.resolver.entity.EntityInfoMetaInfoFactory;
import ru.tyshchenko.vkr.engine.resolver.entity.app.EntityClassesResolver;
import ru.tyshchenko.vkr.engine.resolver.entity.sql.SqlResolver;
import ru.tyshchenko.vkr.engine.util.UploadUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EntityService {

    private final EntityInfoMetaInfoFactory metaInfoFactory;
    private final EntityClassesResolver appEntityResolver;
    private final SqlResolver sqlResolver;
    private final EntityApiFactory entityApiFactory;
    private final SimpleBuilderContext context;

    public void save(List<Entity> entity, List<ReferenceColumn> referenceColumns) {
        Map<String, EntityInfo> entityInfos = metaInfoFactory.getMetaInfo(entity, referenceColumns);
        context.setEntityInfoMap(entityInfos);
        List<EntityInfo> infos = entityInfos.values().stream().toList();
        saveEntityJava(infos);
        saveEntitySql(infos);
    }

    public List<EntityApi> getApi() {
        return entityApiFactory.buildApi(context.getEntityInfoMap().values().stream().toList());
    }

    @SneakyThrows
    private void saveEntityJava(List<EntityInfo> entityInfos) {
        Path projectPath = context.getSourceCodePath().resolve("entity");
        Map<String, String> appEntities = appEntityResolver.buildEntityClasses(entityInfos, SupportedORMLanguage.JAVA_HIBERNATE.name());
        UploadUtils.saveFile(projectPath, context.getPacket(), appEntities);
    }

    @SneakyThrows
    private void saveEntitySql(List<EntityInfo> entityInfos) {
        Path projectPath = context.getProjectPath().resolve("db").resolve("migrations");
        if (!Files.exists(projectPath)) {
            Files.createDirectories(projectPath);
        }
        String sqlScript = sqlResolver.buildSql(entityInfos, SupportedDbLanguage.POSTGRESQL.name());
        var filePath = projectPath.resolve("V1_0__create_entities.sql");
        Files.write(filePath, sqlScript.getBytes());
    }
}