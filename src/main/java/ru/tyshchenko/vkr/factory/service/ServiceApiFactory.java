package ru.tyshchenko.vkr.factory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.dto.service.api.ServiceApi;
import ru.tyshchenko.vkr.dto.service.meta.ServiceInfo;
import ru.tyshchenko.vkr.dto.service.meta.ServiceMethodInfo;
import ru.tyshchenko.vkr.factory.ApiFactory;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ServiceApiFactory implements ApiFactory<ServiceApi, ServiceInfo> {
    @Override
    public List<ServiceApi> buildApi(List<ServiceInfo> metaInfo) {
        return metaInfo.stream().map(info -> ServiceApi.builder()
                .name(info.getName())
                .methods(info.getServiceMethodInfos().stream()
                        .map(ServiceMethodInfo::getMethodName).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }
}