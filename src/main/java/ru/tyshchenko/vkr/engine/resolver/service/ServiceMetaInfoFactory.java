package ru.tyshchenko.vkr.engine.resolver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.engine.api.models.dto.request.RequestDto;
import ru.tyshchenko.vkr.engine.api.models.dto.request.RequestPartDto;
import ru.tyshchenko.vkr.engine.api.models.dto.returns.PartDtoInfo;
import ru.tyshchenko.vkr.engine.api.models.dto.returns.ResponseDtoInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.ConditionInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.RepositoryInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.RepositoryMethodInfo;
import ru.tyshchenko.vkr.engine.api.models.service.meta.ServiceInfo;
import ru.tyshchenko.vkr.engine.api.models.service.meta.ServiceMethodInfo;
import ru.tyshchenko.vkr.engine.api.models.service.source.ServiceMethodSource;
import ru.tyshchenko.vkr.engine.api.models.service.source.ServiceSource;
import ru.tyshchenko.vkr.engine.resolver.type.mapper.ColumnTypeMapper;

import java.util.*;
import java.util.stream.Collectors;

import static ru.tyshchenko.vkr.engine.util.ClassNameUtils.SERVICE;
import static ru.tyshchenko.vkr.engine.util.StringUtils.toCamelCase;
import static ru.tyshchenko.vkr.engine.util.StringUtils.toUpperCaseFirstLetter;

@Component
@RequiredArgsConstructor
public class ServiceMetaInfoFactory {

    private final List<ColumnTypeMapper> columnTypeMapper;

    public Map<String, ServiceInfo> buildServiceInfo(List<ServiceSource> serviceSources,
                                                     Map<String, RepositoryInfo> repositoryInfoMap, String language) {
        Map<String, ServiceInfo> serviceInfoMap = new HashMap<>();
        for (ServiceSource source : serviceSources) {
            ServiceInfo serviceInfo = new ServiceInfo();
            serviceInfo.setName(source.getName() + SERVICE);
            var methodInfo = new ServiceMethodInfo();
            for (ServiceMethodSource methodSource : source.getMethods()) {
                methodInfo.setMethodName(methodSource.getName());
                var requestDto = new RequestDto();
                methodInfo.setRequestDto(requestDto);
                requestDto.setName(serviceInfo.getName() + toUpperCaseFirstLetter(methodSource.getName()) + "RequestDto");
                var containerDto = new ResponseDtoInfo();
                methodInfo.setReturnDto(containerDto);
                containerDto.setName(serviceInfo.getName() + toUpperCaseFirstLetter(methodSource.getName()) + "ResponseDto");
                for (var serRepMethod : methodSource.getRepositoryMethods()) {
                    RepositoryInfo repositoryInfo = repositoryInfoMap.get(serRepMethod.getRepositoryName());
                    var repMethodInfo = repositoryInfo.getRepositoryMethodInfos()
                            .get(serRepMethod.getRepositoryMethod());
                    requestDto.getRequestPartDtos().addAll(requestPartDtos(serRepMethod.getRepositoryMethod(),
                            repMethodInfo.getConditionInfos(), language));
                    containerDto.getParts().add(partDtoInfo(repositoryInfo, repMethodInfo));
                    methodInfo.getRepositoryMethodInfos()
                            .computeIfAbsent(repositoryInfo.getName(), key -> new ArrayList<>())
                            .add(repositoryInfo.getRepositoryMethodInfos()
                                    .get(serRepMethod.getRepositoryMethod()));
                }
            }
            serviceInfo.getServiceMethodInfos().add(methodInfo);
            serviceInfoMap.put(serviceInfo.getName(), serviceInfo);
        }
        return serviceInfoMap;
    }

    private List<RequestPartDto> requestPartDtos(String repMethodName, List<ConditionInfo> conditionInfo, String language) {
        return conditionInfo.stream().map(ConditionInfo::getArgumentInfos)
                .flatMap(Collection::stream)
                .map(argumentInfo -> RequestPartDto.builder()
                        .name(toCamelCase(repMethodName + argumentInfo.getName()))
                        .type(columnTypeMapper.stream().filter(mapper -> language.contains(mapper.language()))
                                .findFirst().orElseThrow().mapType(argumentInfo.getType()))
                        .build()).collect(Collectors.toList());
    }

    private PartDtoInfo partDtoInfo(RepositoryInfo repositoryInfo, RepositoryMethodInfo methodInfo) {
        return PartDtoInfo.builder().entityName(repositoryInfo.getEntityName())
                .returnType(methodInfo.getReturnType()).build();
    }
}
