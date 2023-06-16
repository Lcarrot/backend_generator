package ru.tyshchenko.vkr.engine.resolver.type.mapper;

import lombok.NonNull;

public record Type(String packet, @NonNull String type) {
}
