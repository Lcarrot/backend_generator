package ru.tyshchenko.vkr.engine.api.models.repository.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryApi {

    private String name;
    private List<String> methods;
}