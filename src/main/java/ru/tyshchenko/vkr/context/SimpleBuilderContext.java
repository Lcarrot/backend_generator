package ru.tyshchenko.vkr.context;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.dto.dto.entity.EntityDtoInfo;
import ru.tyshchenko.vkr.dto.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.dto.repository.meta.RepositoryInfo;
import ru.tyshchenko.vkr.dto.service.meta.ServiceInfo;

import java.nio.file.Path;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Data
public class SimpleBuilderContext {

    private Path projectPath;
    private Path sourceCodePath;
    private String packet;
    private Map<String, EntityInfo> entityInfoMap;
    private Map<String, RepositoryInfo> repositoryInfoMap;
    private Map<String, ServiceInfo> serviceInfoMap;
    private Map<String, EntityDtoInfo> entityDtoInfoMap;
}
