package ru.tyshchenko.vkr.dto.controller.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControllerSource {

    private String name;
    private String service;
    private List<ControllerMethodSource> methods;
}