package ru.tyshchenko.vkr.app.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.tyshchenko.vkr.app.context.SimpleBuilderContext;
import ru.tyshchenko.vkr.engine.api.models.repository.api.RepositoryApi;
import ru.tyshchenko.vkr.engine.api.models.repository.source.RepositorySource;
import ru.tyshchenko.vkr.engine.impl.SupportedRepositoryLanguage;
import ru.tyshchenko.vkr.engine.resolver.dto.entity.DtoEntityFactory;
import ru.tyshchenko.vkr.engine.resolver.dto.entity.DtoMetaInfoFactory;
import ru.tyshchenko.vkr.engine.resolver.repository.RepositoryApiFactory;
import ru.tyshchenko.vkr.engine.resolver.repository.RepositoryMetaInfoFactory;
import ru.tyshchenko.vkr.engine.resolver.repository.RepositoryResolver;
import ru.tyshchenko.vkr.engine.util.UploadUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class RepositoryService implements LayerService<RepositorySource, RepositoryApi> {

    private final RepositoryResolver repositoryResolver;
    private final RepositoryApiFactory apiFactory;
    private final RepositoryMetaInfoFactory metaInfoFactory;
    private final DtoMetaInfoFactory dtoMetaInfoFactory;
    private final DtoEntityFactory dtoEntityFactory;
    private final SimpleBuilderContext context;

    @Override
    @SneakyThrows
    public void save(List<RepositorySource> source) {
        var entities = context.getEntityInfoMap();
        var meta = metaInfoFactory.buildRepositoryInfo(source, entities);
        var repos = repositoryResolver
                .buildClasses(meta.values().stream().toList(), SupportedRepositoryLanguage.JAVA_SPRING_DATA.name());
        context.setRepositoryInfoMap(meta);
        Path projectPath = context.getSourceCodePath().resolve("repository");
        context.setRepositoryInfoMap(meta);
        UploadUtils.saveFile(projectPath, context.getPacket(), repos);
        var dtoMeta = dtoMetaInfoFactory
                .buildInfo(context.getRepositoryInfoMap().values().stream().toList());
        context.setEntityDtoInfoMap(dtoMeta);
        var values = dtoEntityFactory.buildDto(dtoMeta.values().stream().toList(),
                context.getEntityInfoMap(), SupportedRepositoryLanguage.JAVA_SPRING_DATA.name())
                .entrySet().stream().collect(toMap(entry -> entry.getKey() + "Dto", Map.Entry::getValue));
        Path dirPath = context.getSourceCodePath().resolve("dto").resolve("entity");
        UploadUtils.saveFile(dirPath, context.getPacket(), values);
    }

    @Override
    public List<RepositoryApi> getApi() {
        return apiFactory.buildApi(context.getRepositoryInfoMap().values().stream().toList());
    }
}
