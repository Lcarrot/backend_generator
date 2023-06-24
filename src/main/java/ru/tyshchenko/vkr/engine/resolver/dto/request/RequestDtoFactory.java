package ru.tyshchenko.vkr.engine.resolver.dto.request;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.ArgumentInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.ConditionInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.RepositoryMethodInfo;
import ru.tyshchenko.vkr.engine.api.models.service.meta.ServiceMethodInfo;
import ru.tyshchenko.vkr.engine.api.factory.DefaultPlaceholder;
import ru.tyshchenko.vkr.engine.resolver.type.mapper.ColumnTypeMapper;
import ru.tyshchenko.vkr.engine.resolver.type.mapper.Type;

import java.nio.file.Files;
import java.util.*;

import static ru.tyshchenko.vkr.engine.util.StringUtils.*;

@Component
@RequiredArgsConstructor
public class RequestDtoFactory {


    private final List<ColumnTypeMapper> columnTypeMappers;

    private String dtoPattern;

    @PostConstruct
    @SneakyThrows
    public void init() {
        dtoPattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/RequestDTO.pattern").toPath());
    }


    public Map<String, String> buildDtos(List<ServiceMethodInfo> serviceMethodInfos, String language) {
        Map<String, String> dtos = new HashMap<>();
        for (var serviceMethodInfo : serviceMethodInfos) {
            Set<String> imports = new HashSet<>();
            StringBuilder dtoBuilder = new StringBuilder(dtoPattern);
            replaceByRegex(dtoBuilder, DefaultPlaceholder.CLASS_NAME,
                    toUpperCaseFirstLetter(toCamelCase(serviceMethodInfo.getRequestDto().getName())));
            replaceByRegex(dtoBuilder, DefaultPlaceholder.FIELDS, buildArguments(serviceMethodInfo.getRepositoryMethodInfos()
                    .values().stream().flatMap(Collection::stream).map(RepositoryMethodInfo::getConditionInfos)
                    .flatMap(Collection::stream)
                    .toList(), imports, language));
            replaceByRegex(dtoBuilder, DefaultPlaceholder.IMPORTS, buildImports(imports));
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

    private String buildArguments(List<ConditionInfo> conditionInfos, Set<String> imports, String language) {
        StringBuilder argumentsBuilder = new StringBuilder();
        for (ConditionInfo conditionInfo : conditionInfos) {
            for (ArgumentInfo argumentInfo : conditionInfo.getArgumentInfos()) {
                Type type = columnTypeMappers.stream()
                        .filter(mapper -> language.contains(mapper.language())).findFirst().orElseThrow()
                        .mapType(argumentInfo.getType());
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