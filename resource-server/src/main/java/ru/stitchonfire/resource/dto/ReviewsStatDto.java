package ru.stitchonfire.resource.dto;

import lombok.Builder;

@Builder
public record ReviewsStatDto(
        Long n,
        Long[] pc
) {
}
