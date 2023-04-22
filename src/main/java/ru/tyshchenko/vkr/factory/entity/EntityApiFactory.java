package ru.tyshchenko.vkr.factory.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.dto.entity.api.EntityApi;
import ru.tyshchenko.vkr.dto.entity.meta.Column;
import ru.tyshchenko.vkr.dto.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.factory.ApiFactory;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EntityApiFactory implements ApiFactory<EntityApi, EntityInfo> {
    @Override
    public List<EntityApi> buildApi(List<EntityInfo> metaInfo) {
        return metaInfo.stream().map(info ->
            EntityApi.builder().name(info.getEntityName())
                    .columns(info.getColumns().stream()
                            .map(Column::getName).collect(Collectors.toList()))
                    .build()
        ).collect(Collectors.toList());
    }
}