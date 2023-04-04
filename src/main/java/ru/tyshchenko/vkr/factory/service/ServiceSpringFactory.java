package ru.tyshchenko.vkr.factory.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.tyshchenko.vkr.dto.dto.entity.EntityDtoInfo;
import ru.tyshchenko.vkr.dto.repository.enums.ReturnType;
import ru.tyshchenko.vkr.dto.repository.meta.ArgumentInfo;
import ru.tyshchenko.vkr.dto.repository.meta.ConditionInfo;
import ru.tyshchenko.vkr.dto.service.meta.ServiceInfo;
import ru.tyshchenko.vkr.dto.service.meta.ServiceMethodInfo;
import ru.tyshchenko.vkr.util.PatternUtils;

import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static ru.tyshchenko.vkr.util.StringUtils.*;

@Component
@RequiredArgsConstructor
public class ServiceSpringFactory implements ServiceFactory {

    private String servicePattern;
    private String serviceMethodPattern;
    private String serviceSetValuePattern;
    private String serviceSetValuesPattern;

    @PostConstruct
    @SneakyThrows
    public void init() {
        servicePattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/service/Service.pattern").toPath());
        serviceMethodPattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/service/Method.pattern").toPath());
        serviceSetValuePattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/service/GetAndSetValue.pattern").toPath());
        serviceSetValuesPattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/service/GetAndSetValues.pattern").toPath());
    }

    public Map<String, String> buildServices(List<ServiceInfo> serviceInfos, Map<String, EntityDtoInfo> entityDtoInfoMap) {
        Map<String, String> services = new HashMap<>();
        for (var serviceInfo : serviceInfos) {
            Set<String> imports = new HashSet<>();
            StringBuilder serviceBuilder = new StringBuilder(servicePattern);
            replaceByRegex(serviceBuilder, PatternUtils.CLASS_NAME, serviceInfo.getName());
            String fields = buildFields(serviceInfo.getServiceMethodInfos().stream()
                    .map(methodInfo -> methodInfo.getRepositoryMethodInfos().keySet())
                    .flatMap(Collection::stream).collect(Collectors.toSet()), imports);
            replaceByRegex(serviceBuilder, PatternUtils.FIELDS, fields);
            StringBuilder methodsBuilder = new StringBuilder();
            for (var methodInfo : serviceInfo.getServiceMethodInfos()) {
                methodsBuilder.append(buildMethod(methodInfo, entityDtoInfoMap, imports)).append("\n");
            }
            replaceByRegex(serviceBuilder, PatternUtils.METHODS, methodsBuilder.toString());
            replaceByRegex(serviceBuilder, PatternUtils.IMPORTS, buildImports(imports));
            services.put(serviceInfo.getName(), serviceBuilder.toString());
        }
        return services;
    }

    private String buildImports(Set<String> imports) {
        StringBuilder importString = new StringBuilder();
        for (String anImport : imports) {
            importString.append("import ").append(anImport).append(";\n");
        }
        return importString.toString();
    }

    private String buildFields(Set<String> serviceNames, Set<String> imports) {
        StringBuilder fieldBuilder = new StringBuilder();
        for (String service : serviceNames) {
            fieldBuilder.append(TAB).append("private final ")
                    .append(toUpperCaseFirstLetter(toCamelCase(service))).append(" ")
                    .append(toCamelCase(service)).append(";\n");
            imports.add("${packet}.repository." + toUpperCaseFirstLetter(toCamelCase(service)));
        }
        return fieldBuilder.toString();
    }

    private String buildMethod(ServiceMethodInfo serviceMethodInfo,
                               Map<String, EntityDtoInfo> entityDtoInfoMap,
                               Set<String> imports) {
        StringBuilder methodBuilder = new StringBuilder(serviceMethodPattern);
        String returnName = toCamelCase(serviceMethodInfo.getReturnDto().getName());
        imports.add("${packet}.dto.response." + toUpperCaseFirstLetter(toCamelCase(returnName)));
        replaceByRegex(methodBuilder, "${return_dto}", toLowerCaseFirstLetter(returnName));
        replaceByRegex(methodBuilder, "${return_dto_class}", toUpperCaseFirstLetter(returnName));
        String requestName = toCamelCase(serviceMethodInfo.getRequestDto().getName());
        imports.add("${packet}.dto.request." + toUpperCaseFirstLetter(toCamelCase(requestName)));
        replaceByRegex(methodBuilder, "${request_dto}", toLowerCaseFirstLetter(requestName));
        replaceByRegex(methodBuilder, "${request_dto_class}", toUpperCaseFirstLetter(requestName));
        replaceByRegex(methodBuilder, "${method_name}", serviceMethodInfo.getMethodName());
        StringBuilder bodyBuilder = new StringBuilder();
        for (var repInfo : serviceMethodInfo.getRepositoryMethodInfos().entrySet()) {
            String repName = repInfo.getKey();
            for (var repMethodInfo : repInfo.getValue()) {
                StringBuilder setBuilder = new StringBuilder(repMethodInfo.getReturnType() == ReturnType.ENTITY
                        ? serviceSetValuePattern : serviceSetValuesPattern);
                String dtoName = entityDtoInfoMap.get(repName).getName();
                replaceByRegex(setBuilder, "${entity_dto_name}", toUpperCaseFirstLetter(toCamelCase(dtoName)));
                imports.add("${packet}.dto." + toUpperCaseFirstLetter(toCamelCase(dtoName)));
                replaceByRegex(setBuilder, "${return_dto}", returnName);
                replaceByRegex(setBuilder, "${repository_name}", toCamelCase(repName));
                replaceByRegex(setBuilder, "${repository_method}", repMethodInfo.getName());
                replaceByRegex(setBuilder, "${arguments}", buildArguments(toLowerCaseFirstLetter(requestName),
                        repMethodInfo.getConditionInfos()));
                bodyBuilder.append(setBuilder).append("\n");
            }
        }
        replaceByRegex(methodBuilder, "${main_part}", bodyBuilder.toString());
        return methodBuilder.toString();
    }

    private String buildArguments(String requestDto, List<ConditionInfo> conditionInfos) {
        return conditionInfos.stream().map(ConditionInfo::getArgumentInfos)
                .flatMap(Collection::stream)
                .map(ArgumentInfo::getName)
                .map(argument -> requestDto + ".get" + toUpperCaseFirstLetter(toCamelCase(argument)) + "()")
                .collect(Collectors.joining(", "));
    }
}