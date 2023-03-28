package ru.tyshchenko.vkr.dto.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    private String name;
    @Builder.Default
    private List<RequestPartDto> requestPartDtos = new ArrayList<>();
}