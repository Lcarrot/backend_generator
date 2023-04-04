package ru.tyshchenko.vkr.factory.dto.request;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.tyshchenko.vkr.dto.dto.entity.EntityDtoInfo;
import ru.tyshchenko.vkr.dto.entity.java.ColumnTypeMapper;
import ru.tyshchenko.vkr.dto.repository.enums.OperationCondition;
import ru.tyshchenko.vkr.dto.repository.meta.ArgumentInfo;
import ru.tyshchenko.vkr.dto.repository.meta.ConditionInfo;
import ru.tyshchenko.vkr.dto.repository.meta.RepositoryMethodInfo;
import ru.tyshchenko.vkr.dto.service.meta.ServiceMethodInfo;
import ru.tyshchenko.vkr.util.PatternUtils;

import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static ru.tyshchenko.vkr.util.StringUtils.*;
import static ru.tyshchenko.vkr.util.StringUtils.replaceByRegex;

@Component
@RequiredArgsConstructor
public class RequestDtoFactory {


    private final ColumnTypeMapper columnTypeMapper;

    private String dtoPattern;

    @PostConstruct
    @SneakyThrows
    public void init() {
        dtoPattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/RequestDTO.pattern").toPath());
    }


    public Map<String, String> buildDtos(List<ServiceMethodInfo> serviceMethodInfos) {
        Map<String, String> dtos = new HashMap<>();
        for (var serviceMethodInfo : serviceMethodInfos) {
            Set<String> imports = new HashSet<>();
            StringBuilder dtoBuilder = new StringBuilder(dtoPattern);
            replaceByRegex(dtoBuilder, PatternUtils.CLASS_NAME,
                    toUpperCaseFirstLetter(toCamelCase(serviceMethodInfo.getRequestDto().getName())));
            replaceByRegex(dtoBuilder, PatternUtils.FIELDS, buildArguments(serviceMethodInfo.getRepositoryMethodInfos()
                    .values().stream().flatMap(Collection::stream).map(RepositoryMethodInfo::getConditionInfos)
                    .flatMap(Collection::stream)
                    .toList(), imports));
            replaceByRegex(dtoBuilder, PatternUtils.IMPORTS, buildImports(imports));
            dtos.put(serviceMethodInfo.getRequestDto().getName(), dtoBuilder.toString());
        }
        return dtos;
    }

    private String buildImports(Set<String> imports) {
        StringBuilder importString = new StringBuilder();
        for (String anImport : imports) {
            importString.append("import ").append(anImport).append(";\n");
        }
        return importString.toString();
    }

    private String buildArguments(List<ConditionInfo> conditionInfos, Set<String> imports) {
        StringBuilder argumentsBuilder = new StringBuilder();
        for (ConditionInfo conditionInfo : conditionInfos) {
            for (ArgumentInfo argumentInfo : conditionInfo.getArgumentInfos()) {
                ColumnTypeMapper.Type type = columnTypeMapper.mapType(argumentInfo.getType());
                if (type.packet() != null) {
                    imports.add(type.packet() + "." + type.type());
                }
                argumentsBuilder.append("private ")
                        .append(toUpperCaseFirstLetter(toCamelCase(type.type())))
                        .append(" ")
                        .append(toLowerCaseFirstLetter(toCamelCase(argumentInfo.getName())))
                        .append(";\n");
            }
        }
        return argumentsBuilder.toString();
    }
}