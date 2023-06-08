package ru.stitchonfire.resource.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderPositionDto {
    Long n;
    String serialCode;
    ProductShortDto product;
    ReviewDto review;
}
