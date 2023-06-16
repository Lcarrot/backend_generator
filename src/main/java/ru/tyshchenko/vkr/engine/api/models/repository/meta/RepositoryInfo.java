package ru.tyshchenko.vkr.engine.api.models.repository.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryInfo {

    private String name;
    private String entityName;
    @Builder.Default
    private Map<String, RepositoryMethodInfo> repositoryMethodInfos = new HashMap<>();
}