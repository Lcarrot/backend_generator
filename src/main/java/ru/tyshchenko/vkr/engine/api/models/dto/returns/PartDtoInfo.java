package ru.tyshchenko.vkr.engine.api.models.dto.returns;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.engine.api.models.repository.enums.ReturnType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartDtoInfo {

    private String entityName;
    private ReturnType returnType;
}
