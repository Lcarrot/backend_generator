package ru.tyshchenko.vkr.dto.repository.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.dto.entity.types.ColumnType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArgumentInfo {
    private ColumnType type;
    private String name;
}