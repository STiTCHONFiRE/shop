package ru.stitchonfire.resource.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.stitchonfire.resource.dto.OrderDto;
import ru.stitchonfire.resource.dto.OrderPositionDto;
import ru.stitchonfire.resource.dto.ReviewDto;
import ru.stitchonfire.resource.model.Order;
import ru.stitchonfire.resource.model.OrderPosition;
import ru.stitchonfire.resource.model.Review;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "products", source = "order.orderPositions", qualifiedByName = "listMapperWithReview")
    OrderDto map(Order order);

    @Mapping(target = "products", source = "order.orderPositions", qualifiedByName = "listMapperIgnoreReview")
    OrderDto mapIgnoreReview(Order order);

    @Named("listMapperIgnoreReview")
    @IterableMapping(qualifiedByName = "mapIgnoreReview")
    List<OrderPositionDto> mapOrderPositionsIgnoreReview(List<OrderPosition> orderPositions);

    @Named("listMapperWithReview")
    @IterableMapping(qualifiedByName = "mapWithReview")
    List<OrderPositionDto> mapOrderPositionsWithReview(List<OrderPosition> orderPositions);

    @Named("mapIgnoreReview")
    @Mapping(target = "review", ignore = true)
    OrderPositionDto mapOrderPositionIgnoreReview(OrderPosition orderPosition);

    @Named("mapWithReview")
    OrderPositionDto map(OrderPosition orderPosition);

    @Mapping(target = "review", ignore = true)
    @IterableMapping(qualifiedByName = "mapIgnoreReview")
    OrderPositionDto mapIgnoreReview(OrderPosition orderPosition);

    @Mapping(target = "username", source = "review.orderPosition.order.username")
    @Mapping(target = "dateOfPurchase", source = "review.orderPosition.order.creationTimestamp")
    ReviewDto map(Review review);
}
