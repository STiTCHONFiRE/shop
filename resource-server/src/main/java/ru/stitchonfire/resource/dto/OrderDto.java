package ru.stitchonfire.resource.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.stitchonfire.resource.state.OrderState;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDto {
    UUID id;
    OrderState orderState;
    List<OrderPositionDto> products;
    Instant instant;
}
