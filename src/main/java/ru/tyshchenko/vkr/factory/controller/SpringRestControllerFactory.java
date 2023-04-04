package ru.tyshchenko.vkr.factory.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.tyshchenko.vkr.dto.controller.source.ControllerMethodSource;
import ru.tyshchenko.vkr.dto.controller.source.ControllerSource;
import ru.tyshchenko.vkr.dto.service.meta.ServiceInfo;
import ru.tyshchenko.vkr.dto.service.meta.ServiceMethodInfo;
import ru.tyshchenko.vkr.util.PatternUtils;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static ru.tyshchenko.vkr.util.StringUtils.*;

@Component
@RequiredArgsConstructor
public class SpringRestControllerFactory {


    private String controllerPattern;
    private String methodPattern;

    @PostConstruct
    @SneakyThrows
    public void init() {
        controllerPattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/controller/Controller.pattern").toPath());
        methodPattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/controller/Method.pattern").toPath());
    }

    public Map<String, String> buildControllers(List<ControllerSource> controllerSources,
                                         Map<String, ServiceInfo> serviceInfoMap) {
        Map<String, String> controllers = new HashMap<>();
        Map<String, Map<String, ServiceMethodInfo>> serviceMethodInfoMap = serviceInfoMap.entrySet()
                .stream().collect(toMap(Map.Entry::getKey, entry -> entry.getValue()
                        .getServiceMethodInfos()
                        .stream().collect(toMap(ServiceMethodInfo::getMethodName, Function.identity()))));
        for (var controllerSource : controllerSources) {
            var controllerBuilder = new StringBuilder(controllerPattern);
            replaceByRegex(controllerBuilder, PatternUtils.CLASS_NAME, controllerSource.getName());
            replaceByRegex(controllerBuilder, "${service_name}",
                    toLowerCaseFirstLetter(toCamelCase(controllerSource.getServiceName())));
            replaceByRegex(controllerBuilder, "${service_class}",
                    toUpperCaseFirstLetter(toCamelCase(controllerSource.getServiceName())));
            String methods = buildMethods(controllerSource.getControllerMethodSources(),
                    serviceMethodInfoMap.get(controllerSource.getServiceName()), controllerSource.getServiceName());
            replaceByRegex(controllerBuilder, "${methods}", methods);
            controllers.put(controllerSource.getName(), controllerBuilder.toString());
        }
        return controllers;
    }

    private String buildMethods(List<ControllerMethodSource> methodSources,
                                Map<String, ServiceMethodInfo> serviceMethodInfoMap, String serviceName) {
        var methodsBuilder = new StringBuilder();
        for (var methodSource : methodSources) {
            var oneMethodBuilder = new StringBuilder(methodPattern);
            var serviceMethodInfo = serviceMethodInfoMap.get(methodSource.getServiceMethod());
            replaceByRegex(oneMethodBuilder, "${mapping}", methodSource.getMapping());
            replaceByRegex(oneMethodBuilder, "${response_dto}",
                    toUpperCaseFirstLetter(toCamelCase(serviceMethodInfo.getReturnDto().getName())));
            replaceByRegex(oneMethodBuilder, PatternUtils.METHOD_NAME, methodSource.getName());
            replaceByRegex(oneMethodBuilder, "${request_dto_class}",
                    toUpperCaseFirstLetter(toCamelCase(serviceMethodInfo.getRequestDto().getName())));
            replaceByRegex(oneMethodBuilder, "${request_dto}",
                    toLowerCaseFirstLetter(toCamelCase(serviceMethodInfo.getRequestDto().getName())));
            replaceByRegex(oneMethodBuilder, "${service_name}",
                    toLowerCaseFirstLetter(toCamelCase(serviceName)));
            replaceByRegex(oneMethodBuilder, "${service_method}",
                    toLowerCaseFirstLetter(toCamelCase(serviceMethodInfo.getMethodName())));
            methodsBuilder.append(oneMethodBuilder).append("\n");
        }
        return methodsBuilder.toString();
    }
}