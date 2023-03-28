package ru.tyshchenko.vkr.dto.service.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.dto.dto.request.RequestDto;
import ru.tyshchenko.vkr.dto.dto.returns.ResponseDtoInfo;
import ru.tyshchenko.vkr.dto.repository.meta.RepositoryMethodInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMethodInfo {

    private String methodName;
    private RequestDto requestDto;
    private ResponseDtoInfo returnDto;
    @Builder.Default
    private Map<String, List<RepositoryMethodInfo>> repositoryMethodInfos = new HashMap<>();
}
