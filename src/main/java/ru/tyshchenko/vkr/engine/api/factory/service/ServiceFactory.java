package ru.tyshchenko.vkr.engine.api.factory.service;

import ru.tyshchenko.vkr.engine.api.models.dto.entity.EntityDtoInfo;
import ru.tyshchenko.vkr.engine.api.models.service.meta.ServiceInfo;

import java.util.List;
import java.util.Map;

public interface ServiceFactory {

    Map<String, String> buildServices(List<ServiceInfo> serviceInfos, Map<String, EntityDtoInfo> entityDtoInfoMap);

    String language();
}
