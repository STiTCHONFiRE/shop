package ru.stitchonfire.resource.mapper;

import org.mapstruct.Mapper;
import ru.stitchonfire.resource.dto.OrderDto;
import ru.stitchonfire.resource.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto map(Order order);
}
