package ru.tyshchenko.vkr.engine.api.models.service.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceSource {

    private String name;
    private List<ServiceMethodSource> methods;
}