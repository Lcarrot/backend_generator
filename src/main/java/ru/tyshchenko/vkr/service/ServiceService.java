package ru.tyshchenko.vkr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.tyshchenko.vkr.context.SimpleBuilderContext;
import ru.tyshchenko.vkr.dto.service.api.ServiceApi;
import ru.tyshchenko.vkr.dto.service.meta.ServiceInfo;
import ru.tyshchenko.vkr.dto.service.meta.ServiceMethodInfo;
import ru.tyshchenko.vkr.dto.service.source.ServiceSource;
import ru.tyshchenko.vkr.factory.dto.request.RequestDtoFactory;
import ru.tyshchenko.vkr.factory.dto.response.ResponseDtoFactory;
import ru.tyshchenko.vkr.factory.service.ServiceApiFactory;
import ru.tyshchenko.vkr.factory.service.ServiceFactory;
import ru.tyshchenko.vkr.factory.service.ServiceMetaInfoFactory;
import ru.tyshchenko.vkr.util.UploadUtils;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService implements LayerService<ServiceSource, ServiceApi> {

    private final ServiceApiFactory apiFactory;
    private final ServiceMetaInfoFactory metaInfoFactory;
    private final RequestDtoFactory requestDtoFactory;
    private final ResponseDtoFactory responseDtoFactory;
    private final ServiceFactory serviceFactory;
    private final SimpleBuilderContext context;

    @Override
    public void save(List<ServiceSource> source) {
        var metaInfo = metaInfoFactory.buildServiceInfo(source, context.getRepositoryInfoMap());
        context.setServiceInfoMap(metaInfo);
        var values = serviceFactory.buildServices(metaInfo
                .values().stream().toList(), context.getEntityDtoInfoMap());
        Path dirPath = context.getSourceCodePath().resolve("service");
        UploadUtils.saveFile(dirPath, context.getPacket(), values);
        List<ServiceMethodInfo> serviceMethodInfos = metaInfo.values().stream()
                .map(ServiceInfo::getServiceMethodInfos).flatMap(Collection::stream).toList();
        Path reqDtoPath = context.getSourceCodePath().resolve("dto").resolve("request");
        UploadUtils.saveFile(reqDtoPath, context.getPacket(), requestDtoFactory.buildDtos(serviceMethodInfos));
        Path respDtoPath = context.getSourceCodePath().resolve("dto").resolve("response");
        UploadUtils.saveFile(respDtoPath, context.getPacket(),
                responseDtoFactory.buildDtos(serviceMethodInfos, context.getEntityDtoInfoMap()));
    }

    @Override
    public List<ServiceApi> getApi() {
        return apiFactory.buildApi(context.getServiceInfoMap().values().stream().toList());
    }
}