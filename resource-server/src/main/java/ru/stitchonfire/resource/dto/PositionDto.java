package ru.stitchonfire.resource.dto;

import lombok.Builder;

@Builder
public record PositionDto(
        Long n,
        Long max,
        ProductShortDto product,
        boolean isAvailable
) {
}
