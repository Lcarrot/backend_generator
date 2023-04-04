package ru.tyshchenko.vkr.factory.service;

import ru.tyshchenko.vkr.dto.dto.entity.EntityDtoInfo;
import ru.tyshchenko.vkr.dto.service.meta.ServiceInfo;

import java.util.List;
import java.util.Map;

public interface ServiceFactory {

    Map<String, String> buildServices(List<ServiceInfo> serviceInfos, Map<String, EntityDtoInfo> entityDtoInfoMap);
}
