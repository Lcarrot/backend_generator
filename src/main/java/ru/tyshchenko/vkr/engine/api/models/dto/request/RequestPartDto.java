package ru.tyshchenko.vkr.engine.api.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.engine.resolver.type.mapper.Type;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPartDto {

    private Type type;
    private String name;
}