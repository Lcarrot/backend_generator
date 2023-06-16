package ru.tyshchenko.vkr.engine.resolver.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.engine.api.models.repository.api.RepositoryApi;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.RepositoryMethodInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.RepositoryInfo;
import ru.tyshchenko.vkr.engine.resolver.ApiFactory;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RepositoryApiFactory implements ApiFactory<RepositoryApi, RepositoryInfo> {
    @Override
    public List<RepositoryApi> buildApi(List<RepositoryInfo> metaInfo) {
        return metaInfo.stream().map(info -> RepositoryApi
                        .builder()
                        .name(info.getName())
                        .methods(info.getRepositoryMethodInfos().values().stream()
                                .map(RepositoryMethodInfo::getName).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}