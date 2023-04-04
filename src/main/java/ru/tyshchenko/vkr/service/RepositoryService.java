package ru.tyshchenko.vkr.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.tyshchenko.vkr.context.SimpleBuilderContext;
import ru.tyshchenko.vkr.dto.repository.api.RepositoryApi;
import ru.tyshchenko.vkr.dto.repository.source.RepositorySource;
import ru.tyshchenko.vkr.factory.dto.entity.DtoEntityFactory;
import ru.tyshchenko.vkr.factory.dto.entity.DtoMetaInfoFactory;
import ru.tyshchenko.vkr.factory.repository.RepositoryApiFactory;
import ru.tyshchenko.vkr.factory.repository.RepositoryFactory;
import ru.tyshchenko.vkr.factory.repository.RepositoryMetaInfoFactory;
import ru.tyshchenko.vkr.util.UploadUtils;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RepositoryService implements LayerService<RepositorySource, RepositoryApi> {

    private final RepositoryFactory repositoryFactory;
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
        var repos = repositoryFactory.buildRepository(meta.values().stream().toList());
        context.setRepositoryInfoMap(meta);
        Path projectPath = context.getSourceCodePath().resolve("repository");
        context.setRepositoryInfoMap(meta);
        UploadUtils.saveFile(projectPath, context.getPacket(), repos);
        var dtoMeta = dtoMetaInfoFactory
                .buildInfo(context.getRepositoryInfoMap().values().stream().toList());
        context.setEntityDtoInfoMap(dtoMeta);
        var values = dtoEntityFactory.buildDto(dtoMeta.values().stream().toList(),
                context.getEntityInfoMap());
        Path dirPath = context.getSourceCodePath().resolve("dto").resolve("entity");
        UploadUtils.saveFile(dirPath, context.getPacket(), values);
    }

    @Override
    public List<RepositoryApi> getApi() {
        return apiFactory.buildApi(context.getRepositoryInfoMap().values().stream().toList());
    }
}
