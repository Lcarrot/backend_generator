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
public class RepositoryMethodSource {

    public String returnType;
    public List<RepositoryMethodCondition> conditions;

}
