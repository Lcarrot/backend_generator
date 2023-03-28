package ru.tyshchenko.vkr.dto.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.dto.entity.java.ColumnTypeMapper;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPartDto {

    private ColumnTypeMapper.Type type;
    private String name;
}