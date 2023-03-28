package ru.tyshchenko.vkr.dto.service.source;

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

    private String serviceName;
    private List<ServiceMethodSource> methodSources;
}