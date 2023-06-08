package ru.stitchonfire.resource.dto;

import lombok.Builder;

@Builder
public record TypeDto(
        String name,
        String tp
) {
}
