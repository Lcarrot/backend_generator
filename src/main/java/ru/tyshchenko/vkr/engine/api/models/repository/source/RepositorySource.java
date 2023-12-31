package ru.tyshchenko.vkr.engine.api.models.repository.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositorySource {

    private String name;
    private String entityName;
    private List<RepositoryMethodSource> methods;
}