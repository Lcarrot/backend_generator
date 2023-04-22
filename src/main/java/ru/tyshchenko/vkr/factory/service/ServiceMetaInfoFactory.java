package ru.tyshchenko.vkr.factory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.dto.dto.request.RequestDto;
import ru.tyshchenko.vkr.dto.dto.request.RequestPartDto;
import ru.tyshchenko.vkr.dto.dto.returns.ResponseDtoInfo;
import ru.tyshchenko.vkr.dto.dto.returns.PartDtoInfo;
import ru.tyshchenko.vkr.dto.entity.java.ColumnTypeMapper;
import ru.tyshchenko.vkr.dto.repository.meta.ConditionInfo;
import ru.tyshchenko.vkr.dto.repository.meta.RepositoryInfo;
import ru.tyshchenko.vkr.dto.repository.meta.RepositoryMethodInfo;
import ru.tyshchenko.vkr.dto.service.meta.ServiceInfo;
import ru.tyshchenko.vkr.dto.service.meta.ServiceMethodInfo;
import ru.tyshchenko.vkr.dto.service.source.ServiceMethodSource;
import ru.tyshchenko.vkr.dto.service.source.ServiceSource;

import java.util.*;
import java.util.stream.Collectors;

import static ru.tyshchenko.vkr.util.ClassNameUtils.SERVICE;
import static ru.tyshchenko.vkr.util.StringUtils.*;

@Component
@RequiredArgsConstructor
public class ServiceMetaInfoFactory {

    private final ColumnTypeMapper columnTypeMapper;

    public Map<String, ServiceInfo> buildServiceInfo(List<ServiceSource> serviceSources,
                                                     Map<String, RepositoryInfo> repositoryInfoMap) {
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
                            repMethodInfo.getConditionInfos()));
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

    private List<RequestPartDto> requestPartDtos(String repMethodName, List<ConditionInfo> conditionInfo) {
        return conditionInfo.stream().map(ConditionInfo::getArgumentInfos)
                .flatMap(Collection::stream)
                .map(argumentInfo -> RequestPartDto.builder()
                        .name(toCamelCase(repMethodName + argumentInfo.getName()))
                        .type(columnTypeMapper.mapType(argumentInfo.getType()))
                        .build()).collect(Collectors.toList());
    }

    private PartDtoInfo partDtoInfo(RepositoryInfo repositoryInfo, RepositoryMethodInfo methodInfo) {
        return PartDtoInfo.builder().entityName(repositoryInfo.getEntityName())
                .returnType(methodInfo.getReturnType()).build();
    }
}
