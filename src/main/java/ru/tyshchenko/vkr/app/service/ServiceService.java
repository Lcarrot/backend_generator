package ru.tyshchenko.vkr.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tyshchenko.vkr.app.context.SimpleBuilderContext;
import ru.tyshchenko.vkr.engine.api.models.service.api.ServiceApi;
import ru.tyshchenko.vkr.engine.api.models.service.meta.ServiceInfo;
import ru.tyshchenko.vkr.engine.api.models.service.meta.ServiceMethodInfo;
import ru.tyshchenko.vkr.engine.api.models.service.source.ServiceSource;
import ru.tyshchenko.vkr.engine.impl.SupportedServiceLanguage;
import ru.tyshchenko.vkr.engine.resolver.dto.request.RequestDtoFactory;
import ru.tyshchenko.vkr.engine.resolver.dto.response.ResponseDtoFactory;
import ru.tyshchenko.vkr.engine.resolver.service.ServiceApiFactory;
import ru.tyshchenko.vkr.engine.resolver.service.ServiceMetaInfoFactory;
import ru.tyshchenko.vkr.engine.resolver.service.ServiceResolver;
import ru.tyshchenko.vkr.engine.util.UploadUtils;

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
    private final ServiceResolver serviceResolver;
    private final SimpleBuilderContext context;

    @Override
    public void save(List<ServiceSource> source) {
        var metaInfo = metaInfoFactory.buildServiceInfo(source, context.getRepositoryInfoMap(),
                SupportedServiceLanguage.JAVA_SPRING.name());
        context.setServiceInfoMap(metaInfo);
        var values = serviceResolver.buildClasses(metaInfo
                .values().stream().toList(), context.getEntityDtoInfoMap(), SupportedServiceLanguage.JAVA_SPRING.name());
        Path dirPath = context.getSourceCodePath().resolve("service");
        UploadUtils.saveFile(dirPath, context.getPacket(), values);
        List<ServiceMethodInfo> serviceMethodInfos = metaInfo.values().stream()
                .map(ServiceInfo::getServiceMethodInfos).flatMap(Collection::stream).toList();
        Path reqDtoPath = context.getSourceCodePath().resolve("dto").resolve("request");
        UploadUtils.saveFile(reqDtoPath, context.getPacket(), requestDtoFactory
                .buildDtos(serviceMethodInfos, SupportedServiceLanguage.JAVA_SPRING.name()));
        Path respDtoPath = context.getSourceCodePath().resolve("dto").resolve("response");
        UploadUtils.saveFile(respDtoPath, context.getPacket(),
                responseDtoFactory.buildDtos(serviceMethodInfos, context.getEntityDtoInfoMap()));
    }

    @Override
    public List<ServiceApi> getApi() {
        return apiFactory.buildApi(context.getServiceInfoMap().values().stream().toList());
    }
}