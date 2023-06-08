package ru.stitchonfire.resource.dto;

import lombok.Builder;
import ru.stitchonfire.resource.state.ReviewRating;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReviewDto(
        UUID id,
        String username,
        String reviewText,
        Instant dateOfPurchase,
        Instant creationTimestamp,
        ReviewRating reviewRating
) {
}
