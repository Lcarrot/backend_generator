package ru.tyshchenko.vkr.engine.api.models.service.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInfo {

    private String name;
    @Builder.Default
    private List<ServiceMethodInfo> serviceMethodInfos = new ArrayList<>();
}