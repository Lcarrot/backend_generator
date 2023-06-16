package ru.tyshchenko.vkr.engine.api.models.service.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceApi {

    private String name;
    private List<String> methods;
}