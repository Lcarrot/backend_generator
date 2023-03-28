package ru.tyshchenko.vkr.factory.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.dto.repository.api.RepositoryApi;
import ru.tyshchenko.vkr.dto.repository.meta.RepositoryMethodInfo;
import ru.tyshchenko.vkr.dto.repository.meta.RepositoryInfo;
import ru.tyshchenko.vkr.factory.ApiFactory;

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