package ru.tyshchenko.vkr.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.tyshchenko.vkr.context.SimpleBuilderContext;
import ru.tyshchenko.vkr.dto.entity.api.EntityApi;
import ru.tyshchenko.vkr.dto.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.dto.entity.source.Entity;
import ru.tyshchenko.vkr.dto.entity.source.ReferenceColumn;
import ru.tyshchenko.vkr.factory.entity.EntityApiFactory;
import ru.tyshchenko.vkr.factory.entity.EntityInfoMetaInfoFactory;
import ru.tyshchenko.vkr.factory.entity.app.AppEntityFactory;
import ru.tyshchenko.vkr.factory.entity.sql.SqlEntityFactory;
import ru.tyshchenko.vkr.util.PathUtils;
import ru.tyshchenko.vkr.util.PatternUtils;
import ru.tyshchenko.vkr.util.UploadUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static ru.tyshchenko.vkr.util.StringUtils.toCamelCase;
import static ru.tyshchenko.vkr.util.StringUtils.toUpperCaseFirstLetter;

@Service
@RequiredArgsConstructor
public class EntityService {

    private final EntityInfoMetaInfoFactory metaInfoFactory;
    private final AppEntityFactory appEntityFactory;
    private final SqlEntityFactory sqlEntityFactory;
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
        Map<String, String> appEntities = appEntityFactory.buildEntities(entityInfos);
        UploadUtils.saveFile(projectPath, context.getPacket(), appEntities);
    }

    @SneakyThrows
    private void saveEntitySql(List<EntityInfo> entityInfos) {
        Path projectPath = context.getProjectPath().resolve("db").resolve("migrations");
        if (!Files.exists(projectPath)) {
            Files.createDirectories(projectPath);
        }
        String sqlScript = sqlEntityFactory.buildEntities(entityInfos);
        var filePath = projectPath.resolve("V1_0__create_entities.sql");
        Files.write(filePath, sqlScript.getBytes());
    }
}