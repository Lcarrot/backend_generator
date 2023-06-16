package ru.tyshchenko.vkr.engine.resolver.dto.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.engine.api.models.dto.entity.EntityDtoInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.RepositoryInfo;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.tyshchenko.vkr.util.StringUtils.toCamelCase;

@Component
@RequiredArgsConstructor
public class DtoMetaInfoFactory {

    public Map<String, EntityDtoInfo> buildInfo(List<RepositoryInfo> entityInfos) {
        return entityInfos.stream().map(repositoryInfo ->
                        EntityDtoInfo.builder()
                                .entityName(toCamelCase(repositoryInfo.getEntityName()))
                                .repositoryName(repositoryInfo.getName())
                                .name(repositoryInfo.getEntityName() + "Dto")
                                .build())
                .collect(Collectors.toMap(EntityDtoInfo::getRepositoryName, Function.identity()));
    }
}