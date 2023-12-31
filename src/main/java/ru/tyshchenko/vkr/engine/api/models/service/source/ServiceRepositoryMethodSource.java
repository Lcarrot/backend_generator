package ru.tyshchenko.vkr.engine.api.models.service.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRepositoryMethodSource {

    private String repositoryName;
    private String repositoryMethod;
}