package ru.tyshchenko.vkr.dto.controller.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControllerMethodSource {

    private String name;
    private String mapping;
    private String serviceMethod;
}