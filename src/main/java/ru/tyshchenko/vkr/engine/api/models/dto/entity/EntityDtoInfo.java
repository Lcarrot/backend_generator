package ru.tyshchenko.vkr.engine.api.models.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityDtoInfo {

    private String name;
    private String entityName;
    private String repositoryName;
}