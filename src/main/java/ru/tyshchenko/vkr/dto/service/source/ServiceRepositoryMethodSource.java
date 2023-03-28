package ru.tyshchenko.vkr.dto.service.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.dto.repository.meta.ArgumentInfo;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRepositoryMethodSource {

    private String repositoryName;
    private String methodName;
}