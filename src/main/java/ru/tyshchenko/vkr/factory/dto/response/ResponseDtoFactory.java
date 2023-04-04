package ru.tyshchenko.vkr.factory.dto.response;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.tyshchenko.vkr.dto.dto.entity.EntityDtoInfo;
import ru.tyshchenko.vkr.dto.service.meta.ServiceMethodInfo;
import ru.tyshchenko.vkr.util.PatternUtils;

import java.nio.file.Files;
import java.util.*;

import static ru.tyshchenko.vkr.util.StringUtils.*;

@Component
@RequiredArgsConstructor
public class ResponseDtoFactory {

    private String dtoPattern;

    @PostConstruct
    @SneakyThrows
    public void init() {
        dtoPattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/ResponseDTO.pattern").toPath());
    }

    public Map<String, String> buildDtos(List<ServiceMethodInfo> serviceMethodInfos,
                                  Map<String, EntityDtoInfo> entityDtoInfoMap) {
        Map<String, String> dtos = new HashMap<>();
        for (var serviceMethodInfo: serviceMethodInfos) {
            Set<String> imports = new HashSet<>();
            StringBuilder dtoBuilder = new StringBuilder(dtoPattern);
            replaceByRegex(dtoBuilder, PatternUtils.CLASS_NAME,
                    toUpperCaseFirstLetter(toCamelCase(serviceMethodInfo.getReturnDto().getName())));
            StringBuilder fieldsBuilder = new StringBuilder();
            for (var repositoryInfo :serviceMethodInfo.getRepositoryMethodInfos().keySet()) {
                String name = toCamelCase(entityDtoInfoMap.get(repositoryInfo).getName());
                imports.add("${packet}.dto." + toUpperCaseFirstLetter(name));
                fieldsBuilder.append(TAB).append("List<").append(toUpperCaseFirstLetter(name)).append(">").append(" ").append(name).append(";\n");
            }
            replaceByRegex(dtoBuilder, PatternUtils.FIELDS, fieldsBuilder.toString());
            replaceByRegex(dtoBuilder, PatternUtils.IMPORTS, buildImports(imports));
            dtos.put(serviceMethodInfo.getReturnDto().getName(), dtoBuilder.toString());
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
}