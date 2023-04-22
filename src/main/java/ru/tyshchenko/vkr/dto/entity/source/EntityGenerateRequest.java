package ru.tyshchenko.vkr.dto.entity.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityGenerateRequest {

    private List<Entity> entities;
    private List<ReferenceColumn> references;
}
